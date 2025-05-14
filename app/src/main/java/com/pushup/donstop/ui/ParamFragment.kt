package com.pushup.donstop.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pushup.donstop.MainActivity
import com.pushup.donstop.R

class ParamFragment : Fragment() {

    private lateinit var ageInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var levelGroup: RadioGroup
    private lateinit var calculateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_param, container, false)

        ageInput = view.findViewById(R.id.ageInput)
        weightInput = view.findViewById(R.id.weightInput)
        levelGroup = view.findViewById(R.id.levelGroup)
        calculateButton = view.findViewById(R.id.calculateButton)

        val prefs = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        ageInput.setText(prefs.getInt("age", 0).toString())
        weightInput.setText(prefs.getFloat("weight", 0f).toString())

        // Восстановление уровня
        val savedLevel = prefs.getString("level", "Beginner") ?: "Beginner"
        when (savedLevel) {
            "Beginner" -> levelGroup.check(R.id.beginner)
            "Medium" -> levelGroup.check(R.id.medium)
            "Advanced" -> levelGroup.check(R.id.advanced)
        }

        // Слушатель изменения уровня
        levelGroup.setOnCheckedChangeListener { _, checkedId ->
            val level = when (checkedId) {
                R.id.beginner -> "Beginner"
                R.id.medium -> "Medium"
                R.id.advanced -> "Advanced"
                else -> "Beginner" // По умолчанию
            }

            // Сохранение выбранного уровня
            prefs.edit().apply {
                putString("level", level)
                apply()
            }
        }

        calculateButton.setOnClickListener {
            val age = ageInput.text.toString().toIntOrNull()
            val weight = weightInput.text.toString().toFloatOrNull()
            val levelId = levelGroup.checkedRadioButtonId
            val level = view.findViewById<RadioButton>(levelId).text.toString()

            if (age != null && weight != null) {
                prefs.edit().apply {
                    putInt("age", age)
                    putFloat("weight", weight)
                    putString("level", level)
                    apply()
                }

                val planFragment = PlanFragment()
                planFragment.arguments = Bundle().apply {
                    putString("level", level)
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, planFragment)
                    .addToBackStack(null)
                    .commit()

                (requireActivity() as? MainActivity)?.showBottomNav()
            } else {
                Toast.makeText(requireContext(), "Invalid age or weight", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
