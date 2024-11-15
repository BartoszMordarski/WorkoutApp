package com.example.workoutapp.data.activeworkout.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0L,
    val workoutName: String,
    val workoutDate: String,
    val duration: String,
    val isTemplate: Boolean = false,
)
