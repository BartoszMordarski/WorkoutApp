package com.example.workoutapp.presentation.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.exercise.Exercise
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.ExerciseDetail

@Composable
fun ExerciseDetailsScreen(exerciseDetail: ExerciseDetail, navController: NavController, viewModel: ExerciseViewModel = hiltViewModel()) {

    val exercise by viewModel.exerciseDetail.collectAsState()

    LaunchedEffect(exerciseDetail.id) {
        viewModel.getExerciseById(exerciseDetail.id)
    }

    exercise?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.headlineLarge)
            Text(text = "Muscle group: ${it.muscleGroup}")
            Text(text = "Description: ${it.description}")
        }
    } ?: run {
        Text(text = "Loading...", modifier = Modifier.padding(16.dp))
    }
}
