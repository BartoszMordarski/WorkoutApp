package com.example.workoutapp.data.activeworkout

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise


data class ExerciseWithSets(
    @Embedded
    val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "workoutExerciseId",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<SetDetail>
)
