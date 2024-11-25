package com.example.workoutapp.presentation.workout

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.ActiveWorkout
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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
                        modifier = Modifier.fillMaxWidth(0.8f)
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
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cancel Workout")
                        }
                    }
                }
            }
        }
    }
}

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
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = exercise.exerciseName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete exercise"
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Set", modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center)
            Text(text = "Previous", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(text = "Kg", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(text = "Reps", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
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

                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}


@Composable
fun SetDetailRow(
    setDetail: SetDetail,
    setNumber: Int,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
) {

    var isCompleted by remember { mutableStateOf(setDetail.isCompleted) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val deleteThreshold = -300f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (offsetX < deleteThreshold) Color.Red else Color.Transparent)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < deleteThreshold) {
                                scope.launch {
                                    viewModel.removeSetFromWorkoutExercise(
                                        setDetail.workoutExerciseId,
                                        setDetail
                                    )
                                }
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX += dragAmount
                        }
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = setNumber.toString(),
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (setDetail.previousWeight != null && setDetail.previousReps != null) {
                    "${setDetail.previousWeight.toInt()}kg x ${setDetail.previousReps}"
                } else {
                    "-"
                },
                modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )

            var weight by remember { mutableStateOf(setDetail.weight.toString()) }
            TextField(
                value = weight,
                onValueChange = { newValue ->
                    weight = newValue
                    setDetail.weight = newValue.toFloatOrNull() ?: 0f
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                label = null,
                placeholder = { Text("kg") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            var reps by remember { mutableStateOf(setDetail.reps.toString()) }
            TextField(
                value = reps,
                onValueChange = { newValue ->
                    reps = newValue
                    setDetail.reps = newValue.toIntOrNull() ?: 0
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                label = null,
                placeholder = { Text("reps") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = {
                    viewModel.markSetAsCompleted(setDetail.workoutExerciseId, setDetail.setUUID)
                    isCompleted = !isCompleted
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm Set",
                    tint = if (isCompleted) Color.Green else Color.Gray
                )
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
