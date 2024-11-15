package com.example.workoutapp.data.activeworkout.workout

import com.example.workoutapp.data.activeworkout.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepository @Inject constructor (private val workoutDao: WorkoutDao) {

    suspend fun insertWorkout(workout: Workout): Long{
        return workoutDao.insertWorkout(workout)
    }


    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>> {
        return workoutDao.getAllWorkoutsWithExercises()
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }
}