package com.heartsteel.heartory.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityDetailItemBinding

class ExerciseTodayDetailAdapter(private val items: List<Exercise>) : RecyclerView.Adapter<ExerciseTodayDetailAdapter.ViewHolder>() {

    class ViewHolder(val binding:FragmentExerciseTodayActivityDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvClassName.text = exercise.name
            binding.tvTime.text = exercise.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseTodayActivityDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)  // Use the bind method to set the data
    }

    override fun getItemCount(): Int = items.size
}