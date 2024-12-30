package com.example.workoutapp.presentation.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
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

    Column(
        modifier = Modifier
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
            Text(
                text = "Exercises",
                style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            var expanded by remember { mutableStateOf(false) }

            IconButton(onClick = { navController.navigate(AddExercise(null)) }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Exercise",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            DropdownMenuWithAnimation(viewModel)
        }

        if (muscleGroup != "All" && muscleGroup.isNotEmpty()) {
            Text(
                text = "Exercises targetting ${muscleGroup.toLowerCase()}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onClick = {
                        if (!exercise.isUserCreated) {
                            navController.navigate(ExerciseDetail(exercise.id))
                        }
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
fun DropdownMenuWithAnimation(viewModel: ExerciseViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val muscleGroups = listOf("All", "Core", "Arms", "Back", "Chest", "Legs", "Shoulders", "Full Body")

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Select Muscle Group",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.wrapContentSize().align(Alignment.TopStart)

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
            .height(90.dp)
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )

            if (exercise.isUserCreated) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Exercise",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Exercise",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete Exercise", style = MaterialTheme.typography.titleMedium) },
                text = { Text("Are you sure you want to delete this exercise?") },
                confirmButton = {
                    Button(onClick = {
                        onDelete()
                        showDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

}



