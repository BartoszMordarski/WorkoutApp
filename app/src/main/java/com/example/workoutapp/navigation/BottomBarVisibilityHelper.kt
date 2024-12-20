package com.example.workoutapp.navigation
import androidx.compose.runtime.Composable


@Composable
fun shouldShowBottomBar(route: String?): Boolean {
    return when (route) {
        "home", "add_workout", "exercises", "history", "diet" -> true
        else -> false
    }
}