package com.example.workoutapp.data.template.texercise

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TemplateExerciseRepository @Inject constructor(
    private val templateExerciseDao: TemplateExerciseDao
) {
    suspend fun insertTemplateExercise(templateExercise: TemplateExercise) {
        templateExerciseDao.insertTemplateExercise(templateExercise)
    }

    fun getTemplateExercisesByTemplateId(templateId: Long): Flow<List<TemplateExercise>> {
        return templateExerciseDao.getTemplateExercisesByTemplateId(templateId)
    }

    suspend fun deleteTemplateExercise(templateExerciseId: Long) {
        templateExerciseDao.deleteTemplateExercise(templateExerciseId)
    }
}