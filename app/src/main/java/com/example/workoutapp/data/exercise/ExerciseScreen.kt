package com.example.workoutapp.data.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ExerciseScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val muscleGroup by viewModel.muscleGroup.collectAsState()
    val exercises by viewModel.exercises.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(text = "Exercises", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.weight(1f))
            var expanded by remember { mutableStateOf(false) }

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
                ExerciseItem(exercise) {
                    //TODO Navigate to exercise detail screen, add a plus icon next to dropdown to add custom exercise

                }
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Text(
            text = exercise.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

