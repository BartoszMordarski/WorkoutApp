package com.example.workoutapp.presentation.exercise

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
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
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val exerciseId = addExercise.id
    val focusManager = LocalFocusManager.current


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
            .padding(20.dp)
            .padding(top = 40.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Insert exercise name",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Exercise Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = MaterialTheme.shapes.medium
        )

        MuscleGroupSelector(
            selectedMuscleGroup = muscleGroup,
            onMuscleGroupSelected = { muscleGroup = it }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if (name.isEmpty() || muscleGroup.isEmpty()) {
                    errorMessage = "Please fill in all fields"
                } else {
                    errorMessage = ""
                    if (isInEditMode) {
                        exerciseId?.let { viewModel.updateExercise(it, name, muscleGroup) }
                    } else {
                        viewModel.addExercise(name, muscleGroup)
                    }
                    navController.navigateUp()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = name.isNotEmpty() && muscleGroup.isNotEmpty()
        ) {
            Text(text = if (isInEditMode) "Update Exercise" else "Add Exercise")
        }

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Cancel")
        }
    }
}

@Composable
fun MuscleGroupSelector(selectedMuscleGroup: String, onMuscleGroupSelected: (String) -> Unit) {
    val muscleGroups = listOf("Arms", "Back", "Chest", "Legs", "Shoulders", "Core", "Full Body")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Select muscle group",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        val scrollState = rememberScrollState()

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.horizontalScroll(scrollState)
        ) {
            muscleGroups.forEach { group ->
                MuscleGroupButton(
                    group = group,
                    isSelected = group == selectedMuscleGroup,
                    onClick = { onMuscleGroupSelected(group) }
                )
            }
        }
    }
}

@Composable
fun MuscleGroupButton(group: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = group,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
