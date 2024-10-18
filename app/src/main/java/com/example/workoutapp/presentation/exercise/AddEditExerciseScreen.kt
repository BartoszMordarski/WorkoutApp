package com.example.workoutapp.presentation.exercise

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.AddExercise

@Composable
fun AddEditExerciseScreen(
    navController: NavController,
    addExercise: AddExercise,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var muscleGroup by rememberSaveable { mutableStateOf("") }
    var isInEditMode by rememberSaveable { mutableStateOf(false) }
    val exerciseId = addExercise.id

    LaunchedEffect(exerciseId) {
        exerciseId?.let {
            isInEditMode = true
            viewModel.getExerciseById(exerciseId)
            viewModel.selectedExercise.collect { exercise ->
                exercise?.let {
                    name = exercise.name
                    muscleGroup = exercise.muscleGroup
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Exercise Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MuscleGroupDropdown(selectedMuscleGroup = muscleGroup, onMuscleGroupSelected = { muscleGroup = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (isInEditMode) {
                exerciseId?.let { viewModel.updateExercise(it, name, muscleGroup) }
            } else {
                viewModel.addExercise(name, muscleGroup)
            }
            navController.navigateUp()
        }) {
            Text(text = if (isInEditMode) "Update Exercise" else "Add Exercise")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigateUp() }) {
            Text(text = "Cancel")
        }
    }
}

@Composable
fun MuscleGroupDropdown(selectedMuscleGroup: String, onMuscleGroupSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val muscleGroups = listOf("Arms", "Back", "Chest", "Legs", "Shoulders", "Core", "Full Body")

    Column {
        Box(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(12.dp)
                .border(width = 1.dp, color = Color.Gray)
                .padding(8.dp)
        ) {
            Text(text = selectedMuscleGroup.ifEmpty { "Select Muscle Group" })
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            muscleGroups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(text = group) },
                    onClick = {
                        onMuscleGroupSelected(group)
                        expanded = false
                    })
            }
        }
    }
}


