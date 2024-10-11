package com.example.workoutapp
import androidx.compose.runtime.Composable


@Composable
fun shouldShowBottomBar(route: String?): Boolean {
    return when (route) {
        "home", "add_workout", "exercises" -> true
        else -> false
    }
}