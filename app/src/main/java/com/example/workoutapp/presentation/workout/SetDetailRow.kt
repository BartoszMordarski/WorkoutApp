package com.example.workoutapp.presentation.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutapp.data.activeworkout.ActiveWorkoutViewModel
import com.example.workoutapp.data.activeworkout.set.SetDetail
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetDetailRow(
    setDetail: SetDetail,
    setNumber: Int,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
) {

    var isCompleted by remember { mutableStateOf(setDetail.isCompleted) }
    val scope = rememberCoroutineScope()

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                scope.launch {
                    viewModel.removeSetFromWorkoutExercise(
                        setDetail.workoutExerciseId,
                        setDetail
                    )
                }
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color by animateColorAsState(
                targetValue = if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red else Color.Transparent,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
            )
        },
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.25f) },
        dismissContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = setNumber.toString(),
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (setDetail.previousWeight != null && setDetail.previousReps != null) {
                            "${setDetail.previousWeight.toInt()}kg x ${setDetail.previousReps}"
                        } else {
                            "-"
                        },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
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
    )
}
