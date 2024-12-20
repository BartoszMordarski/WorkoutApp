package com.example.workoutapp.data.diet.api

data class NutritionResponse(
    val items: List<FoodItem>
)

data class FoodItem(
    val name: String,
    val calories: Double,
    val serving_size_g: Double,
    val protein_g: Double,
    val carbohydrates_total_g: Double,
    val fat_total_g: Double,
    val sugar_g: Double,
    val fiber_g: Double
)