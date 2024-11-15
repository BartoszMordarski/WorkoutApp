package com.example.workoutapp.presentation.workout

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    Log.d("AddWorkoutScreen", "Templates: $workoutTemplates")

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
                style = MaterialTheme.typography.headlineLarge,
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

        Log.d("WorkoutTemplates", "Templates: $workoutTemplates")

        LazyColumn {
            items(workoutTemplates) { template ->
                Log.d("LazyColumn", "Rendering template: ${template.template.templateName}")
                WorkoutTemplateCard(
                    template,
                    exerciseViewModel,
                    onClick = {
                        navController.navigate(ActiveWorkout(template.template.templateId))
                    }
                )
            }
        }
    }
}

@Composable
fun WorkoutTemplateCard(
    template: TemplateWithExercises,
    viewModel: ExerciseViewModel,
    onClick: () -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = template.template.templateName,
                style = MaterialTheme.typography.titleLarge
            )
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


