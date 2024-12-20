package com.example.workoutapp.data.diet.model

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.diet.api.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(private val repository: DietRepository) : ViewModel() {

    private val _foods = MutableStateFlow<List<FoodItem>>(emptyList())
    val foods: StateFlow<List<FoodItem>> = _foods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchFoods(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val foodList = repository.searchFoods(query)
                _foods.value = foodList
            } catch (e: Exception) {
                Log.d("DietViewModel", "Error searching foods: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
