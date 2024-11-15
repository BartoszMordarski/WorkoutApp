package com.example.workoutapp.data.template.wtemplate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_templates")
data class WorkoutTemplate(
    @PrimaryKey(autoGenerate = true)
    val templateId: Long = 0L,
    val templateName: String,
    val isDefault: Boolean = false
)