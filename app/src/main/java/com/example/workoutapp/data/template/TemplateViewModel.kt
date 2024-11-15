package com.example.workoutapp.data.template

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val templateRepository: WorkoutTemplateRepository
) : ViewModel() {

    private val _workoutTemplates = MutableStateFlow<List<TemplateWithExercises>>(emptyList())
    val workoutTemplates: StateFlow<List<TemplateWithExercises>> get() = _workoutTemplates

    init {
        viewModelScope.launch {
            templateRepository.addDefaultTemplates()
        }
        loadTemplates()

    }

    private fun loadTemplates() {
        viewModelScope.launch {
            templateRepository.getAllTemplates().collect { templates ->
                _workoutTemplates.value = templates
            }
        }
    }
}