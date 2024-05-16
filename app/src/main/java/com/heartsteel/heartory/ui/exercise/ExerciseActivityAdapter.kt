package com.heartsteel.heartory.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseActivityItemBinding

class ExerciseActivityAdapter(private val items: List<Exercise>) : RecyclerView.Adapter<ExerciseActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvClassName.text = exercise.name
            binding.tvInstructorName.text = exercise.instructorName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)  // Use the bind method to set the data
    }

    override fun getItemCount(): Int = items.size
}