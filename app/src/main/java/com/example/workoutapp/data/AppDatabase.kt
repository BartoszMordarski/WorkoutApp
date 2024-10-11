package com.example.workoutapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workoutapp.data.exercise.Exercise
import com.example.workoutapp.data.exercise.ExerciseDao

@Database(
    entities = [Exercise::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

}