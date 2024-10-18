package com.example.workoutapp.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.Exercise
import com.example.workoutapp.WorkoutPlan

@Composable
fun AddWorkoutScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "New Workout",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                navController.navigate("activeWorkout")
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

        val sampleWorkoutPlans = listOf(
            WorkoutPlan(
                name = "Full Body Workout",
                exercises = listOf(
                    Exercise(name = "Squat", muscleGroup = "Legs", sets = 3),
                    Exercise(name = "Bench Press", muscleGroup = "Chest", sets = 4),
                    Exercise(name = "Deadlift", muscleGroup = "Back", sets = 3),
                    Exercise(name = "Shoulder Press", muscleGroup = "Shoulders", sets = 3)
                )
            ),
            WorkoutPlan(
                name = "Upper Body Strength",
                exercises = listOf(
                    Exercise(name = "Pull Ups", muscleGroup = "Back", sets = 4),
                    Exercise(name = "Overhead Press", muscleGroup = "Shoulders", sets = 3),
                    Exercise(name = "Dumbbell Curls", muscleGroup = "Arms", sets = 3),
                    Exercise(name = "Tricep Dips", muscleGroup = "Arms", sets = 3)
                )
            ),
            WorkoutPlan(
                name = "Leg Day",
                exercises = listOf(
                    Exercise(name = "Leg Press", muscleGroup = "Legs", sets = 4),
                    Exercise(name = "Lunges", muscleGroup = "Legs", sets = 3),
                    Exercise(name = "Leg Curls", muscleGroup = "Legs", sets = 4),
                    Exercise(name = "Calf Raises", muscleGroup = "Legs", sets = 3)
                )
            )
        )

        LazyColumn {
            items(sampleWorkoutPlans) { plan ->
                WorkoutPlanItem(plan)
            }
        }
    }
}

@Composable
fun WorkoutPlanItem(workoutPlan: WorkoutPlan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workoutPlan.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            workoutPlan.exercises.forEach { exercise ->
                Text(
                    text = "${exercise.sets} x ${exercise.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAddWorkoutScreen() {
    AddWorkoutScreen(navController = rememberNavController())
}

