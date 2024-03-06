package com.example.datastore

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

class MyCustomValueFormatter :ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value == 0f) {
            ""
        } else {
            value.roundToInt().toString() + " %"
        }
    }
}