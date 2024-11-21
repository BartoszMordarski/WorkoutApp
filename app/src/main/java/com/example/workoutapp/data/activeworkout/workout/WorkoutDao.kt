package com.example.workoutapp.data.activeworkout.workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.workoutapp.data.activeworkout.ExerciseWithSets
import com.example.workoutapp.data.activeworkout.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getWorkoutById(workoutId: Long): Flow<Workout?>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): Flow<WorkoutWithExercises>

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE isTemplate = 1")
    fun getAllTemplates(): Flow<List<Workout>>

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId")
    fun getExercisesWithSets(workoutId: Long): Flow<List<ExerciseWithSets>>
}