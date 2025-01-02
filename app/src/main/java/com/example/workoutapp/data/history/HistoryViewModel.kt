package com.example.workoutapp.data.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.activeworkout.WorkoutWithExercises
import com.example.workoutapp.data.activeworkout.workout.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workoutHistory = MutableStateFlow<List<WorkoutWithExercises>>(emptyList())
    val workoutHistory: StateFlow<List<WorkoutWithExercises>> get() = _workoutHistory

    private val _workoutDetails = MutableStateFlow<WorkoutWithExercises?>(null)
    val workoutDetails: StateFlow<WorkoutWithExercises?> get() = _workoutDetails

    private val _weeklyWorkoutCounts = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val weeklyWorkoutCounts: State<List<Pair<String, Int>>> get() = _weeklyWorkoutCounts

    init {
        fetchWorkoutHistory()
    }

    fun fetchWorkoutDetails(workoutId: Long) {
        viewModelScope.launch {
            val workoutDetails = workoutRepository.getWorkoutWithExercises(workoutId).firstOrNull()
            _workoutDetails.value = workoutDetails
        }
    }

    private fun fetchWorkoutHistory() {
        viewModelScope.launch {
            workoutRepository.getAllWorkoutsWithExercises().collect { workouts ->
                _workoutHistory.value = workouts
            }
        }
    }

    fun loadWeeklyWorkoutCounts() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val mondays = (0..4).map { today.minusWeeks(it.toLong()).with(DayOfWeek.MONDAY) }
            val workoutCounts = mondays.map { monday ->
                val nextMonday = monday.plusDays(7)
                val count = _workoutHistory.value.count {
                    val workoutDate = LocalDate.parse(it.workout.workoutDate)
                    workoutDate >= monday && workoutDate < nextMonday
                }
                monday.toString() to count
            }
            _weeklyWorkoutCounts.value = workoutCounts.reversed()
        }
    }

    fun deleteWorkout(workoutWithExercises: WorkoutWithExercises) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workoutWithExercises.workout)
        }
    }
}
