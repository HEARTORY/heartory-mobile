package com.heartsteel.heartory.ui.exercise.base

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.heartsteel.heartory.R

import com.heartsteel.heartory.databinding.FragmentExerciseItemCategoryCardBinding
import com.heartsteel.heartory.databinding.FragmentExerciseItemEnrollCardBinding
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.service.model.response.ExerciseMyResponseDTO
import com.heartsteel.heartory.service.model.response.ExerciseResponseDTO

class ExerciseEnrollAdapter(
    private var items: List<ExerciseMyResponseDTO>,
    private val onItemClick: (ExerciseMyResponseDTO) -> Unit
) : RecyclerView.Adapter<ExerciseEnrollAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FragmentExerciseItemEnrollCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exerciseMyResponseDTO: ExerciseMyResponseDTO, onItemClick: (ExerciseMyResponseDTO) -> Unit) {
            val exercise = exerciseMyResponseDTO.exercise
            binding.tvCategoryName.text = exercise.title
            Glide.with(binding.root.context)
                .load(exercise.thumbUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivCategoryImage)

            binding.root.setOnClickListener {
                onItemClick(exerciseMyResponseDTO)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentExerciseItemEnrollCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateExercises(newItems: List<ExerciseMyResponseDTO>) {
        items = newItems
        notifyDataSetChanged()
    }
}