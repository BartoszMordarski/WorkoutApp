package com.example.workoutapp.navigation

import java.io.Serializable

sealed class Screen(val route: String): Serializable {
    object Home : Screen("home")
    object AddWorkout : Screen("add_workout")
    object Exercises : Screen("exercises")
    object History : Screen("history")
    object SelectExercise : Screen("select_exercise")

}

@kotlinx.serialization.Serializable
data class ExerciseDetail(val id: Long)

@kotlinx.serialization.Serializable
data class AddExercise(val id: Long?)

@kotlinx.serialization.Serializable
data class ActiveWorkout(val id: Long?)