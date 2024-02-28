package com.initiatetech.initiate_news.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.initiatetech.initiate_news.R
import com.initiatetech.initiate_news.model.NewsResponse
import java.util.Locale

class NewsTimelineAdapter(private val onTitleClick: (String) -> Unit) : RecyclerView.Adapter<NewsTimelineAdapter.NewsViewHolder>() {
    private var newsItems: List<NewsResponse> = ArrayList()

    fun submitList(items: List<NewsResponse>) {
        newsItems = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_LEFT else VIEW_TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = if (viewType == VIEW_TYPE_LEFT) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_left, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_right, parent, false)
        }
        return NewsViewHolder(view, onTitleClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsItems[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int = newsItems.size

    class NewsViewHolder(itemView: View, private val onTitleClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateView: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(newsItem: NewsResponse) {
            titleView.text = newsItem.title
            dateView.text = formatDate(newsItem.publishedDate)

            titleView.setOnClickListener { onTitleClick(newsItem.id.toString()) }
        }

        private fun formatDate(dateStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            return outputFormat.format(date)
        }
    }

    companion object {
        private const val VIEW_TYPE_LEFT = 0
        private const val VIEW_TYPE_RIGHT = 1
    }
}
