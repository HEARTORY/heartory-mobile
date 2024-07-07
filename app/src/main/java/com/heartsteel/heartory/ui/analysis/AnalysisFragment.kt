package com.heartsteel.heartory.ui.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.healthcarecomp.base.BaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentAnalysisBinding
import com.heartsteel.heartory.service.model.response.HBRecordRes
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class AnalysisFragment : BaseFragment(R.layout.fragment_analysis) {

    private lateinit var _binding: FragmentAnalysisBinding
    private val viewModel: AnalysisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(layoutInflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupEvent()
        setupObserver()
    }

    private fun setupEvent() {

    }

    private fun setupView() {


    }

    override fun onResume() {
        super.onResume()
        viewModel.getHBRecords()
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
        leftAxis.axisMinimum = 30f
        leftAxis.axisMaximum = 160f
        leftAxis.granularity = 10f
        leftAxis.setDrawGridLines(false)
        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false

        // Refreshing the chart
        chart.invalidate()

        _binding.tvMax.text = hBRecords.maxByOrNull { it.hr }?.hr.toString()
        _binding.tvMin.text = hBRecords.minByOrNull { it.hr }?.hr.toString()
        _binding.tvAverage.text = hBRecords.map { it.hr }.average().toInt().toString()
    }


}