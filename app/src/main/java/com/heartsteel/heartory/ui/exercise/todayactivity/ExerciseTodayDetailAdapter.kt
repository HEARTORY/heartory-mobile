package com.heartsteel.heartory.ui.exercise.todayactivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityDetailItemBinding
import com.heartsteel.heartory.service.model.domain.Lesson
import com.heartsteel.heartory.service.model.domain.Exercise

class ExerciseTodayDetailAdapter(
    private var lessons: List<Lesson>,
    private val listener: (Lesson) -> Unit
) : RecyclerView.Adapter<ExerciseTodayDetailAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseTodayActivityDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson, onItemClick: (Lesson) -> Unit) {
            binding.tvClassName.text = lesson.lessonName
            binding.tvTime.text = "${lesson.lengthSeconds / 60}:${lesson.lengthSeconds % 60}"

            Glide.with(binding.root.context)
                .load(lesson.thumbUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                onItemClick(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseTodayActivityDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lessons[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = lessons.size

    fun updateLessons(newLessons: List<Lesson>) {
        lessons = newLessons
        notifyDataSetChanged()
    }
}