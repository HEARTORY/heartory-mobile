package com.heartsteel.heartory.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.domain.Article
import com.heartsteel.heartory.service.model.domain.ArticleList

class OuterAdapter(
    private val outerItems: MutableList<ArticleList>,
    private val onInnerItemClick: (Article) -> Unit
) : RecyclerView.Adapter<ListArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blogs, parent, false)
        return ListArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListArticleViewHolder, position: Int) {
        val outerItem = outerItems[position]
        holder.outerItemTitle.text = outerItem.blogType

        // Set up the inner RecyclerView
        holder.innerRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.innerRecyclerView.adapter = InnerAdapter(outerItem.blogs, onInnerItemClick)
    }

    override fun getItemCount(): Int {
        return outerItems.size
    }
}

data class OuterItem(val title: String, val innerItems: List<InnerItem>)
data class InnerItem(val title: String, val imageUrl: String)
