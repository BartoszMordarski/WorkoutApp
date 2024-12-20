package com.example.workoutapp.data.diet.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CalorieNinjaApi {

    @GET("v1/nutrition")
    suspend fun getFoods(
        @Query("query") query: String,
        @Header("X-Api-Key") apiKey: String
    ): NutritionResponse
}
