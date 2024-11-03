package com.example.workoutapp.data.activeworkout.set

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetDetailRepository @Inject constructor (private val setDetailDao: SetDetailDao) {

    suspend fun insertSetDetail(setDetail: SetDetail) {
        setDetailDao.insertSetDetail(setDetail)
    }

    fun getSetsByWorkoutExerciseId(workoutExerciseId: Long): Flow<List<SetDetail>> {
        return setDetailDao.getSetsByWorkoutExerciseId(workoutExerciseId)
    }

    fun getNumberOfAllSets(): Flow<Int> {
        return setDetailDao.getNumberOfAllSets()
    }

    suspend fun deleteSetsByWorkoutExerciseId(workoutExerciseId: Long) {
        setDetailDao.deleteSetsByWorkoutExerciseId(workoutExerciseId)
    }
}