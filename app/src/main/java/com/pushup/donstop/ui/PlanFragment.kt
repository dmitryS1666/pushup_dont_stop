package com.pushup.donstop.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.pushup.donstop.R

class PlanFragment : Fragment() {

    private lateinit var levelGroup: RadioGroup
    private lateinit var beginnerButton: RadioButton
    private lateinit var mediumButton: RadioButton
    private lateinit var advancedButton: RadioButton
    private lateinit var tableLayout: TableLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        levelGroup = view.findViewById(R.id.levelGroup)
        beginnerButton = view.findViewById(R.id.beginner)
        mediumButton = view.findViewById(R.id.medium)
        advancedButton = view.findViewById(R.id.advanced)
        tableLayout = view.findViewById(R.id.planTable)

        // Получаем переданный уровень
        val selectedLevel = arguments?.getString("level") ?: "Beginner"

        when (selectedLevel) {
            "Beginner" -> beginnerButton.isChecked = true
            "Medium" -> mediumButton.isChecked = true
            "Advanced" -> advancedButton.isChecked = true
        }

        showPlan(selectedLevel)

        levelGroup.setOnCheckedChangeListener { _, checkedId ->
            val level = when (checkedId) {
                R.id.beginner -> "Beginner"
                R.id.medium -> "Medium"
                R.id.advanced -> "Advanced"
                else -> "Beginner"
            }
            showPlan(level)
        }

        return view
    }

    private fun showPlan(level: String) {
        tableLayout.removeAllViews()

        val rows = when (level) {
            "Beginner" -> listOf("10 push-ups", "Rest 60s", "3 sets")
            "Medium" -> listOf("20 push-ups", "Rest 45s", "4 sets")
            "Advanced" -> listOf("30 push-ups", "Rest 30s", "5 sets")
            else -> listOf()
        }

        for (item in rows) {
            val row = TableRow(requireContext())
            val text = TextView(requireContext())
            text.text = item
            text.setPadding(16, 16, 16, 16)
            row.addView(text)
            tableLayout.addView(row)
        }
    }
}
