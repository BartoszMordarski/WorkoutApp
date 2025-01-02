package com.example.workoutapp.presentation.workout

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.ActiveWorkout
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun ActiveWorkoutScreen(
    navController: NavHostController,
    activeWorkout: ActiveWorkout,
    viewModel: ActiveWorkoutViewModel = hiltViewModel(),
    exerciseViewModel: ExerciseViewModel = hiltViewModel()
) {
    val workoutName by viewModel.workoutName.collectAsState()
    val exercises by viewModel.exercises.collectAsState()
    val setsInProgress by viewModel.setsInProgress.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    var cancelDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.isLoading.collectAsState()

    BackHandler {
        cancelDialog = true
    }
    if (cancelDialog) {
        AlertDialog(
            onDismissRequest = { cancelDialog = false },
            title = { Text("Cancel workout") },
            text = { Text("Are you sure you want to cancel this workout?") },
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
                            cancelDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Keep working out")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.cancelWorkout()
                            cancelDialog = false
                            navController.navigateUp()
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
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


    LaunchedEffect(activeWorkout) {
        if (activeWorkout.id != null) {
            viewModel.loadTemplateExercises(activeWorkout.id)
        }
    }

    LaunchedEffect(Unit) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        val exerciseId = savedStateHandle?.get<Long>("selectedExercise")
        exerciseId?.let {
            exerciseViewModel.getExerciseById(it)
        }
    }

    LaunchedEffect(exerciseViewModel.selectedExercise) {
        exerciseViewModel.selectedExercise.collect { exercise ->
            exercise?.let {

                val maxId = exercises.maxOfOrNull { it.workoutExerciseId } ?: 0

                val workoutExercise = WorkoutExercise(
                    workoutExerciseId = maxId + 1,
                    exerciseId = exercise.id,
                    exerciseName = exercise.name,
                    workoutId = 0
                )
                viewModel.addExercise(workoutExercise)
                exerciseViewModel.clearSelectedExercise()
            }
        }
    }

    DialogHandler(viewModel, dialogState, navController)

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = workoutName,
                    onValueChange = { newName -> viewModel.updateWorkoutName(newName) },
                    placeholder = { Text("Workout Name") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                WorkoutTimer(modifier = Modifier.weight(0.5f))

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        viewModel.finishWorkout()
                    },
                    modifier = Modifier.weight(0.7f)
                ) {
                    Text("Finish")
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(
                        items = exercises,
                        key = { it.workoutExerciseId }
                    ) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            sets = setsInProgress[exercise.workoutExerciseId] ?: emptyList(),
                            onAddSet = {
                                coroutineScope.launch {
                                    viewModel.addSetToWorkoutExercise(
                                        exercise.workoutExerciseId,
                                        exercise.exerciseId
                                    )
                                }
                            }
                        )
                    }

                    item {
                        Button(
                            onClick = { navController.navigate("select_exercise") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text("Add Exercise")
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                cancelDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Text("Cancel Workout")
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun WorkoutTimer(
    modifier: Modifier = Modifier,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
) {
    val time by viewModel.time.collectAsState()

    val hours = TimeUnit.MILLISECONDS.toHours(time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60

    val formattedTime = if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

    Text(text = formattedTime)
}
