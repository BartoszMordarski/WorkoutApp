package com.example.workoutapp.data.activeworkout

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.set.SetDetailRepository
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExerciseRepository
import com.example.workoutapp.data.activeworkout.workout.Workout
import com.example.workoutapp.data.activeworkout.workout.WorkoutRepository
import com.example.workoutapp.data.exercise.ExerciseRepository
import com.example.workoutapp.data.template.texercise.TemplateExercise
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplate
import com.example.workoutapp.data.template.wtemplate.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository,
    private val setDetailRepository: SetDetailRepository,
    private val templateRepository: WorkoutTemplateRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _workoutName = MutableStateFlow("New workout")
    val workoutName: StateFlow<String> get() = _workoutName

    private val _exercises = MutableStateFlow<List<WorkoutExercise>>(emptyList())
    val exercises: StateFlow<List<WorkoutExercise>> get() = _exercises

    private val _setsInProgress = MutableStateFlow<Map<Long, List<SetDetail>>>(emptyMap())
    val setsInProgress: StateFlow<Map<Long, List<SetDetail>>> get() = _setsInProgress

    private var _startTime = System.currentTimeMillis()

    private val _time = MutableStateFlow(0L)
    val time: StateFlow<Long> get() = _time

    private var _isTemplateWorkout = MutableStateFlow(false)
    val isTemplateWorkout: StateFlow<Boolean> get() = _isTemplateWorkout

    private var _originalTemplateId = MutableStateFlow<Long?>(null)
    val originalTemplateId: StateFlow<Long?> get() = _originalTemplateId

    private val _dialogState = MutableStateFlow<DialogType?>(null)
    val dialogState: StateFlow<DialogType?> get() = _dialogState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _time.value = System.currentTimeMillis() - _startTime
            }
        }
    }

    private var _areTemplateExercisesLoaded = MutableStateFlow(false)

    fun loadTemplateExercises(templateId: Long) {
        if (!_areTemplateExercisesLoaded.value) {
            _areTemplateExercisesLoaded.value = true
            _isLoading.value = true

            viewModelScope.launch {

                _isTemplateWorkout.value = true
                _originalTemplateId.value = templateId

                templateRepository.getTemplateWithExercisesById(templateId)
                    .collect { templateWithExercises ->
                        updateWorkoutName(templateWithExercises.template.templateName)

                        for (templateExercise in templateWithExercises.exercises) {
                            val newWorkoutExercise = WorkoutExercise(
                                workoutExerciseId = _exercises.value.size.toLong() + 1,
                                workoutId = 0L,
                                exerciseId = templateExercise.exerciseId,
                                exerciseName = exerciseRepository.getExerciseById(templateExercise.exerciseId)
                                    .first().name
                            )

                            addExercise(newWorkoutExercise)

                            repeat(templateExercise.numberOfSets) {
                                addSetToWorkoutExercise(
                                    newWorkoutExercise.workoutExerciseId,
                                    newWorkoutExercise.exerciseId
                                )
                            }
                        }
                        _isLoading.value = false
                    }
            }
        }
    }


    fun updateWorkoutName(newName: String) {
        _workoutName.value = newName
    }

    fun markSetAsCompleted(workoutExerciseId: Long, setUUID: String) {
        val currentSets = _setsInProgress.value.toMutableMap()
        val existingSets = currentSets[workoutExerciseId]?.toMutableList() ?: return

        val setToUpdate = existingSets.find { it.setUUID == setUUID }
        setToUpdate?.isCompleted = true

        _setsInProgress.value = currentSets
    }


    fun addExercise(exercise: WorkoutExercise) {
        if (_exercises.value.none { it.exerciseId == exercise.exerciseId }) {
            _exercises.value += exercise

            val currentSets = _setsInProgress.value.toMutableMap()

            if (currentSets[exercise.workoutExerciseId] == null) {
                currentSets[exercise.workoutExerciseId] = emptyList()
            }
            _setsInProgress.value = currentSets
        }
    }

    fun deleteExerciseFromWorkout(workoutExerciseId: Long) {
        viewModelScope.launch {
            _exercises.value =
                _exercises.value.filterNot { it.workoutExerciseId == workoutExerciseId }
            _setsInProgress.value = _setsInProgress.value.filterKeys { it != workoutExerciseId }
        }
    }


    private suspend fun getPreviousSetDetails(exerciseId: Long): Pair<List<SetDetail>, String?> {
        val workoutExerciseWithSets = workoutExerciseRepository.getLastWorkoutExerciseWithSets(exerciseId)
        val sets = workoutExerciseWithSets.firstOrNull()?.sets ?: emptyList()
        val difficulty = workoutExerciseWithSets.firstOrNull()?.workoutExercise?.difficulty

        return sets to difficulty
    }

    suspend fun addSetToWorkoutExercise(workoutExerciseId: Long, exerciseId: Long) {
        val currentSets = _setsInProgress.value.toMutableMap()
        val existingSets = currentSets[workoutExerciseId]?.toMutableList() ?: mutableListOf()
        val setNumber = existingSets.size + 1

        val (previousSets, difficulty) = getPreviousSetDetails(exerciseId)

        val (previousWeight, previousReps) = if (setNumber <= previousSets.size) {
            val prevSet = previousSets[setNumber - 1]
            prevSet.weight to prevSet.reps
        } else {
            null to null
        }

        val lastSetInCurrentWorkout = existingSets.lastOrNull()

        val newWeight: Float
        var newReps: Int

        if (previousReps != null && previousReps != 0) {
            newReps = when (difficulty) {
                "Easy" -> previousReps + 2
                "Medium" -> previousReps + 1
                "Hard" -> previousReps - 1
                else -> previousReps
            }

            if (previousReps < 12) {
                newWeight = previousWeight ?: 0f
            } else {
                newWeight = (previousWeight ?: 0f) + 5f
                newReps = 8
            }
        } else if (lastSetInCurrentWorkout != null && lastSetInCurrentWorkout.reps != 0) {
            newWeight = lastSetInCurrentWorkout.weight
            newReps = lastSetInCurrentWorkout.reps
        } else {
            newWeight = 0f
            newReps = 0
        }

        val newSet = SetDetail(
            workoutExerciseId = workoutExerciseId,
            setNumber = setNumber,
            weight = newWeight,
            reps = newReps,
            previousWeight = previousWeight,
            previousReps = previousReps
        )

        existingSets.add(newSet)
        currentSets[workoutExerciseId] = existingSets
        _setsInProgress.value = currentSets
    }


    fun removeSetFromWorkoutExercise(workoutExerciseId: Long, setDetail: SetDetail) {
        val currentSets = _setsInProgress.value.toMutableMap()
        val updatedSets = currentSets[workoutExerciseId]?.toMutableList() ?: return

        updatedSets.remove(setDetail)
        updatedSets.forEachIndexed { index, set -> set.setNumber = index + 1 }

        currentSets[workoutExerciseId] = updatedSets
        _setsInProgress.value = currentSets.toMap()
    }

    fun saveWorkout(difficulty: String?) {
        viewModelScope.launch {
            val workout = Workout(
                workoutName = _workoutName.value,
                workoutDate = getCurrentDate(),
                duration = calculateDuration(),
                isTemplate = false
            )
            val insertedWorkoutId = workoutRepository.insertWorkout(workout)

            _exercises.value.forEach { exercise ->
                val workoutExercise =
                    exercise.copy(
                        workoutId = insertedWorkoutId,
                        workoutExerciseId = 0L,
                        difficulty = difficulty
                    )
                val insertedWorkoutExerciseId =
                    workoutExerciseRepository.insertWorkoutExercise(workoutExercise)

                _setsInProgress.value[exercise.workoutExerciseId]?.forEach { setDetail ->
                    setDetailRepository.insertSetDetail(
                        setDetail.copy(workoutExerciseId = insertedWorkoutExerciseId)
                    )
                }
            }
        }
    }


    private suspend fun isWorkoutModified(): Boolean {
        val templateExercises = getTemplateExercises()
        val currentExercises = _exercises.value

        if (templateExercises.size != currentExercises.size) return true

        for (exercise in currentExercises) {
            val templateExercise = templateExercises.find { it.exerciseId == exercise.exerciseId }

            if (templateExercise == null) return true

            val currentSets = _setsInProgress.value[exercise.workoutExerciseId]?.size ?: 0
            if (currentSets != templateExercise.numberOfSets) {
                return true
            }
        }
        return false
    }

    fun saveAsNewTemplate() {
        viewModelScope.launch {
            val newTemplate = WorkoutTemplate(
                templateName = _workoutName.value,
                isDefault = false
            )
            val insertedTemplateId = templateRepository.insertTemplate(newTemplate)

            _exercises.value.forEach { exercise ->
                val templateExercise = TemplateExercise(
                    templateId = insertedTemplateId,
                    exerciseId = exercise.exerciseId,
                    numberOfSets = _setsInProgress.value[exercise.workoutExerciseId]?.size ?: 0
                )
                templateRepository.insertTemplateExercise(templateExercise)
            }
        }
    }

    fun updateExistingTemplate(templateId: Long) {
        viewModelScope.launch {

            val existingTemplate = templateRepository.getTemplateById(templateId).first()

            if (existingTemplate.isDefault) {
                saveAsNewTemplate()
            } else {
                val updatedTemplate = WorkoutTemplate(
                    templateId = templateId,
                    templateName = _workoutName.value,
                    isDefault = false
                )
                templateRepository.updateTemplate(updatedTemplate)

                templateRepository.deleteTemplateExercisesByTemplateId(templateId)

                _exercises.value.forEach { exercise ->
                    val templateExercise = TemplateExercise(
                        templateId = templateId,
                        exerciseId = exercise.exerciseId,
                        numberOfSets = _setsInProgress.value[exercise.workoutExerciseId]?.size ?: 0
                    )
                    templateRepository.insertTemplateExercise(templateExercise)
                }
            }
        }
    }


    private suspend fun getTemplateExercises(): List<TemplateExercise> {
        return templateRepository.getExercisesByTemplateId(_originalTemplateId.value ?: 0L)
    }

    private fun checkAndShowDialog() {
        viewModelScope.launch {
            if (isTemplateWorkout.value && isWorkoutModified()) {
                _dialogState.value = DialogType.DialogForTemplateChangedWorkout
            } else if (isTemplateWorkout.value && !isWorkoutModified()) {
                _dialogState.value = DialogType.DialogForTemplateUnchangedWorkout
            } else if (!isTemplateWorkout.value) {
                _dialogState.value = DialogType.DialogForEmptyWorkout
            } else {
                _dialogState.value = null
            }
        }
    }

    fun dismissDialog() {
        _dialogState.value = null
    }

    fun finishWorkout() {
        viewModelScope.launch {
            checkAndShowDialog()
        }
    }

    fun cancelWorkout() {
        _workoutName.value = "New workout"
        _exercises.value = emptyList()
        _setsInProgress.value = emptyMap()
        _time.value = 0L
        _startTime = System.currentTimeMillis()
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    @SuppressLint("DefaultLocale")
    private fun calculateDuration(): String {
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - _startTime
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(totalTime),
            TimeUnit.MILLISECONDS.toMinutes(totalTime) % 60,
            TimeUnit.MILLISECONDS.toSeconds(totalTime) % 60
        )
    }
}
