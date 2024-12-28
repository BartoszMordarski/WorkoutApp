package com.example.workoutapp.data.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.diet.model.DailyDietRepository
import com.example.workoutapp.data.diet.model.DietDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DietHistoryViewModel @Inject constructor(
    private val dietRepository: DailyDietRepository
) : ViewModel() {

    private val _dietHistory = MutableStateFlow<List<DietDay>>(emptyList())
    val dietHistory: StateFlow<List<DietDay>> = _dietHistory

    init {
        loadDietHistory()
    }

    private fun loadDietHistory() {
        viewModelScope.launch {
            val allMeals = dietRepository.getAllDietItems()
            val groupedMeals = allMeals.groupBy { it.dateAdded }

            _dietHistory.value = groupedMeals.map { entry ->
                val totalCalories = entry.value.sumOf { it.calories }
                val totalProtein = entry.value.sumOf { it.protein }
                val totalFat = entry.value.sumOf { it.fat }
                val totalCarbs = entry.value.sumOf { it.carbs }

                DietDay(
                    dateAdded = entry.key,
                    totalCalories = totalCalories,
                    totalProtein = totalProtein,
                    totalFat = totalFat,
                    totalCarbs = totalCarbs,
                    meals = entry.value
                )
            }
        }
    }

    fun deleteDietByDate(date: LocalDate) {
        viewModelScope.launch {
            dietRepository.deleteDietByDate(date)
            _dietHistory.value = _dietHistory.value.filter { it.dateAdded != date }
        }
    }
}
