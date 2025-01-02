package com.example.workoutapp.data.activeworkout.set

import javax.inject.Inject

class SetDetailRepository @Inject constructor (private val setDetailDao: SetDetailDao) {

    suspend fun insertSetDetail(setDetail: SetDetail) {
        setDetailDao.insertSetDetail(setDetail)
    }

}