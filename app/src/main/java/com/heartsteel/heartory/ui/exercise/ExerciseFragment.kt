package com.heartsteel.heartory.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseBinding


class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        seeAllClickListener()
        todayAvtivityClickListener()

    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise(name = "Yoga", imageUrl = "https://media1.popsugar-assets.com/files/thumbor/vuQmhXf-dfGzBK2liB8_4f6kuF8/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2014/04/04/724/n/1922729/b95be44e0421cbd2_Plank-Knee-In/i/Knee-Up-Plank.jpg"),
            Exercise(name = "Warm Up", imageUrl = "https://hips.hearstapps.com/hmg-prod/images/2021-runnersworld-weekendworkouts-ep41-situps-jc-v03-index-1633617537.jpg?crop=0.9893230083304001xw:1xh;center,top&resize=1200:*"),
            Exercise(name = "push up", imageUrl = "https://www.realsimple.com/thmb/rEmEAm4vfx67IRbFgoVA0RzhTgI=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/health-benefits-of-pushups-GettyImages-498315681-7008d40842444270868c88b516496884.jpg"),
            Exercise(name = "Shoulder", imageUrl = "https://ik.imagekit.io/02fmeo4exvw/September_2014/Shoulders2_main.jpg")
        )
        val adapter = ExerciseCategoryAdapter(exerciseList)

        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }
    private fun seeAllClickListener() {
        binding.tvSeeAllCategories.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseActivityListFragment)

        }
    }
    private fun todayAvtivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseTodayActivityListFragment)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun setupView() {

    }

    private fun setupEvent() {

    }


}