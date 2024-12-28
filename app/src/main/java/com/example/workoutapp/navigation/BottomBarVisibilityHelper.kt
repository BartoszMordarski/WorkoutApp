package com.example.workoutapp.navigation
import androidx.compose.runtime.Composable


@Composable
fun shouldShowBottomBar(route: String?): Boolean {
    return when (route) {
        "home", "add_workout", "exercises", "history", "workout_history", "diet_history", "diet" -> true
        else -> false
    }
}