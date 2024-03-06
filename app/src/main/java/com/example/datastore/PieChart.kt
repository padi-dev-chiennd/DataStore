package com.example.datastore

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datastore.databinding.ActivityPieChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
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
    val entries = listOf(
        PieEntry(1f,"Category 1"),
        PieEntry(2f,"Category 2"),
        PieEntry(3f,"Category 3"),
        PieEntry(4f,"Category 4"),
        PieEntry(4f,"Category 4"),
        PieEntry(4f,"Category 4"),
        PieEntry(4f,"Category 4"),
        PieEntry(4f,"Category 4"),
        PieEntry(4f,"Category 4"),

    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_pie_chart)
        setDataPieChart()
    }

    private fun setDataPieChart() {
        binding.customPieChart.description.text = ""
        binding.customPieChart.setTouchEnabled(false)
        binding.customPieChart.setDrawEntryLabels(false)
        val dataSet = PieDataSet(entries,"")
//        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.colors = COLORFUL_COLORS.toList()
        dataSet.setDrawValues(false)

        val pieData = PieData(dataSet)
        val legend = binding.customPieChart.legend
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

        pieData.setValueFormatter(MyCustomValueFormatter())
//        val legendEntries = entries.mapIndexed { index, pieEntry ->
//            LegendEntry().apply {
//                label = "${pieEntry.label}: ${pieEntry.value}"
//                // Adjust other properties as needed
//            }
//        }
//
//        // Set custom LegendEntry objects to the Legend
//        binding.customPieChart.legend.setCustom(legendEntries.toTypedArray())
        val label = entries.map { "${it.label} ${it.value.toInt()}" }

        binding.customPieChart.data = pieData
        binding.customPieChart.invalidate()
    }

}