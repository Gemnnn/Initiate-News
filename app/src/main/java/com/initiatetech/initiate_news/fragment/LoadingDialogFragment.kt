import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.initiatetech.initiate_news.R

class LoadingDialogFragment : DialogFragment() {

    private lateinit var tvProgressMessage: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var messageIndex = 0
    private val fullText = "Collecting news data..."
    private val delayMillis = 150L 

    private lateinit var textAnimationRunnable: Runnable

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_loading_dialog, container, false)
        tvProgressMessage = view.findViewById(R.id.tvProgressMessage)
        setupTextAnimation()
        return view
    }

    private fun setupTextAnimation() {
        // Initialize the runnable
        textAnimationRunnable = object : Runnable {
            override fun run() {

                if (messageIndex < fullText.length) {
                    tvProgressMessage.text = fullText.substring(0, messageIndex++)
                    handler.postDelayed(this, delayMillis)
                } else {
                    messageIndex = 0
                    tvProgressMessage.text = ""
                    handler.postDelayed(this, 1000) // Pause before restarting
                }
            }
        }
        // Start the animation
        handler.post(textAnimationRunnable)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(textAnimationRunnable) // Prevent memory leaks
    }

    companion object {
        fun newInstance() = LoadingDialogFragment()
    }
}
