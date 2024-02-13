package com.initiatetech.initiate_news.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.databinding.FragmentUserBinding
import com.initiatetech.initiate_news.login.LoginActivity
import com.initiatetech.initiate_news.repository.UserRepository
import com.initiatetech.initiate_news.viewmodel.UserViewModel
import java.io.IOException
import java.util.Locale

class UserFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    // Declare the ActivityResultLauncher for location permissions
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    private lateinit var locationCallback: com.google.android.gms.location.LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Initialize the ActivityResultLauncher
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Toast.makeText(requireContext(), "Location access granted", Toast.LENGTH_SHORT).show()
                    if (isLocationEnabled()) {
                        getCurrentLocation()
                    } else {
                        Toast.makeText(requireContext(), "Location service is disabled. Please enable it.", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    updateLocationUI(location.latitude, location.longitude)
                    // Optionally stop location updates if you only need one update
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val factory = UserViewModel.UserViewModelFactory(UserRepository())
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnLogout.setOnClickListener {
            viewModel.logoutUser()
            val intent = Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        binding.btnGetLocation.setOnClickListener {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        return binding.root
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = ContextCompat.getSystemService(requireContext(), LocationManager::class.java) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Directly request new location data
            requestNewLocationData()
        } else {
            Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("StringFormatMatches")
    private fun updateLocationUI(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses?.get(0)
                    val country = address?.countryName // Get the country name
                    val province = address?.adminArea // Get the province or state

                    // Update your UI here with the country and province
                    val locationText = getString(R.string.location_text_with_country_province, latitude, longitude, country, province)
                    binding.etLocation.text = locationText
                } else {
                    // Handle case where no address is found
                    binding.etLocation.text = getString(R.string.location_text, latitude, longitude)
                }
            }
        } catch (e: IOException) {
            // Handle the situation where the geocoder is not available or fails
            e.printStackTrace()
            binding.etLocation.text = getString(R.string.location_text, latitude, longitude)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = UserFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun requestNewLocationData() {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1 // Only need a single update.
            maxWaitTime = 10000 // 10 seconds max wait time.
        }

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    updateLocationUI(location.latitude, location.longitude)
                }
                // Optionally, remove the location callback after receiving an update
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        // Check for permissions again, even though we've checked before calling this function
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }





}
