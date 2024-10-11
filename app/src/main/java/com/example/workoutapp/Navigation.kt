package com.example.workoutapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workoutapp.data.exercise.ExerciseScreen

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeView(modifier = Modifier.padding(innerPadding))
        }

        composable(Screen.AddWorkout.route) {
            AddWorkoutScreen(navController, Modifier.padding(innerPadding))
        }

        composable(Screen.Exercises.route) {
            ExerciseScreen(navController, Modifier.padding(innerPadding))
        }
    }
}