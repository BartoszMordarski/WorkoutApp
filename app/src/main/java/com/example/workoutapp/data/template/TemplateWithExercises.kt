package com.example.workoutapp.data.template
import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutapp.data.template.texercise.TemplateExercise
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplate

data class TemplateWithExercises(
    @Embedded
    val template: WorkoutTemplate,
    @Relation(
        entity = TemplateExercise::class,
        parentColumn = "templateId",
        entityColumn = "templateId"
    )
    val exercises: List<TemplateExercise>
)
