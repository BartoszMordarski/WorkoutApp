package com.example.workoutapp.data.activeworkout

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.activeworkout.workout.Workout

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout,
    @Relation(
        entity = WorkoutExercise::class,
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseWithSets>
)
