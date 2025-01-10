package com.example.workoutapp.presentation.workout

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel
import com.example.workoutapp.data.activeworkout.DialogType

@Composable
fun DialogHandler(
    viewModel: ActiveWorkoutViewModel,
    dialogState: DialogType?,
    navController: NavController
) {
    when (dialogState) {
        DialogType.DialogForTemplateChangedWorkout -> {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = { Text("Finish workout") },
                text = { Text("The template workout has been modified. Do you want to save the changes?") },
                confirmButton = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var selectedDifficulty by remember { mutableStateOf<String?>(null) }

                        DifficultySelector(
                            selectedDifficulty = selectedDifficulty,
                            onDifficultySelected = { selectedDifficulty = it }
                        )

                        Button(
                            onClick = {
                                viewModel.updateExistingTemplate(viewModel.originalTemplateId.value ?: 0L)
                                viewModel.saveWorkout(selectedDifficulty)
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            },
                            enabled = selectedDifficulty != null
                        ) {
                            Text("Update template and save values")
                        }

                        Button(
                            onClick = {
                                viewModel.saveWorkout(selectedDifficulty)
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            },
                            enabled = selectedDifficulty != null
                        ) {
                            Text("Save values only")
                        }
                    }
                }
            )
        }

        DialogType.DialogForTemplateUnchangedWorkout -> {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = { Text("Finish workout") },
                text = { Text("The workout template has not been changed. Do you want to finish this workout?") },
                confirmButton = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var selectedDifficulty by remember { mutableStateOf<String?>(null) }

                        DifficultySelector(
                            selectedDifficulty = selectedDifficulty,
                            onDifficultySelected = { selectedDifficulty = it }
                        )

                        Button(
                            onClick = {
                                viewModel.saveWorkout(selectedDifficulty)
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            },
                            enabled = selectedDifficulty != null
                        ) {
                            Text("Finish workout and save values")
                        }

                        Button(
                            onClick = { viewModel.dismissDialog() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }

        DialogType.DialogForEmptyWorkout -> {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = { Text("Finish workout") },
                text = { Text("Do you want to save this workout as a template?") },
                confirmButton = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var selectedDifficulty by remember { mutableStateOf<String?>(null) }

                        DifficultySelector(
                            selectedDifficulty = selectedDifficulty,
                            onDifficultySelected = { selectedDifficulty = it }
                        )

                        Button(
                            onClick = {
                                viewModel.saveAsNewTemplate()
                                viewModel.saveWorkout(selectedDifficulty)
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            },
                            enabled = selectedDifficulty != null
                        ) {
                            Text("Save as a template")
                        }

                        Button(
                            onClick = {
                                viewModel.saveWorkout(selectedDifficulty)
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            },
                            enabled = selectedDifficulty != null
                        ) {
                            Text("Save values only")
                        }
                    }
                }
            )
        }
        null -> {}
    }
}


@Composable
fun DifficultySelector(selectedDifficulty: String?, onDifficultySelected: (String) -> Unit) {
    val difficulties = listOf("Easy", "Medium", "Hard")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        val scrollState = rememberScrollState()

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.horizontalScroll(scrollState)
        ) {
            difficulties.forEach { difficulty ->
                DifficultyButton(
                    difficulty = difficulty,
                    isSelected = difficulty == selectedDifficulty,
                    onClick = { onDifficultySelected(difficulty) }
                )
            }
        }
    }
}

@Composable
fun DifficultyButton(difficulty: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) Color(0xFF81C784) else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color(0xFF388E3C) else MaterialTheme.colorScheme.onSurface

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
            text = difficulty,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
