package com.example.workoutapp.data.workout

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithSets(
    @Embedded
    val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "workoutExerciseId",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<SetDetail>
)
