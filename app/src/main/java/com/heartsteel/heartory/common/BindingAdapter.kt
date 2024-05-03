package com.heartsteel.heartory.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String)
{
    Glide.with(view.context).load(url).into(view)
}

@BindingAdapter("imageResource")
fun setImageResource(view: ImageView, resource: Int)
{
    view.setImageResource(resource)
}



