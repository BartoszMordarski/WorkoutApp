package com.example.workoutapp.data.template.wtemplate

import androidx.room.*
import com.example.workoutapp.data.template.TemplateWithExercises
import com.example.workoutapp.data.template.texercise.TemplateExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: WorkoutTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercises(templateExercises: List<TemplateExercise>)

    @Query("SELECT * FROM workout_templates")
    fun getAllTemplates(): Flow<List<TemplateWithExercises>>

    @Query("SELECT * FROM workout_templates WHERE templateId = :templateId")
    fun getTemplateWithExercisesById(templateId: Long): Flow<TemplateWithExercises>

    @Query("SELECT * FROM workout_templates WHERE templateId = :templateId")
    fun getTemplateById(templateId: Long): Flow<WorkoutTemplate>

    @Delete
    suspend fun deleteTemplate(template: WorkoutTemplate)

    @Delete
    suspend fun deleteTemplateExercises(exercises: List<TemplateExercise>)

    @Update
    suspend fun updateTemplate(template: WorkoutTemplate)

    @Query("SELECT * FROM template_exercises WHERE templateId = :templateId")
    suspend fun getExercisesByTemplateId(templateId: Long): List<TemplateExercise>

    @Transaction
    suspend fun insertTemplateWithExercises(template: WorkoutTemplate, exercises: List<TemplateExercise>) {
        val templateId = insertTemplate(template)
        val exercisesWithTemplateId = exercises.map { it.copy(templateId = templateId) }
        insertTemplateExercises(exercisesWithTemplateId)
    }
}
