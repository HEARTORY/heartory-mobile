package com.heartsteel.heartory.ui.exercise

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseItemCategoryCardBinding

class ExerciseCategoryAdapter(
    private val items: List<Exercise>,
    private val onItemClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseCategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseItemCategoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise, onItemClick: (Exercise) -> Unit) {
            binding.tvCategoryName.text = exercise.category
            Log.d("ExerciseCategoryAdapter", "Loading image URL: ${exercise.imageUrl}")

            Glide.with(binding.root.context)
                .load(exercise.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("GlideError", "Failed to load image: ${exercise.imageUrl}", e)
                        return false // important to return false so the error placeholder can be placed
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("GlideSuccess", "Successfully loaded image: ${exercise.imageUrl}")
                        return false
                    }
                })
                .into(binding.ivCategoryImage)

            binding.root.setOnClickListener {
                onItemClick(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseItemCategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount() = items.size
}