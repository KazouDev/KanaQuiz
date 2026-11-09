package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import fr.enssat.kanaquiz.elevache.stats.MAX_GRADE

const val PROGRESS_CHART_DESCRIPTION = "Graphique de progression"

@Composable
fun ProgressChart(grades: List<Double>, modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val chart = LineChart(context)
            chart.contentDescription = PROGRESS_CHART_DESCRIPTION
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.axisRight.isEnabled = false
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.axisMaximum = MAX_GRADE.toFloat()
            chart.axisLeft.setLabelCount(MAX_GRADE + 1, true)
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.xAxis.granularity = 1f
            chart.xAxis.setDrawGridLines(false)
            chart.setTouchEnabled(false)
            chart
        },
        update = { chart ->
            val entries = mutableListOf<Entry>()
            for ((i, grade) in grades.withIndex()) {
                entries.add(Entry((i + 1).toFloat(), grade.toFloat()))
            }

            val dataSet = LineDataSet(entries, "Grade")
            dataSet.color = lineColor
            dataSet.setCircleColor(lineColor)
            dataSet.lineWidth = 3f
            dataSet.circleRadius = 4f
            dataSet.setDrawValues(false)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataSet.setDrawFilled(true)
            dataSet.fillColor = lineColor
            dataSet.fillAlpha = 50

            chart.xAxis.textColor = textColor
            chart.axisLeft.textColor = textColor
            chart.data = LineData(dataSet)
            chart.animateX(800)
        }
    )
}
