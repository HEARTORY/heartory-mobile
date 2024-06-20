package com.heartsteel.heartory.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R

class InnerAdapter(private val innerItems: List<InnerItem>) : RecyclerView.Adapter<ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_element, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val innerItem = innerItems[position]
        holder.textView.text = innerItem.title

        // Load image using Glide or any other image loading library
        Glide.with(holder.itemView.context)
            .load(innerItem.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return innerItems.size
    }
}
