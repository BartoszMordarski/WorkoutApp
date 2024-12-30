package com.example.workoutapp.presentation.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.exercise.ExerciseViewModel
import com.example.workoutapp.navigation.ExerciseDetail

@Composable
fun ExerciseDetailsScreen(
    exerciseDetail: ExerciseDetail,
    navController: NavController,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val exercise by viewModel.exerciseDetail.collectAsState()

    LaunchedEffect(exerciseDetail.id) {
        viewModel.getExerciseById(exerciseDetail.id)
    }

    exercise?.let {
        val context = LocalContext.current
        val imageResId = remember(it.imageName) {
            context.resources.getIdentifier(it.imageName, "drawable", context.packageName)
        }

        var showInstructions by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = it.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 24.dp),
                color = Color.White
            )

            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = it.name,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f / 3f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f / 2.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image Available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Muscle Group",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = it.muscleGroup,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = it.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))


                Button(
                    onClick = { showInstructions = !showInstructions },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = if (showInstructions) "Hide Instructions" else "Show Instructions")
                }

                AnimatedVisibility(
                    visible = showInstructions,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Instructions:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it.instructions ?: "No instructions available.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }


            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
