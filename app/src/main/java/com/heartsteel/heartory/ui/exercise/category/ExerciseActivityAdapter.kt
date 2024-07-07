package com.heartsteel.heartory.ui.exercise.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.databinding.FragmentExerciseActivityItemBinding
import com.heartsteel.heartory.service.model.domain.Lesson

class ExerciseActivityAdapter(
    private val items: List<Lesson>,
    private val listener: (Lesson) -> Unit
) : RecyclerView.Adapter<ExerciseActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseActivityItemBinding, val listener: (Lesson) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.tvClassName.text = lesson.lessonName
            binding.tvInstructorName.text = lesson.lengthSeconds.toString()
            Glide.with(binding.root.context)
                .load(lesson.thumbUrl)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                listener(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}