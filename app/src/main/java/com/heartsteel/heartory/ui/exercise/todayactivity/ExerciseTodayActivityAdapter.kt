package com.heartsteel.heartory.ui.exercise.todayactivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityItemBinding
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.domain.Lesson


class ExerciseTodayActivityAdapter(
    private var items: List<Lesson>,
    private val listener: (Lesson) -> Unit
) : RecyclerView.Adapter<ExerciseTodayActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseTodayActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson, onItemClick: (Lesson) -> Unit) {
            binding.tvClassName.text = lesson.lessonName
            Glide.with(binding.root.context)
                .load(lesson.thumbUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                onItemClick(lesson)
            }
            Log.d("Adapter", "Binding lesson: ${lesson.lessonName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseTodayActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder called for position: $position with item: ${items[position]}")
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    fun updateLessons(newItems: List<Lesson>) {
        items = newItems
        notifyDataSetChanged()
        Log.d("Adapter", "Lessons updated: ${newItems.size}")
    }
}