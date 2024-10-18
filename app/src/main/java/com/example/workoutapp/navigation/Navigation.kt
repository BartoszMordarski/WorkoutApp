package com.example.workoutapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.workoutapp.HistoryScreen
import com.example.workoutapp.HomeView
import com.example.workoutapp.presentation.exercise.ExerciseDetailsScreen
import com.example.workoutapp.presentation.exercise.ExerciseScreen
import com.example.workoutapp.presentation.workout.AddWorkoutScreen
import com.example.workoutapp.presentation.exercise.AddEditExerciseScreen


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

        composable(Screen.History.route) {
            HistoryScreen(modifier = Modifier.padding(innerPadding))
        }

        composable<ExerciseDetail> { backStackEntry ->
            val exerciseDetail : ExerciseDetail = backStackEntry.toRoute()
            ExerciseDetailsScreen(exerciseDetail, navController)
        }

        composable<AddExercise> { backStackEntry ->
            val addExercise : AddExercise = backStackEntry.toRoute()
            AddEditExerciseScreen(navController, addExercise)
        }
    }
}