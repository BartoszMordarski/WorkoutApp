package com.example.workoutapp.data.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor (private val exerciseRepository: ExerciseRepository) : ViewModel() {

    private val _muscleGroup = MutableStateFlow<String>("")
    val muscleGroup: StateFlow<String> get() = _muscleGroup

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> get() = _exercises

    init {
        viewModelScope.launch {
            exerciseRepository.addDefaultExercises()
        }
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseRepository.allExercises.collect { allExercises ->
                _exercises.value = allExercises
            }
        }
    }

    fun filterExercisesByMuscleGroup(muscleGroup: String) {
        viewModelScope.launch {
            _muscleGroup.value = muscleGroup
            if(muscleGroup == "All"){
                exerciseRepository.allExercises.collect { allExercises ->
                    _exercises.value = allExercises
                }
            } else {
                exerciseRepository.getExercisesByMuscleGroup(muscleGroup).collect { exercises ->
                    _exercises.value = exercises
                }
            }
        }
    }
}