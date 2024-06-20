package com.heartsteel.heartory.ui.article

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.R

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.imageView)
    val textView: TextView = view.findViewById(R.id.textView)
}
