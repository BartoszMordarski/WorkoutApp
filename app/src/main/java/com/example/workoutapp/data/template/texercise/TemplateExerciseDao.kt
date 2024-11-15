package com.example.workoutapp.data.template.texercise

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercise(templateExercise: TemplateExercise)

    @Transaction
    @Query("SELECT * FROM template_exercises WHERE templateId = :templateId")
    fun getTemplateExercisesByTemplateId(templateId: Long): Flow<List<TemplateExercise>>

    @Query("DELETE FROM template_exercises WHERE id = :templateExerciseId")
    suspend fun deleteTemplateExercise(templateExerciseId: Long)
}