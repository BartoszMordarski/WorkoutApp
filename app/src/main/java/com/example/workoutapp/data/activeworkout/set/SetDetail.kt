package com.example.workoutapp.data.activeworkout.set

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import java.util.UUID

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
    var setNumber: Int,
    var weight: Float,
    var reps: Int,
    var isCompleted: Boolean = false,
    @Ignore val setUUID: String = UUID.randomUUID().toString()

) {
    constructor(
        setId: Long,
        workoutExerciseId: Long,
        setNumber: Int,
        weight: Float,
        reps: Int,
        isCompleted: Boolean
    ) : this(setId, workoutExerciseId, setNumber, weight, reps, isCompleted, UUID.randomUUID().toString())
}
