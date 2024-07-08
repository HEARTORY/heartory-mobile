package com.heartsteel.heartory.ui.exercise.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentExerciseRecommendationActivityItemBinding
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.service.model.domain.Lesson

class ExerciseRecommendationActivityAdapter(
    private val items: List<Lesson>,
    private val listener: (Lesson) -> Unit
) : RecyclerView.Adapter<ExerciseRecommendationActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseRecommendationActivityItemBinding, val listener: (Lesson) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.tvClassName.text = lesson.lessonName
            val minutes = lesson.lengthSeconds / 60
            val seconds = lesson.lengthSeconds % 60
            val formattedDuration = String.format("%02d:%02d", minutes, seconds)
            binding.tvInstructorName.text = formattedDuration
            Glide.with(binding.root.context)
                .load(lesson.thumbUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                listener(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseRecommendationActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
