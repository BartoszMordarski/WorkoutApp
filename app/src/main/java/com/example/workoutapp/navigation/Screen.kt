package com.example.workoutapp.navigation

import java.io.Serializable

sealed class Screen(val route: String): Serializable {
    object Home : Screen("home")
    object Diet : Screen("diet")
    object AddWorkout : Screen("add_workout")
    object Exercises : Screen("exercises")
    object History : Screen("history")
    object WorkoutHistory : Screen("workout_history")
    object DietHistory : Screen("diet_history")
    object SelectExercise : Screen("select_exercise")
    object TodayDiet: Screen("today_diet_screen")
    object AddCustomMeal: Screen("add_custom_meal_screen")

}

@kotlinx.serialization.Serializable
data class ExerciseDetail(val id: Long)

@kotlinx.serialization.Serializable
data class AddExercise(val id: Long?)

@kotlinx.serialization.Serializable
data class ActiveWorkout(val id: Long?)

@kotlinx.serialization.Serializable
data class HistoryDetail(val id: Long)