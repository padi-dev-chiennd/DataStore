package com.example.datastore

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class IntegerFormatter(private val yValues: FloatArray) : ValueFormatter() {
    private val format = DecimalFormat("###") // Format values as integers

    override fun getFormattedValue(value: Float): String {
        return yValues[value.toInt()].toString()
    }
}