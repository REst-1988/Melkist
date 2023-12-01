package com.example.melkist.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.melkist.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class SparklineStyle (private val context: Context){

    fun styleChart (lineChart: LineChart) = lineChart.apply {
        axisRight.apply {
            isEnabled = false
            //axisRight.setDrawGridLines(false)
        }
        axisLeft.apply {
            isEnabled = true
            //axisMaximum = 0f
            //axisMaximum = 10f
            setDrawGridLines(false)
        }
        xAxis.apply {
            //axisMaximum = 0f
            //axisMaximum = 24f
            //isGranularityEnabled = true
            //granularity = 4f
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            setAvoidFirstLastClipping(false)
            setDrawAxisLine(false)
            labelRotationAngle = -45f
            setCenterAxisLabels(true)
/*            textColor = if (isSystemThemeInDarkMode(context))
                Color.rgb(203, 172, 123)
            else
                Color.rgb(13, 23, 64)*/
        }
        setTouchEnabled(true)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
    }

    fun styleLineDataSet (lineDataSet: LineDataSet) = lineDataSet.apply {
        color = ContextCompat.getColor(context, R.color.theme_base_color)
        valueTextColor = ContextCompat.getColor(context, R.color.yellow)
        setDrawValues(false)
        lineWidth = 3f
        isHighlightEnabled = true
        setDrawHighlightIndicators(false)
        setDrawCircles(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
        setDrawFilled(true)
        fillDrawable = ContextCompat.getDrawable(context, R.drawable.gradient_chart_color)
    }
}