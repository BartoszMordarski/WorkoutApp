package com.example.workoutapp.data.diet.model
import java.time.LocalDate

data class DietDay(
    val dateAdded: LocalDate,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
    val meals: List<DailyDietItem>
)