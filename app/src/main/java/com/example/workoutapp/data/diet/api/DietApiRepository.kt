package com.example.workoutapp.data.diet.api

import android.util.Log
import com.example.workoutapp.BuildConfig
import javax.inject.Inject

class DietApiRepository @Inject constructor (private val api: CalorieNinjaApi) {

    suspend fun searchFoods(query: String): List<FoodItem> {
        val response = api.getFoods(query, BuildConfig.CALORIENINJA_API_KEY)
        Log.d("DietRepository", "Response: $response")
        return response.items
    }
}
