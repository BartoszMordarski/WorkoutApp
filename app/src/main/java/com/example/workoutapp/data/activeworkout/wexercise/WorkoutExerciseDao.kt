package com.example.workoutapp.data.activeworkout.wexercise

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExercise): Long

    @Query("DELETE FROM workout_exercises WHERE workoutExerciseId = :exerciseId")
    suspend fun deleteExercise(exerciseId: Long)

    @Query("SELECT * FROM workout_exercises WHERE workoutExerciseId = :id")
    fun getWorkoutExerciseById(id: Long): Flow<WorkoutExercise>

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<WorkoutExercise>>
}