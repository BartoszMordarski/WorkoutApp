package com.example.workoutapp.data.exercise

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExerciseRepository @Inject constructor (private val exerciseDao: ExerciseDao) {

    suspend fun addDefaultExercises() {
        if(exerciseDao.getAllExercises().first().isEmpty()){
            val defaultExercises = listOf(
                Exercise(name = "Squat", muscleGroup = "Legs", description = "Lower body exercise."),
                Exercise(name = "Bench Press", muscleGroup = "Chest", description = "Upper body exercise."),
                Exercise(name = "Deadlift", muscleGroup = "Back", description = "Full body exercise."),
                Exercise(name = "Overhead Press", muscleGroup = "Shoulders", description = "Upper body exercise."),
                Exercise(name = "Shoulder Press", muscleGroup = "Shoulders", description = "Upper body exercise."),
                Exercise(name = "Plank", muscleGroup = "Core", description = "Core stability exercise."),
                Exercise(name = "Russian Twists", muscleGroup = "Core", description = "Core rotation exercise."),
                Exercise(name = "Leg Raises", muscleGroup = "Core", description = "Lower abs exercise."),

                // Arms
                Exercise(name = "Bicep Curl", muscleGroup = "Arms", description = "Upper arm strength exercise."),
                Exercise(name = "Tricep Dips", muscleGroup = "Arms", description = "Back of arms strength exercise."),
                Exercise(name = "Hammer Curl", muscleGroup = "Arms", description = "Bicep strength exercise with neutral grip."),

                // Back
                Exercise(name = "Pull-Up", muscleGroup = "Back", description = "Upper back and lats strength exercise."),
                Exercise(name = "Bent Over Row", muscleGroup = "Back", description = "Middle back exercise with weights."),
                Exercise(name = "Lat Pulldown", muscleGroup = "Back", description = "Latissimus dorsi strength exercise."),

                // Chest
                Exercise(name = "Push-Up", muscleGroup = "Chest", description = "Upper body exercise targeting the chest."),
                Exercise(name = "Chest Fly", muscleGroup = "Chest", description = "Chest isolation exercise with dumbbells."),
                Exercise(name = "Incline Bench Press", muscleGroup = "Chest", description = "Upper chest strength exercise."),

                // Legs
                Exercise(name = "Lunge", muscleGroup = "Legs", description = "Lower body exercise targeting quadriceps."),
                Exercise(name = "Leg Press", muscleGroup = "Legs", description = "Machine-based leg exercise."),
                Exercise(name = "Calf Raise", muscleGroup = "Legs", description = "Lower leg exercise focusing on calves."),

                // Shoulders
                Exercise(name = "Lateral Raise", muscleGroup = "Shoulders", description = "Deltoid isolation exercise."),
                Exercise(name = "Front Raise", muscleGroup = "Shoulders", description = "Anterior deltoid strength exercise."),
                Exercise(name = "Arnold Press", muscleGroup = "Shoulders", description = "Multi-angle shoulder press exercise."),

                // Full Body
                Exercise(name = "Burpee", muscleGroup = "Full Body", description = "Full body cardio and strength exercise."),
                Exercise(name = "Kettlebell Swing", muscleGroup = "Full Body", description = "Explosive full body movement."),
                Exercise(name = "Mountain Climbers", muscleGroup = "Full Body", description = "Core and full body cardio exercise.")
            )
            exerciseDao.addExercises(defaultExercises)
        }
    }

    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<Exercise>> {
        return exerciseDao.getExercisesByMuscleGroup(muscleGroup)
    }

    fun getExerciseById(id: Long): Flow<Exercise> {
        return exerciseDao.getExerciseById(id)
    }

}