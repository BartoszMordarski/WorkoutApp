package com.example.workoutapp.data.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0L,
    val workoutName: String,
)
