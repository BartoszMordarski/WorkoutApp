package com.example.workoutapp.data.diet.model

import android.util.Log
import com.example.workoutapp.BuildConfig
import com.example.workoutapp.data.diet.api.CalorieNinjaApi
import com.example.workoutapp.data.diet.api.FoodItem
import javax.inject.Inject

class DietRepository @Inject constructor (private val api: CalorieNinjaApi) {

    suspend fun searchFoods(query: String): List<FoodItem> {
        val response = api.getFoods(query, BuildConfig.CALORIENINJA_API_KEY)
        Log.d("DietRepository", "Response: $response")
        return response.items
    }
}
