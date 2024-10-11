package com.example.workoutapp

data class WorkoutPlan(
    val name: String,
    val exercises: List<Exercise>
)

data class Exercise(
    val name: String,
    val muscleGroup: String,
    val sets: Int
)
