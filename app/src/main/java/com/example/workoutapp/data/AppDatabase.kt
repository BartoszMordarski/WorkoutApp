package com.example.workoutapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.set.SetDetailDao
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExerciseDao
import com.example.workoutapp.data.activeworkout.workout.Workout
import com.example.workoutapp.data.activeworkout.workout.WorkoutDao
import com.example.workoutapp.data.exercise.Exercise
import com.example.workoutapp.data.exercise.ExerciseDao

@Database(
    entities = [Exercise::class, Workout::class, WorkoutExercise::class, SetDetail::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun setDetailDao(): SetDetailDao

}