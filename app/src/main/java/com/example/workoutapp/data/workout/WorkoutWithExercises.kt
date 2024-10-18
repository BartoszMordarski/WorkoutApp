package com.example.workoutapp.data.workout

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseWithSets>
)
