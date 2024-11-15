package com.example.workoutapp.data.hiltmodules

import com.example.workoutapp.data.AppDatabase
import com.example.workoutapp.data.template.texercise.TemplateExerciseDao
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplateModule {

    @Provides
    @Singleton
    fun provideTemplateDao(db: AppDatabase): WorkoutTemplateDao {
        return db.workoutTemplateDao()
    }

    @Provides
    @Singleton
    fun provideTemplateExerciseDao(db: AppDatabase): TemplateExerciseDao {
        return db.templateExerciseDao()
    }
}