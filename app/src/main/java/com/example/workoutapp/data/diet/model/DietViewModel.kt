package com.example.workoutapp.data.diet.model

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.diet.api.DietApiRepository
import com.example.workoutapp.data.diet.api.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val apiRepository: DietApiRepository,
    private val dailyDietRepository: DailyDietRepository,
) : ViewModel() {

    private val _foods = MutableStateFlow<List<FoodItem>>(emptyList())
    val foods: StateFlow<List<FoodItem>> = _foods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val todayDiet: Flow<List<DailyDietItem>> = dailyDietRepository.getDietItemsForToday()

    fun addFoodToDiet(food: FoodItem) {
        val today = LocalDate.now()
        val dietItem = DailyDietItem(
            name = food.name,
            calories = food.calories,
            protein = food.protein_g,
            fat = food.fat_total_g,
            carbs = food.carbohydrates_total_g,
            dateAdded = today
        )
        viewModelScope.launch {
            dailyDietRepository.addDietItem(dietItem)
        }
    }

    fun deleteDietItem(item: DailyDietItem) {
        viewModelScope.launch {
            dailyDietRepository.deleteDietItem(item)
        }
    }

    fun searchFoods(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val foodList = apiRepository.searchFoods(query)
                _foods.value = foodList
            } catch (e: Exception) {
                Log.d("DietViewModel", "Error searching foods: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}