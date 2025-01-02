package com.example.workoutapp.presentation.workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.ui.draw.clipToBounds

@Composable
fun ExerciseCard(
    viewModel: ActiveWorkoutViewModel = hiltViewModel(),
    exercise: WorkoutExercise,
    sets: List<SetDetail>,
    onAddSet: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .clipToBounds()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = exercise.exerciseName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete exercise",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Set", modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Text(text = "Previous", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Text(text = "Kg", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Text(text = "Reps", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.weight(0.5f))
        }

        sets.forEachIndexed { index, setDetail ->
            key(setDetail.setUUID) {
                SetDetailRow(
                    setDetail = setDetail,
                    setNumber = index + 1

                )
            }
        }

        TextButton(
            onClick = onAddSet,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "+ Add Set")
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete exercise") },
            text = { Text("Are you sure you want to delete this exercise?") },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        viewModel.deleteExerciseFromWorkout(exercise.workoutExerciseId)
                        showDialog = false
                    }) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}
