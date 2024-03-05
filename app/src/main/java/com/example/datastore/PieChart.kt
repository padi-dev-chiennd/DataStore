package com.example.datastore

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datastore.databinding.ActivityPieChartBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class PieChart : AppCompatActivity() {
    private lateinit var binding : ActivityPieChartBinding
    val COLORFUL_COLORS = intArrayOf(
        Color.rgb(0, 113, 255),
        Color.rgb(0, 174, 255),
        Color.rgb(0, 216, 255),
        Color.rgb(0, 243, 255),
//        Color.rgb(106, 150, 31),
//        Color.rgb(179, 100, 53)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_pie_chart)
        setDataPieChart()
    }

    private fun setDataPieChart() {
        val entries = listOf(
            PieEntry(1f,"Category 1"),
            PieEntry(2f,"Category 2"),
            PieEntry(3f,"Category 3"),
            PieEntry(4f,"Category 4")

        )
        val dataSet = PieDataSet(entries,"")
        dataSet.setDrawValues(false)
//        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        dataSet.colors = COLORFUL_COLORS.toList()
        val pieData = PieData(dataSet)
        binding.customPieChart.description.text = ""
//        dataSet.valueLinePart1OffsetPercentage = 80f // Adjust as needed
//        dataSet.valueLinePart2Length = 0.4f
//
//        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        binding.customPieChart.data = pieData
        binding.customPieChart.invalidate()
    }

}