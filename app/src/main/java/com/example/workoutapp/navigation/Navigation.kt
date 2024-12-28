package com.example.workoutapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.workoutapp.presentation.history.WorkoutHistoryScreen
import com.example.workoutapp.HomeView
import com.example.workoutapp.presentation.diet.AddCustomFoodScreen
import com.example.workoutapp.presentation.diet.DietSearchScreen
import com.example.workoutapp.presentation.diet.TodayDietScreen
import com.example.workoutapp.presentation.exercise.ExerciseDetailsScreen
import com.example.workoutapp.presentation.exercise.ExerciseScreen
import com.example.workoutapp.presentation.workout.AddWorkoutScreen
import com.example.workoutapp.presentation.exercise.AddEditExerciseScreen
import com.example.workoutapp.presentation.history.DietHistoryScreen
import com.example.workoutapp.presentation.history.HistoryDetailsScreen
import com.example.workoutapp.presentation.history.HistoryScreen
import com.example.workoutapp.presentation.workout.ActiveWorkoutScreen
import com.example.workoutapp.presentation.workout.SelectExerciseScreen


@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeView(innerPadding)
        }

        composable(Screen.Diet.route) {
            DietSearchScreen(navController, innerPadding)
        }

        composable(Screen.TodayDiet.route) {
            TodayDietScreen(navController)
        }

        composable(Screen.AddCustomMeal.route) {
            AddCustomFoodScreen(navController)
        }

        composable(Screen.AddWorkout.route) {
            AddWorkoutScreen(navController, innerPadding)
        }

        composable(Screen.Exercises.route) {
            ExerciseScreen(navController, innerPadding)
        }

        composable(Screen.History.route) {
            HistoryScreen(navController, innerPadding)
        }

        composable(Screen.WorkoutHistory.route) {
            WorkoutHistoryScreen(navController, innerPadding)
        }

        composable(Screen.DietHistory.route) {
            DietHistoryScreen(navController, innerPadding)
        }

        composable<HistoryDetail> { backStackEntry ->
            val historyDetail: HistoryDetail = backStackEntry.toRoute()
            HistoryDetailsScreen(historyDetail, navController)
        }

        composable<ExerciseDetail> { backStackEntry ->
            val exerciseDetail: ExerciseDetail = backStackEntry.toRoute()
            ExerciseDetailsScreen(exerciseDetail, navController)
        }

        composable<AddExercise> { backStackEntry ->
            val addExercise: AddExercise = backStackEntry.toRoute()
            AddEditExerciseScreen(navController, addExercise)
        }

        composable<ActiveWorkout> { backStackEntry ->
            val activeWorkout: ActiveWorkout = backStackEntry.toRoute()
            ActiveWorkoutScreen(navController, activeWorkout)
        }

        composable(Screen.SelectExercise.route) {
            SelectExerciseScreen(navController)
        }
    }
}