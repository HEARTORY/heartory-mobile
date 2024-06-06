package com.heartsteel.heartory.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityDetailItemBinding

class ExerciseTodayDetailAdapter(
    private val items: List<Exercise>,
    private val listener: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseTodayDetailAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentExerciseTodayActivityDetailItemBinding, val listener: (Exercise) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvClassName.text = exercise.name
            binding.tvTime.text = exercise.time
            Glide.with(binding.root.context)
                .load(exercise.imageUrl)
                .into(binding.ivClassLogo)

            binding.root.setOnClickListener {
                listener(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseTodayActivityDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) { exercise ->
            listener(exercise)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}