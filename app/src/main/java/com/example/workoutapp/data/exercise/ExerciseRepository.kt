package com.example.workoutapp.data.exercise

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExerciseRepository @Inject constructor (private val exerciseDao: ExerciseDao) {

    suspend fun addDefaultExercises() {
        if (exerciseDao.getAllExercises().first().isEmpty()) {
            val defaultExercises = listOf(
                Exercise(
                    name = "Squat",
                    muscleGroup = "Legs",
                    description = "A foundational lower body exercise that strengthens the quadriceps, hamstrings, and glutes.",
                    instructions = "1. Stand with feet shoulder-width apart.\n" +
                            "2. Lower your body by bending your knees and pushing your hips back.\n" +
                            "3. Keep your chest up and back straight.\n" +
                            "4. Return to standing position.",
                    imageName = "squat"
                ),
                Exercise(
                    name = "Bench Press",
                    muscleGroup = "Chest",
                    description = "A compound upper body exercise that targets the chest, shoulders, and triceps.",
                    instructions = "1. Lie on a flat bench with your feet flat on the ground.\n" +
                            "2. Grip the barbell with hands slightly wider than shoulder-width.\n" +
                            "3. Lower the barbell to your chest.\n" +
                            "4. Press it back up until arms are fully extended.",
                    imageName = "bench_press"
                ),
                Exercise(
                    name = "Deadlift",
                    muscleGroup = "Back",
                    description = "A full-body strength exercise focusing on the posterior chain muscles.",
                    instructions = "1. Stand with feet hip-width apart, barbell over mid-foot.\n" +
                            "2. Grip the barbell just outside your knees.\n" +
                            "3. Keep your back straight and lift the barbell by extending your hips and knees.\n" +
                            "4. Lower the barbell back to the ground.",
                    imageName = "deadlift"
                ),
                Exercise(
                    name = "Overhead Press",
                    muscleGroup = "Shoulders",
                    description = "An upper body exercise that strengthens the shoulders and triceps.",
                    instructions = "1. Stand with feet shoulder-width apart, barbell at chest level.\n" +
                            "2. Press the barbell overhead until arms are fully extended.\n" +
                            "3. Lower the barbell back to chest level.",
                    imageName = "overhead_press"
                ),
                Exercise(
                    name = "Plank",
                    muscleGroup = "Core",
                    description = "A core stability exercise that strengthens the abs and lower back.",
                    instructions = "1. Lie face down and support your weight on your forearms and toes.\n" +
                            "2. Keep your body in a straight line from head to heels.\n" +
                            "3. Hold this position for the desired duration.",
                    imageName = "plank"
                ),
                Exercise(
                    name = "Pull-Up",
                    muscleGroup = "Back",
                    description = "An upper body exercise targeting the lats and biceps.",
                    instructions = "1. Grip the pull-up bar with hands slightly wider than shoulder-width.\n" +
                            "2. Pull your body up until your chin is above the bar.\n" +
                            "3. Lower yourself back down with control.",
                    imageName = "pull_up"
                ),
                Exercise(
                    name = "Bent Over Row",
                    muscleGroup = "Back",
                    description = "A compound exercise focusing on the middle and upper back.",
                    instructions = "1. Hold a barbell with a shoulder-width grip.\n" +
                            "2. Bend forward at the hips, keeping your back straight.\n" +
                            "3. Pull the barbell towards your abdomen.\n" +
                            "4. Lower it back down with control.",
                    imageName = "bent_over_row"
                ),
                Exercise(
                    name = "Lunge",
                    muscleGroup = "Legs",
                    description = "A unilateral lower body exercise targeting the quadriceps, glutes, and hamstrings.",
                    instructions = "1. Step forward with one leg and lower your hips.\n" +
                            "2. Keep your back straight and lower your back knee towards the ground.\n" +
                            "3. Push off the front foot to return to the starting position.\n" +
                            "4. Repeat on the other leg.",
                    imageName = "lunge"
                ),
                Exercise(
                    name = "Push-Up",
                    muscleGroup = "Chest",
                    description = "A classic upper body exercise that targets the chest, shoulders, and triceps.",
                    instructions = "1. Start in a high plank position with hands shoulder-width apart.\n" +
                            "2. Lower your chest towards the ground while keeping your body straight.\n" +
                            "3. Push back up to the starting position.",
                    imageName = "push_up"
                ),
                Exercise(
                    name = "Russian Twists",
                    muscleGroup = "Core",
                    description = "A rotational core exercise that strengthens the obliques.",
                    instructions = "1. Sit on the floor with your knees bent and feet slightly elevated.\n" +
                            "2. Hold a weight or clasp your hands together.\n" +
                            "3. Rotate your torso to one side, then the other, in a controlled motion.",
                    imageName = "russian_twists"
                ),
                Exercise(
                    name = "Shoulder Press",
                    muscleGroup = "Shoulders",
                    description = "A compound exercise that strengthens the deltoid muscles and triceps.",
                    instructions = "1. Sit on a bench with a backrest, holding a barbell or dumbbells at shoulder level.\n" +
                            "2. Press the weight overhead until your arms are fully extended.\n" +
                            "3. Slowly lower the weight back to shoulder level.\n" +
                            "4. Maintain a straight back and avoid arching your spine.",
                    imageName = "shoulder_press"
                ),
                Exercise(
                    name = "Bicep Curl",
                    muscleGroup = "Arms",
                    description = "An isolation exercise targeting the biceps for upper arm strength.",
                    instructions = "1. Stand with feet shoulder-width apart, holding a dumbbell in each hand.\n" +
                            "2. Keep your elbows close to your torso and palms facing forward.\n" +
                            "3. Curl the weights up towards your shoulders while contracting your biceps.\n" +
                            "4. Lower the weights back to the starting position in a controlled manner.",
                    imageName = "bicep_curl"
                ),
                Exercise(
                    name = "Tricep Dips",
                    muscleGroup = "Arms",
                    description = "A bodyweight exercise focusing on the triceps, shoulders, and chest.",
                    instructions = "1. Sit on the edge of a bench or chair and place your hands next to your hips.\n" +
                            "2. Slide your hips off the edge, supporting yourself with your arms.\n" +
                            "3. Lower your body by bending your elbows until your upper arms are parallel to the ground.\n" +
                            "4. Push through your palms to return to the starting position.",
                    imageName = "triceps_dips"
                ),
                Exercise(
                    name = "Lat Pulldown",
                    muscleGroup = "Back",
                    description = "A machine-based exercise targeting the latissimus dorsi and upper back muscles.",
                    instructions = "1. Sit at a lat pulldown machine and grip the bar with hands slightly wider than shoulder-width.\n" +
                            "2. Pull the bar down towards your chest while keeping your torso stationary.\n" +
                            "3. Slowly return the bar to the starting position.\n" +
                            "4. Avoid using momentum or leaning back excessively.",
                    imageName = "lat_pulldown"
                ),
                Exercise(
                    name = "Chest Fly",
                    muscleGroup = "Chest",
                    description = "An isolation exercise focusing on the pectoral muscles.",
                    instructions = "1. Lie on a flat bench holding a dumbbell in each hand with palms facing each other.\n" +
                            "2. Extend your arms above your chest with a slight bend in your elbows.\n" +
                            "3. Lower the weights out to the sides until your arms are parallel to the ground.\n" +
                            "4. Bring the weights back to the starting position by squeezing your chest muscles.",
                    imageName = "chest_fly"
                ),
                Exercise(
                    name = "Incline Bench Press",
                    muscleGroup = "Chest",
                    description = "A strength exercise targeting the upper portion of the chest and shoulders.",
                    instructions = "1. Lie on an incline bench set at a 30-45 degree angle, holding a barbell or dumbbells.\n" +
                            "2. Lower the barbell or dumbbells to your upper chest.\n" +
                            "3. Press the weight back up until your arms are fully extended.\n" +
                            "4. Keep your back pressed against the bench throughout the movement.",
                    imageName = "incline_bench_press"
                ),
                Exercise(
                    name = "Incline Dumbbell Press",
                    muscleGroup = "Chest",
                    description = "A variation of the bench press that emphasizes the upper chest and shoulders.",
                    instructions = "1. Lie on an incline bench holding a dumbbell in each hand at shoulder level.\n" +
                            "2. Press the dumbbells upwards until your arms are fully extended.\n" +
                            "3. Slowly lower the dumbbells back to shoulder level.\n" +
                            "4. Keep your movements controlled and your back pressed against the bench.",
                    imageName = "incline_dumbbell_press"
                ),
                Exercise(
                    name = "Leg Press",
                    muscleGroup = "Legs",
                    description = "A machine-based exercise targeting the quadriceps, hamstrings, and glutes.",
                    instructions = "1. Sit on the leg press machine and place your feet shoulder-width apart on the platform.\n" +
                            "2. Push the platform away from you by extending your legs.\n" +
                            "3. Slowly lower the platform back until your knees are at a 90-degree angle.\n" +
                            "4. Avoid locking your knees at the top of the movement.",
                    imageName = "leg_press"
                ),
                Exercise(
                    name = "Calf Raise",
                    muscleGroup = "Legs",
                    description = "An exercise focusing on the calves for lower leg strength.",
                    instructions = "1. Stand with your feet shoulder-width apart and your toes on an elevated surface.\n" +
                            "2. Raise your heels as high as possible by pushing through the balls of your feet.\n" +
                            "3. Hold the position for a moment, then slowly lower your heels back down.\n" +
                            "4. Keep your movements controlled to maximize muscle engagement.",
                    imageName = "calf_raise"
                ),
                Exercise(
                    name = "Lateral Raise",
                    muscleGroup = "Shoulders",
                    description = "An isolation exercise targeting the deltoid muscles.",
                    instructions = "1. Stand with a dumbbell in each hand, arms at your sides.\n" +
                            "2. Raise your arms out to the sides until they are parallel to the ground.\n" +
                            "3. Slowly lower your arms back to the starting position.\n" +
                            "4. Avoid using momentum to lift the weights.",
                    imageName = "lateral_raise"
                ),
                Exercise(
                    name = "Front Raise",
                    muscleGroup = "Shoulders",
                    description = "An exercise targeting the anterior deltoid muscles.",
                    instructions = "1. Stand holding a dumbbell in each hand with your arms at your sides.\n" +
                            "2. Lift one or both arms straight in front of you to shoulder height.\n" +
                            "3. Slowly lower your arms back to the starting position.\n" +
                            "4. Keep your back straight and avoid swinging the weights.",
                    imageName = "front_raise"
                ),
                Exercise(
                    name = "Arnold Press",
                    muscleGroup = "Shoulders",
                    description = "A multi-angle shoulder exercise named after Arnold Schwarzenegger.",
                    instructions = "1. Sit on a bench with a backrest, holding dumbbells at chest level with palms facing you.\n" +
                            "2. Rotate your hands outward as you press the dumbbells overhead.\n" +
                            "3. Reverse the movement to return to the starting position.\n" +
                            "4. Perform the exercise in a controlled and fluid motion.",
                    imageName = "arnold_press"
                ),
                Exercise(
                    name = "Burpee",
                    muscleGroup = "Full Body",
                    description = "A full-body exercise combining strength and cardio.",
                    instructions = "1. Start in a standing position.\n" +
                            "2. Drop into a squat and place your hands on the ground.\n" +
                            "3. Jump your feet back into a plank position.\n" +
                            "4. Perform a push-up, then jump your feet back to the squat position.\n" +
                            "5. Jump explosively into the air and repeat.",
                    imageName = "burpee"
                ),
                Exercise(
                    name = "Kettlebell Swing",
                    muscleGroup = "Full Body",
                    description = "An explosive movement targeting the hips, glutes, and core.",
                    instructions = "1. Stand with feet shoulder-width apart, holding a kettlebell with both hands.\n" +
                            "2. Hinge at your hips and swing the kettlebell between your legs.\n" +
                            "3. Drive through your hips to swing the kettlebell up to shoulder height.\n" +
                            "4. Allow the kettlebell to swing back down and repeat.",
                    imageName = "kettlebell_swing"
                ),
                Exercise(
                    name = "Mountain Climbers",
                    muscleGroup = "Full Body",
                    description = "A cardio and core exercise that mimics running in a plank position.",
                    instructions = "1. Start in a high plank position with your hands under your shoulders.\n" +
                            "2. Drive one knee towards your chest while keeping the other leg extended.\n" +
                            "3. Quickly switch legs, mimicking a running motion.\n" +
                            "4. Maintain a strong core and avoid letting your hips sag.",
                    imageName = "mountain_climbers"
                )
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


    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.editExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }

}