package com.example.workoutapp

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE exercises ADD COLUMN isUserCreated INTEGER NOT NULL DEFAULT 0")
        }
    }

}