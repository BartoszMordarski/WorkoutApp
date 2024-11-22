package com.example.workoutapp.data.activeworkout.wexercise

import com.example.workoutapp.data.activeworkout.ExerciseWithSets
import com.example.workoutapp.data.template.texercise.TemplateExercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutExerciseRepository @Inject constructor (private val workoutExerciseDao: WorkoutExerciseDao) {


    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExercise): Long{
        return workoutExerciseDao.insertWorkoutExercise(workoutExercise)
    }

    fun getWorkoutExerciseById(id: Long): Flow<WorkoutExercise> {
        return workoutExerciseDao.getWorkoutExerciseById(id)
    }

    suspend fun deleteExercise(exerciseId: Long) {
        workoutExerciseDao.deleteExercise(exerciseId)
    }

    fun getLastWorkoutExerciseWithSets(exerciseId: Long): Flow<ExerciseWithSets?> {
        return workoutExerciseDao.getLastWorkoutExerciseWithSets(exerciseId)
    }
}