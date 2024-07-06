package com.heartsteel.heartory.ui.exercise.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.databinding.FragmentExerciseRecommendationActivityItemBinding
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.service.model.domain.Lesson

class ExerciseRecommendationActivityAdapter(
    private val items: List<Exercise>,
    private val listener: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseRecommendationActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseRecommendationActivityItemBinding, val listener: (Exercise) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvClassName.text = exercise.title
            binding.tvInstructorName.text = exercise.type
            Glide.with(binding.root.context)
                .load(exercise.thumbUrl)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                listener(exercise)
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