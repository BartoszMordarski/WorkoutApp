package com.example.workoutapp

import android.app.Application
import androidx.room.Room
import com.example.workoutapp.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WorkoutApp: Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "workout_app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}