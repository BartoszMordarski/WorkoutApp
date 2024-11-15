package com.example.workoutapp.presentation.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.exercise.Exercise
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.AddExercise
import com.example.workoutapp.navigation.ExerciseDetail

@Composable
fun ExerciseScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val muscleGroup by viewModel.muscleGroup.collectAsState()
    val exercises by viewModel.exercises.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)
        .padding(bottom = paddingValues.calculateBottomPadding())
        .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(text = "Exercises", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.weight(1f))
            var expanded by remember { mutableStateOf(false) }

            IconButton(onClick = { navController.navigate(AddExercise(null)) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }


            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Select Muscle Group")
                }

                val muscleGroups =
                    listOf("All", "Core", "Arms", "Back", "Chest", "Legs", "Shoulders", "Full Body")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.3f),
                ) {
                    muscleGroups.forEach { group ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = group,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                viewModel.filterExercisesByMuscleGroup(group)
                                expanded = false
                            })
                    }
                }
            }

        }

        if (muscleGroup != "All" && muscleGroup.isNotEmpty()) {
            Text(
                text = "Selected Muscle Group: $muscleGroup",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn {
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onClick = {
                        navController.navigate(ExerciseDetail(exercise.id))
                    },
                    onEdit = {
                        navController.navigate(AddExercise(exercise.id))
                    },
                    onDelete = {
                        viewModel.deleteExercise(exercise)
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            if (exercise.isUserCreated) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Exercise"
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Exercise"
                    )
                }

            }
        }



        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete Exercise") },
                text = { Text("Are you sure you want to delete this exercise?") },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            onDelete()
                            showDialog = false
                        }) {
                            Text("Delete")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }
    }
}


