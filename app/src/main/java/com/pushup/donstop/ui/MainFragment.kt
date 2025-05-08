package com.pushup.donstop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.pushup.donstop.MainActivity
import com.pushup.donstop.R

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val startButton: Button = view.findViewById(R.id.startButton)

        // Обработчик нажатия на кнопку
        startButton.setOnClickListener {
            (activity as? MainActivity)?.openWorkoutFragment()  // Переход через MainActivity
        }

        return view
    }
}
