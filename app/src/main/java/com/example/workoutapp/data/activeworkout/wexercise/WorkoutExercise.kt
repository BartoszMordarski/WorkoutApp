package com.example.workoutapp.data.activeworkout.wexercise

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.workout.Workout

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
    val difficulty: String? = null
)
