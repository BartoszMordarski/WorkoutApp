package com.example.workoutapp.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.history.HistoryViewModel
import com.example.workoutapp.navigation.HistoryDetail

@Composable
fun HistoryDetailsScreen(
    historyDetail: HistoryDetail,
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val workoutDetails by viewModel.workoutDetails.collectAsState()

    LaunchedEffect(historyDetail.id) {
        viewModel.fetchWorkoutDetails(historyDetail.id)
    }

    workoutDetails?.let { workout ->
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(top = 24.dp)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = workout.workout.workoutName,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Workout Date",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = workout.workout.workoutDate,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Workout Duration",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = workout.workout.duration,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(workout.exercises) { exerciseWithSets ->
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = exerciseWithSets.workoutExercise.exerciseName,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        exerciseWithSets.sets.forEachIndexed { index, set ->
                            Text(
                                text = "${index + 1}. ${set.weight}kg x ${set.reps}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.headlineSmall.fontSize), // Powiększony tekst
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

