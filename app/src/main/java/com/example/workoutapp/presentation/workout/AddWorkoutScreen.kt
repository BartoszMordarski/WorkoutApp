package com.example.workoutapp.presentation.workout

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.data.template.TemplateViewModel
import com.example.workoutapp.data.template.TemplateWithExercises
import com.example.workoutapp.navigation.ActiveWorkout

@Composable
fun AddWorkoutScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: TemplateViewModel = hiltViewModel(),
    exerciseViewModel: ExerciseViewModel = hiltViewModel()
) {
    val workoutTemplates by viewModel.workoutTemplates.collectAsState()

    val defaultTemplates = workoutTemplates.filter { it.template.isDefault }
    val userTemplates = workoutTemplates.filter { !it.template.isDefault }

    Column(modifier = Modifier.fillMaxSize().padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = "New Workout",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
        }


        Button(
            onClick = {
                navController.navigate(ActiveWorkout(null))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Start an empty workout")
        }

        Text(
            text = "Available Workout Plans",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {

            if (userTemplates.isNotEmpty()) {
                item {
                    Text(
                        text = "User's Templates",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(userTemplates) { template ->
                    WorkoutTemplateCard(
                        template,
                        viewModel,
                        exerciseViewModel,
                        onClick = {
                            navController.navigate(ActiveWorkout(template.template.templateId))
                        }
                    )
                }
            }

            if (defaultTemplates.isNotEmpty()) {
                item {
                    Text(
                        text = "Default Templates",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(defaultTemplates) { template ->
                    WorkoutTemplateCard(
                        template,
                        viewModel,
                        exerciseViewModel,
                        onClick = {
                            navController.navigate(ActiveWorkout(template.template.templateId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutTemplateCard(
    template: TemplateWithExercises,
    templateViewModel: TemplateViewModel,
    viewModel: ExerciseViewModel,
    onClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete template") },
            text = { Text("Are you sure you want to delete this template?") },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            templateViewModel.deleteTemplate(template.template)
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.template.templateName,
                    style = MaterialTheme.typography.titleLarge
                )
                if(!template.template.isDefault){
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Template",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            template.exercises.forEach { exercise ->
                Text(
                    text = "${exercise.numberOfSets} x ${viewModel.getExerciseNameById(exercise.exerciseId)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



