package com.initiatetech.initiate_news.adapter

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.model.LocationNewsResponse
import java.text.ParseException
import java.util.Locale

class LocationNewsTimeLineAdapter(private val onTitleClick: (String) -> Unit) : RecyclerView.Adapter<LocationNewsTimeLineAdapter.NewsViewHolder>() {
    private var locationNewsItems: List<LocationNewsResponse> = ArrayList()

    fun locationSubmitList(items: List<LocationNewsResponse>) {
        Log.d("LocationNewsAdapter", "Submitting list: ${items.size}")
        locationNewsItems = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_LEFT else VIEW_TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = if (viewType == VIEW_TYPE_LEFT) {
            LayoutInflater.from(parent.context).inflate(R.layout.location_news_left, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.location_news_right, parent, false)
        }
        return NewsViewHolder(view, onTitleClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val locationNewsItem = locationNewsItems[position]
        holder.bind(locationNewsItem)

        if (position == itemCount - 1) {
            holder.itemView.setBackgroundResource(R.drawable.item_shadow)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.normal_background)
        }
    }

    override fun getItemCount(): Int = locationNewsItems.size

    class NewsViewHolder(itemView: View, private val onTitleClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateView: TextView = itemView.findViewById(R.id.tvDate)
        private val keywordView: TextView = itemView.findViewById(R.id.tvLocationKeyword)

        fun bind(locationNewsItem: LocationNewsResponse) {
            titleView.text = locationNewsItem.shortTitle
            dateView.text = formatDate(locationNewsItem.publishedDate)
            keywordView.text = locationNewsItem.keyword
            itemView.setOnClickListener { onTitleClick(locationNewsItem.id.toString()) }
        }

        private fun formatDate(dateStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.getDefault())
            try {
                val date = inputFormat.parse(dateStr)
                return outputFormat.format(date)
            } catch (e: ParseException) {
                // Log the exception or handle it as needed
                e.printStackTrace()
                return dateStr
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_LEFT = 0
        private const val VIEW_TYPE_RIGHT = 1
    }
}
