package com.example.workoutapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.set.SetDetailDao
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExerciseDao
import com.example.workoutapp.data.activeworkout.workout.Workout
import com.example.workoutapp.data.activeworkout.workout.WorkoutDao
import com.example.workoutapp.data.diet.model.DailyDietDao
import com.example.workoutapp.data.diet.model.DailyDietItem
import com.example.workoutapp.data.exercise.Exercise
import com.example.workoutapp.data.exercise.ExerciseDao
import com.example.workoutapp.data.template.texercise.TemplateExercise
import com.example.workoutapp.data.template.texercise.TemplateExerciseDao
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplate
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplateDao

@Database(
    entities = [
        Exercise::class,
        Workout::class,
        WorkoutExercise::class,
        SetDetail::class,
        WorkoutTemplate::class,
        TemplateExercise::class,
        DailyDietItem::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun setDetailDao(): SetDetailDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun templateExerciseDao(): TemplateExerciseDao
    abstract fun dailyDietDao(): DailyDietDao

}