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
import com.heartsteel.heartory.databinding.FragmentExerciseActivityListBinding
class ExerciseActivityFragment : Fragment() {

    private var _binding: FragmentExerciseActivityListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        todayActivityClickListener()
        backClickListener()
    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise(name = "Knee push up", instructorName = "John Doe", imageUrl = "https://media1.popsugar-assets.com/files/thumbor/vuQmhXf-dfGzBK2liB8_4f6kuF8/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2014/04/04/724/n/1922729/b95be44e0421cbd2_Plank-Knee-In/i/Knee-Up-Plank.jpg"),
            Exercise(name = "Push up", instructorName = "Jane Doe", imageUrl = "https://www.realsimple.com/thmb/rEmEAm4vfx67IRbFgoVA0RzhTgI=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/health-benefits-of-pushups-GettyImages-498315681-7008d40842444270868c88b516496884.jpg"),
            Exercise(name = "Sit up", instructorName = "Jane Doe", imageUrl = "https://hips.hearstapps.com/hmg-prod/images/2021-runnersworld-weekendworkouts-ep41-situps-jc-v03-index-1633617537.jpg?crop=0.9893230083304001xw:1xh;center,top&resize=1200:*")
        )
        val adapter = ExerciseActivityAdapter(exerciseList)

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }
    private fun todayActivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseTodayActivityListFragment)

        }
    }
    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseFragment)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}