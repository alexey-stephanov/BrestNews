package com.alexstephanov.brestnews.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexstephanov.brestnews.R
import com.alexstephanov.brestnews.models.NewsItem
import com.squareup.picasso.Picasso
import io.realm.RealmList

class DataAdapter(private val items: RealmList<NewsItem>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position]!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(item: NewsItem) {
            val thumbnailImageView: ImageView = itemView.findViewById(R.id.image_view_list_item_thumbnail)
            val titleTextView: TextView = itemView.findViewById(R.id.text_view_list_item_title)
            val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_list_item_description)
            val publicationDateTextView: TextView = itemView.findViewById(R.id.text_view_list_item_publication_date)
            val sourceTextView: TextView = itemView.findViewById(R.id.text_view_list_item_source)

            val convertText: ArrayList<String> = ArrayList()
            convertText.addAll(item.text)

            val newTitle: String = replaceSpecialSymbols(item.title)
            val newDescription: String = replaceSpecialSymbols(item.description)

            titleTextView.text = newTitle
            descriptionTextView.text = newDescription
            publicationDateTextView.text = item.date

            sourceTextView.text = when {
                item.link.contains("onlinebrest") -> "onlinebrest.by"
                item.link.contains("vb.by") -> "vb.by"
                item.link.contains("brestcity") -> "brestcity.com"
                else -> "unknown"
            }

            Picasso.get().load(item.thumbnail).into(thumbnailImageView)

            itemView.setOnClickListener {
                (thumbnailImageView.context as MainActivity).showArticle(item.title, item.thumbnail, item.date, item.link, convertText)
            }
        }

        override fun onClick(v: View?) {
        }

        private fun replaceSpecialSymbols(content: String) : String {
            var convertedContent = ""
            if(content.contains("&") && content.contains(";")) {
                convertedContent = content.replace("&laquo;", "\"")
                convertedContent = convertedContent.replace("&raquo;", "\"")
                convertedContent = convertedContent.replace("&nbsp;", " ")
                convertedContent = convertedContent.replace("&ndash;", "-")
                convertedContent = convertedContent.replace("&mdash;", "-")
                convertedContent = convertedContent.replace("&hellip;", "...")
                convertedContent = convertedContent.replace("&bdquo;", "\"")
                convertedContent = convertedContent.replace("&ldquo;", "\"")
                convertedContent = convertedContent.replace("&minus;", "-")
                convertedContent = convertedContent.replace("&rsquo;", "\"")
                convertedContent = convertedContent.replace("&rdquo;", "\"")
                convertedContent = convertedContent.replace("\\", "")

                return convertedContent
            }
            return content
        }
    }
}