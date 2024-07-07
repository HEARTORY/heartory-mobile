package com.heartsteel.heartory.ui.home

import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.healthcarecomp.base.BaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentHomeBinding
import com.heartsteel.heartory.service.model.response.HBRecordRes
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.getstream.avatarview.coil.loadImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var _binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        setupObserver()
        return _binding.root
    }

    private fun setupObserver() {
        viewModel.hBRecordsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    setupChartView(it.data?.toList() ?: emptyList())
                }

                is Resource.Error -> {
                    Toasty.error(requireContext(), it.message.toString(), Toasty.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupView() {
        _binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)
        _binding.tvDate.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.ENGLISH))

        val imageList = listOf(
            SlideModel(imagePath = R.drawable.banner_1, scaleType = ScaleTypes.CENTER_CROP),
            SlideModel(imagePath = R.drawable.banner_2, scaleType = ScaleTypes.CENTER_CROP),
        )
        _binding.imageSlider.setImageList(imageList)
        _binding.btnChatNow.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_chatFragment)
        }
    }

    private fun setupEvent() {
        _binding.cvChat.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_chatFragment)
        }
        _binding.cvAnalysis.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_analysisFragment)
        }
        _binding.cvExercise.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_exerciseFragment)
        }
        _binding.cvArticle.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_articleFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getHBRecords()
    }

    private fun setupChartView(hBRecords: List<HBRecordRes>) {
        val chart: LineChart = _binding.chart1
        val entries = mutableListOf<Entry>()

        hBRecords.forEachIndexed { index, hbRecord ->
            entries.add(Entry(index.toFloat(), hbRecord.hr.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Heart Rate").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(getColor(R.color.secondary_color))
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
        leftAxis.axisMinimum = 60f
        leftAxis.axisMaximum = 160f
        leftAxis.granularity = 10f
        leftAxis.setDrawGridLines(false)
        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false

        // Refreshing the chart
        chart.invalidate()
    }

}