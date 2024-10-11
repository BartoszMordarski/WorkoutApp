package com.example.workoutapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workoutapp.data.workout.exercise.WorkoutExercise

@Entity(
    tableName = "set_details",
    foreignKeys = [ForeignKey(
        entity = WorkoutExercise::class,
        parentColumns = ["workoutExerciseId"],
        childColumns = ["workoutExerciseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SetDetail(
    @PrimaryKey(autoGenerate = true)
    val setId: Long = 0L,
    val workoutExerciseId: Long,
    val setNumber: Int,
    val weight: Float,
    val reps: Int,
)
