package com.example.workoutapp.data.hiltmodules

import com.example.workoutapp.data.AppDatabase
import com.example.workoutapp.data.activeworkout.set.SetDetailDao
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExerciseDao
import com.example.workoutapp.data.activeworkout.workout.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutModule {

    @Provides
    @Singleton
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao {
        return db.workoutDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutExerciseDao(db: AppDatabase): WorkoutExerciseDao {
        return db.workoutExerciseDao()
    }

    @Provides
    @Singleton
    fun provideSetDetailDao(db: AppDatabase): SetDetailDao {
        return db.setDetailDao()
    }
}