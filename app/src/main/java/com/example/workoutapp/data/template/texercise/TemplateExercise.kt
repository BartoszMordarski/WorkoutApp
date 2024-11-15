package com.example.workoutapp.data.template.texercise

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workoutapp.data.exercise.Exercise

@Entity(
    tableName = "template_exercises",
    foreignKeys = [ForeignKey(
        entity = Exercise::class,
        parentColumns = ["id"],
        childColumns = ["exerciseId"],
        onDelete = ForeignKey.CASCADE
    )]
)data class TemplateExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val templateId: Long,
    val exerciseId: Long,
    val numberOfSets: Int
)
