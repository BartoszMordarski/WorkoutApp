package com.example.workoutapp.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutapp.data.workout.exercise.WorkoutExercise

data class ExerciseWithSets(
    @Embedded
    val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "workoutExerciseId",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<SetDetail>
)
