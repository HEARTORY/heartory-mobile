package com.heartsteel.heartory.ui.article

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.R

class ListArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val outerItemTitle: TextView = view.findViewById(R.id.txt_subTitle)
    val innerRecyclerView: RecyclerView  = view.findViewById(R.id.recyclerView)
}
