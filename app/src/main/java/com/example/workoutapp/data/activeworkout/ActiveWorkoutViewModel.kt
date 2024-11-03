package com.example.workoutapp.data.activeworkout

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.activeworkout.set.SetDetail
import com.example.workoutapp.data.activeworkout.set.SetDetailRepository
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExercise
import com.example.workoutapp.data.activeworkout.wexercise.WorkoutExerciseRepository
import com.example.workoutapp.data.activeworkout.workout.Workout
import com.example.workoutapp.data.activeworkout.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
    private val setDetailRepository: SetDetailRepository
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

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _time.value = System.currentTimeMillis() - _startTime
            }
        }
    }

    fun updateWorkoutName(newName: String) {
        _workoutName.value = newName
    }

    fun markSetAsCompleted(workoutExerciseId: Long, setNumber: Int) {
        val currentSets = _setsInProgress.value.toMutableMap()
        val existingSets = currentSets[workoutExerciseId]?.toMutableList() ?: return

        val setToUpdate = existingSets.find { it.setNumber == setNumber }
        setToUpdate?.isCompleted = true

        _setsInProgress.value = currentSets
    }


    fun addExercise(exercise: WorkoutExercise) {
        if (_exercises.value.none { it.exerciseId == exercise.exerciseId }) {
            _exercises.value = _exercises.value + exercise

            val currentSets = _setsInProgress.value.toMutableMap()

            if (currentSets[exercise.workoutExerciseId] == null) {
                currentSets[exercise.workoutExerciseId] = emptyList()
            }
            _setsInProgress.value = currentSets
        }
    }

    fun deleteExerciseFromWorkout(workoutExerciseId: Long) {
        viewModelScope.launch {
            setDetailRepository.deleteSetsByWorkoutExerciseId(workoutExerciseId)
            workoutExerciseRepository.deleteExercise(workoutExerciseId)

            _exercises.value = _exercises.value.filterNot { it.workoutExerciseId == workoutExerciseId }
            _setsInProgress.value = _setsInProgress.value.filterKeys { it != workoutExerciseId }
        }
    }

    fun addSetToWorkoutExercise(workoutExerciseId: Long) {
            val currentSets = _setsInProgress.value.toMutableMap()

            val existingSets = currentSets[workoutExerciseId]?.toMutableList() ?: mutableListOf()
            val setNumber = existingSets.size + 1

            val newSet = SetDetail(
                workoutExerciseId = workoutExerciseId,
                setNumber = setNumber,
                weight = 0f,
                reps = 0
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
        _setsInProgress.value = currentSets
    }



    fun finishWorkout() {
        viewModelScope.launch {
            val workout = Workout(
                workoutName = _workoutName.value,
                workoutDate = getCurrentDate(),
                duration = calculateDuration(),
                isTemplate = false
            )
            val insertedWorkoutId = workoutRepository.insertWorkout(workout)

            _exercises.value.forEach { exercise ->
                val workoutExercise = exercise.copy(workoutId = insertedWorkoutId)
                val insertedWorkoutExerciseId = workoutExerciseRepository.insertWorkoutExercise(workoutExercise)

                _setsInProgress.value[exercise.workoutExerciseId]?.forEach { setDetail ->
                    setDetailRepository.insertSetDetail(
                        setDetail.copy(workoutExerciseId = insertedWorkoutExerciseId)
                    )
                }
            }
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
