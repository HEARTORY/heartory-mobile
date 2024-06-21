package com.heartsteel.heartory.ui.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseActivityItemBinding

class ExerciseActivityAdapter(
    private val items: List<Exercise>,
    private val onItemClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseActivityAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FragmentExerciseActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise, onItemClick: (Exercise) -> Unit) {
            Log.d("ExerciseActivityAdapter", "Binding exercise: ${exercise.name}, instructor: ${exercise.instructorName}, imageUrl: ${exercise.imageUrl}")
            binding.tvClassName.text = exercise.name
            binding.tvInstructorName.text = exercise.instructorName
            Glide.with(binding.root.context)
                .load(exercise.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                onItemClick(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size
}