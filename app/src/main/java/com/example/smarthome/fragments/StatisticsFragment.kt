package com.example.smarthome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.smarthome.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.data.Entry

class StatisticsFragment : Fragment() {

    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart
    private lateinit var radarChart: RadarChart
    private lateinit var bubbleChart: BubbleChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Initialize charts
        lineChart = view.findViewById(R.id.lineChart)
        pieChart = view.findViewById(R.id.pieChart)
        radarChart = view.findViewById(R.id.radarChart)
        bubbleChart = view.findViewById(R.id.bubbleChart)

        // Call functions to set up each chart
        setupLineChart()
        setupPieChart()
        setupRadarChart()
        setupBubbleChart()

        return view
    }

    // Line Chart with Gradient Fill
    private fun setupLineChart() {
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 1f))
        entries.add(Entry(1f, 3f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 5f))

        val dataSet = LineDataSet(entries, "Line Chart")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.blue_main)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()  // Refresh the chart
    }

    // Pie Chart
    private fun setupPieChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(20f, "Atharv"))
        entries.add(PieEntry(20f, "Vishesh"))
        entries.add(PieEntry(20f, "Juhi"))
        entries.add(PieEntry(40f, "Fights"))

        val dataSet = PieDataSet(entries, "Till Date Contribution")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()  // Refresh the chart
    }

    // Radar Chart (Spider Web Chart)
    private fun setupRadarChart() {
        val entries = ArrayList<RadarEntry>()
        entries.add(RadarEntry(5f))
        entries.add(RadarEntry(6f))
        entries.add(RadarEntry(3f))
        entries.add(RadarEntry(9f))
        entries.add(RadarEntry(1f))

        val dataSet = RadarDataSet(entries, "Radar Chart")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.blue_main)
        dataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.blue_main)
        dataSet.setDrawFilled(true)

        val radarData = RadarData(dataSet)
        radarChart.data = radarData
        radarChart.invalidate()  // Refresh the chart
    }

    // Bubble Chart
    private fun setupBubbleChart() {
        val entries = ArrayList<BubbleEntry>()
        entries.add(BubbleEntry(0f, 5f, 2f))  // (x, y, size)
        entries.add(BubbleEntry(1f, 3f, 3f))
        entries.add(BubbleEntry(2f, 8f, 1f))
        entries.add(BubbleEntry(3f, 7f, 4f))

        val dataSet = BubbleDataSet(entries, "Bubble Chart")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.blue_main)
        dataSet.setDrawValues(true)

        val bubbleData = BubbleData(dataSet)
        bubbleChart.data = bubbleData
        bubbleChart.invalidate()  // Refresh the chart
    }
}

