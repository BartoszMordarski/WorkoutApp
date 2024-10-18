package com.example.workoutapp.data.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository) :
    ViewModel() {

    private val _muscleGroup = MutableStateFlow("")
    val muscleGroup: StateFlow<String> get() = _muscleGroup

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> get() = _exercises

    private val _exerciseDetail = MutableStateFlow<Exercise?>(null)
    val exerciseDetail: StateFlow<Exercise?> get() = _exerciseDetail

    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> get() = _selectedExercise

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
            if (muscleGroup == "All") {
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

    fun getExerciseById(id: Long) {
        viewModelScope.launch {
            exerciseRepository.getExerciseById(id).collect { exercise ->
                _exerciseDetail.value = exercise
                _selectedExercise.value = exercise
            }
        }
    }

    fun addExercise(name: String, muscleGroup: String) {
        viewModelScope.launch {
            val newExercise = Exercise(
                name = name,
                muscleGroup = muscleGroup,
                description = null,
                isUserCreated = true
            )
            exerciseRepository.insertExercise(newExercise)
        }
    }

    fun updateExercise(id: Long, name: String, muscleGroup: String) {
        viewModelScope.launch {
            exerciseRepository.getExerciseById(id).firstOrNull()?.let { exercise ->
                val updatedExercise = exercise.copy(name = name, muscleGroup = muscleGroup)
                exerciseRepository.updateExercise(updatedExercise)
            }
        }
    }


    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
        }
    }

}