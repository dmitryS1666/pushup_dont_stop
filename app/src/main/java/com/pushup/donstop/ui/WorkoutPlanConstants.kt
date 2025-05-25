package com.pushup.donstop.ui

object WorkoutPlanConstants {
    val setsMap = mutableMapOf(
        "Beginner" to listOf(4, 3, 3, 2, 2),
        "Medium" to listOf(8, 6, 6, 4, 4),
        "Advanced" to listOf(12, 10, 8, 8, 6)
    )

    val restTimeMap = mutableMapOf(
        "Beginner" to "01:00",
        "Medium" to "00:45",
        "Advanced" to "00:30"
    )
}