package com.heartsteel.heartory.ui.article

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.domain.Article

class InnerAdapter(
    private val innerItems: List<Article>,
    private val onItemClick: (Article) -> Unit
    ) : RecyclerView.Adapter<ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_element, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val innerItem = innerItems[position]
        holder.textView.text = innerItem.title

        // Load image using Glide or any other image loading library
        Glide.with(holder.imageView.context)
            .load(innerItem.imageUrl)
            .placeholder(R.drawable.mia_happy)
            .error(R.drawable.mia_cry)
            .into(holder.imageView)


        // Set the click listener on the image view
        holder.imageView.setOnClickListener {
            onItemClick(innerItem)
        }
    }

    override fun getItemCount(): Int {
        return innerItems.size
    }
}
