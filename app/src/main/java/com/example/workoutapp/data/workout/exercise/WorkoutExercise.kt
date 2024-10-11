package com.example.workoutapp.data.workout.exercise

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workoutapp.data.Workout

@Entity(
    tableName = "workout_exercises",
    foreignKeys = [ForeignKey(
        entity = Workout::class,
        parentColumns = ["workoutId"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkoutExercise(

    @PrimaryKey(autoGenerate = true)
    val workoutExerciseId: Long = 0L,
    val workoutId: Long,
    val exerciseId: Long,
    val exerciseName: String,
)
