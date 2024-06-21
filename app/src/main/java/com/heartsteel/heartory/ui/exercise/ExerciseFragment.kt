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
            Exercise(category = "Yoga", instructorName = "John Doe", time = "30 minutes", imageUrl = "https://media1.popsugar-assets.com/files/thumbor/vuQmhXf-dfGzBK2liB8_4f6kuF8/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2014/04/04/724/n/1922729/b95be44e0421cbd2_Plank-Knee-In/i/Knee-Up-Plank.jpg"),
            Exercise(category= "Warm Up", instructorName = "Jane Doe", time = "15 minutes", imageUrl = "https://cdn-ejllp.nitrocdn.com/iZVFuSGGfTRQLjNAjhrfeBqwaewiFFNh/assets/images/optimized/rev-6cd54fe/www.vingo.fit/wp-content/uploads/2022/12/Large-arm-circles.jpg"),
            Exercise(category = "Push Up", instructorName = "John Smith", time = "20 minutes", imageUrl = "https://www.realsimple.com/thmb/rEmEAm4vfx67IRbFgoVA0RzhTgI=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/health-benefits-of-pushups-GettyImages-498315681-7008d40842444270868c88b516496884.jpg"),
            Exercise(category = "Shoulder", instructorName = "Jane Smith", time = "25 minutes", imageUrl = "https://ik.imagekit.io/02fmeo4exvw/September_2014/Shoulders2_main.jpg")
        )


        val adapter = ExerciseCategoryAdapter(exerciseList) { exercise ->
            val bundle = Bundle().apply {
                putString("category", exercise.category)
//                putString("instructorName", exercise.instructorName)
//                putString("imageUrl", exercise.imageUrl)
            }
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseActivityListFragment, bundle)
        }


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