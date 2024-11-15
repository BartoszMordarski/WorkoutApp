package com.example.workoutapp.data.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.activeworkout.WorkoutWithExercises
import com.example.workoutapp.data.activeworkout.workout.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workoutHistory = MutableStateFlow<List<WorkoutWithExercises>>(emptyList())
    val workoutHistory: StateFlow<List<WorkoutWithExercises>> get() = _workoutHistory

    init {
        fetchWorkoutHistory()
    }

    private fun fetchWorkoutHistory() {
        viewModelScope.launch {
            workoutRepository.getAllWorkoutsWithExercises().collect { workouts ->
                _workoutHistory.value = workouts
            }
        }
    }

    fun deleteWorkout(workoutWithExercises: WorkoutWithExercises) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workoutWithExercises.workout)
        }
    }
}
