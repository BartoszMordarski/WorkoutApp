package com.example.workoutapp.data.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val muscleGroup: String = "",
    val description: String = "",
)
