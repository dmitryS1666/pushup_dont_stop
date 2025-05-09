package com.pushup.donstop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.pushup.donstop.MainActivity
import com.pushup.donstop.R

class WorkoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)

//        val navParams: LinearLayout = view.findViewById(R.id.navParams)
//        val navPlan: LinearLayout = view.findViewById(R.id.navPlan)
//        val navRun: LinearLayout = view.findViewById(R.id.navRun)
//        val navStat: LinearLayout = view.findViewById(R.id.navStat)
//        val navSet: LinearLayout = view.findViewById(R.id.navSet)
//
//        navParams.setOnClickListener {
//            System.out.println("navParams")
//        }
//
//        navPlan.setOnClickListener {
//            System.out.println("navPlan")
//        }
//
//        navRun.setOnClickListener {
//            System.out.println("navRun")
//        }
//
//        navStat.setOnClickListener {
//            System.out.println("navStat")
//        }
//
//        navSet.setOnClickListener {
//            System.out.println("navSetButton")
//            (activity as? MainActivity)?.openSettingsFragment()
//        }

        return view
    }
}
