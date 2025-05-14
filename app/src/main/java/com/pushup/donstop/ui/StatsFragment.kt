package com.pushup.donstop.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pushup.donstop.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StatsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var percentText: TextView
    private lateinit var dateRange: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        barChart = view.findViewById(R.id.barChart)
        percentText = view.findViewById(R.id.percentText)
        dateRange = view.findViewById(R.id.dateRange)

        showChart()

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun showChart() {
        val trainingDates = getTrainingDates() // Заглушка — получи реальные даты
        if (trainingDates.isEmpty()) return

        // Находим минимальную дату и вычитаем 2 дня
        val startDate = Calendar.getInstance().apply {
            time = trainingDates.minOrNull() ?: Date()
            add(Calendar.DAY_OF_YEAR, -2)
        }

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        var completedCount = 0
        var totalDays = 0

        val calendar = startDate.clone() as Calendar

        // Проверяем, если нет данных за последние 2 дня, начинаем с сегодняшнего
        if (trainingDates.isEmpty()) {
            calendar.time = today.time
        }

        while (!calendar.after(today)) {
            val date = calendar.time
            val dateLabel = dateFormat.format(date)
            labels.add(dateLabel)

            val isDone = trainingDates.any { isSameDay(it, date) } // замените на реальный статус
            val value = if (isDone) 1f else 0f
            if (value == 1f) completedCount++

            entries.add(BarEntry(totalDays.toFloat(), value))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            totalDays++
        }

        val dataSet = BarDataSet(entries, "Done")
        dataSet.color = Color.YELLOW
        dataSet.valueTextColor = Color.TRANSPARENT // Убираем текстовые значения сверху столбцов
        dataSet.valueTextSize = 0f // Убираем размер текста

        val data = BarData(dataSet)
        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.WHITE

        // Настройки для оси Y
        val leftAxis = barChart.axisLeft
        leftAxis.setDrawLabels(false) // Отключаем отображение меток оси Y
        leftAxis.setDrawGridLines(true) // Оставляем линии сетки
        leftAxis.enableGridDashedLine(10f, 10f, 0f) // Пунктирная линия для сетки

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setTouchEnabled(false)
        barChart.invalidate()

        val percent = (completedCount * 100) / totalDays
        percentText.text = "$percent%"

        val todayFormatted = SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date())
        dateRange.text = "Today - $todayFormatted"
    }

    private fun getTrainingDates(): List<Date> {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return listOf(
            format.parse("2025-05-10"),
            format.parse("2025-05-12"),
            format.parse("2025-05-13")
        ).filterNotNull()
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val fmt = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return fmt.format(date1) == fmt.format(date2)
    }
}
