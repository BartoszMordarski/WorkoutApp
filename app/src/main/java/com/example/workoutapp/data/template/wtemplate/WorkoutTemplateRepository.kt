package com.example.workoutapp.data.template.wtemplate
import com.example.workoutapp.data.template.TemplateWithExercises
import com.example.workoutapp.data.template.texercise.TemplateExercise
import com.example.workoutapp.data.template.texercise.TemplateExerciseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WorkoutTemplateRepository @Inject constructor(
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val templateExerciseDao: TemplateExerciseDao
) {

    suspend fun addDefaultTemplates() {
        if (workoutTemplateDao.getAllTemplates().first().isEmpty()) {

            val defaultTemplates = listOf(
                WorkoutTemplate(templateName = "Full Body Routine", isDefault = true),
                WorkoutTemplate(templateName = "Upper Body Split", isDefault = true),
                WorkoutTemplate(templateName = "Leg Day Blast", isDefault = true)
            )

            val templateExercisesMap = mutableMapOf<Long, List<TemplateExercise>>()

            for (template in defaultTemplates) {
                val templateId = workoutTemplateDao.insertTemplate(template)

                val exercises = when (template.templateName) {
                    "Full Body Routine" -> listOf(
                        TemplateExercise(templateId = templateId, exerciseId = 1, numberOfSets = 3),
                        TemplateExercise(templateId = templateId, exerciseId = 3, numberOfSets = 3),
                        TemplateExercise(templateId = templateId, exerciseId = 5, numberOfSets = 4),
                        TemplateExercise(templateId = templateId, exerciseId = 7, numberOfSets = 4)
                    )
                    "Upper Body Split" -> listOf(
                        TemplateExercise(templateId = templateId, exerciseId = 2, numberOfSets = 4),
                        TemplateExercise(templateId = templateId, exerciseId = 4, numberOfSets = 3),
                        TemplateExercise(templateId = templateId, exerciseId = 6, numberOfSets = 3),
                        TemplateExercise(templateId = templateId, exerciseId = 8, numberOfSets = 4)
                    )
                    "Leg Day Blast" -> listOf(
                        TemplateExercise(templateId = templateId, exerciseId = 9, numberOfSets = 4),
                        TemplateExercise(templateId = templateId, exerciseId = 10, numberOfSets = 4),
                        TemplateExercise(templateId = templateId, exerciseId = 11, numberOfSets = 3),
                        TemplateExercise(templateId = templateId, exerciseId = 12, numberOfSets = 3)
                    )
                    else -> emptyList()
                }

                templateExercisesMap[templateId] = exercises
            }

            for ((templateId, exercises) in templateExercisesMap) {
                workoutTemplateDao.insertTemplateExercises(exercises)
            }
        }
    }

    suspend fun insertTemplate(template: WorkoutTemplate): Long {
        return workoutTemplateDao.insertTemplate(template)
    }

    suspend fun insertTemplateExercise(exercise: TemplateExercise) {
        templateExerciseDao.insertTemplateExercise(exercise)
    }

    suspend fun updateTemplate(template: WorkoutTemplate) {
        workoutTemplateDao.updateTemplate(template)
    }

    suspend fun addTemplate(template: WorkoutTemplate, exercises: List<TemplateExercise>) {
        workoutTemplateDao.insertTemplateWithExercises(template, exercises)
    }

    suspend fun getExercisesByTemplateId(templateId: Long): List<TemplateExercise> {
        return workoutTemplateDao.getExercisesByTemplateId(templateId)
    }

    fun getAllTemplates(): Flow<List<TemplateWithExercises>> {
        return workoutTemplateDao.getAllTemplates()
    }

    fun getTemplateById(templateId: Long): Flow<WorkoutTemplate> {
        return workoutTemplateDao.getTemplateById(templateId)
    }

    fun getTemplateWithExercisesById(templateId: Long): Flow<TemplateWithExercises> {
        return workoutTemplateDao.getTemplateWithExercisesById(templateId)
    }

    suspend fun deleteTemplate(template: WorkoutTemplate) {
        workoutTemplateDao.deleteTemplate(template)
    }

    suspend fun deleteTemplateExercisesByTemplateId(templateId: Long) {
        val exercises = workoutTemplateDao.getExercisesByTemplateId(templateId)
        workoutTemplateDao.deleteTemplateExercises(exercises)

    }
}