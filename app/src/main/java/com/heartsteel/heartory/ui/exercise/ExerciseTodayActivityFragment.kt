package com.heartsteel.heartory.ui.exercise

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityListBinding

class ExerciseTodayActivityFragment : Fragment() {

    private var _binding: FragmentExerciseTodayActivityListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseTodayActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        todayDetailClickListener()
        setupChartView()
        backClickListener()
    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise(name = "Knee push up", time = "Today, 03:00pm"),
            Exercise(name = "Push up", time = "Today, 03:00pm"),
            Exercise(name = "Sit up", time = "Today, 03:00pm")
        )
        val adapter = ExerciseTodayActivityAdapter(exerciseList)

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun todayDetailClickListener() {
        binding.buttonCheck.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTodayActivityListFragment_to_exerciseTodayDetailExerciseListFragment)
        }
    }
    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTodayActivityListFragment_to_exerciseFragment)

        }
    }

    private fun setupChartView() {
        val chart: LineChart = binding.lineChart
        val entries = mutableListOf<Entry>()

        // Example data for a month
        val daysInMonth = 30
        for (i in 1..daysInMonth) {
            val percentage = (0..100).random().toFloat()
            entries.add(Entry(i.toFloat(), percentage))
        }

        val dataSet = LineDataSet(entries, "Workout Completion").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.BLUE)
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        chart.data = lineData

        // Customizing X-axis
        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        // Customizing Y-axis
        val leftAxis: YAxis = chart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100f
        leftAxis.granularity = 10f
        leftAxis.setDrawGridLines(false)
        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false

        // Refreshing the chart
        chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}