package com.heartsteel.heartory.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityItemBinding

class ExerciseTodayActivityAdapter(private val items: List<Exercise>) : RecyclerView.Adapter<ExerciseTodayActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseTodayActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvClassName.text = exercise.name
            binding.tvTime.text = exercise.time
            Glide.with(binding.root.context)
                .load(exercise.imageUrl)
                .into(binding.ivClassLogo) // Assuming ivClassLogo is the id of your ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseTodayActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}