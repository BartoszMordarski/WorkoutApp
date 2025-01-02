package com.example.workoutapp.presentation.workout

import androidx.compose.runtime.getValue
import com.example.workoutapp.data.activeworkout.DialogType

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel

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
                        Button(
                            onClick = {
                                viewModel.updateExistingTemplate(viewModel.originalTemplateId.value ?: 0L)
                                viewModel.saveWorkout()
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            }
                        ) {
                            Text("Update template and save values")
                        }

                        Button(
                            onClick = {
                                viewModel.saveWorkout()
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            }
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
                        Button(
                            onClick = {
                                viewModel.saveWorkout()
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            }
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
                        Button(
                            onClick = {
                                viewModel.saveAsNewTemplate()
                                viewModel.saveWorkout()
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            }
                        ) {
                            Text("Save as a template")
                        }

                        Button(
                            onClick = {
                                viewModel.saveWorkout()
                                viewModel.dismissDialog()
                                navController.navigateUp()
                            }
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

