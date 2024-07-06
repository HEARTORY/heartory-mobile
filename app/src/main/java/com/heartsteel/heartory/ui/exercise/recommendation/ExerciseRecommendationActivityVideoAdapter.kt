package com.heartsteel.heartory.ui.exercise.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.databinding.FragmentExerciseRecommendationVideoIteamBinding
import com.heartsteel.heartory.service.model.domain.Lesson
import com.heartsteel.heartory.R

class ExerciseRecommendationActivityVideoAdapter(
    private val items: List<Lesson>,
    private val listener: (Lesson) -> Unit
) : RecyclerView.Adapter<ExerciseRecommendationActivityVideoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FragmentExerciseRecommendationVideoIteamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson, listener: (Lesson) -> Unit) {
            binding.tvClassName.text = lesson.lessonName
            binding.tvTime.text = lesson.lengthSeconds.toString()


            Glide.with(binding.root.context)
                .load(lesson.thumbUrl)
                .placeholder(R.drawable.placeholder_image) // Set a placeholder image
                .error(R.drawable.error_image) // Set an error image
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                listener(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseRecommendationVideoIteamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size
}