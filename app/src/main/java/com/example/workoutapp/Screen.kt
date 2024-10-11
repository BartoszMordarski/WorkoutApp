package com.example.workoutapp

import java.io.Serializable

sealed class Screen(val route: String): Serializable {
    object Home : Screen("home")
    object AddWorkout : Screen("add_workout")
    object Exercises : Screen("exercises")

}