package com.example.workoutapp.data.diet.model

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DailyDietRepository @Inject constructor(private val dailyDietDao: DailyDietDao) {


    fun getDietItemsForToday(): Flow<List<DailyDietItem>> {
        val today = LocalDate.now()
        return dailyDietDao.getDietItemsForDate(today)
    }

    fun getAllDietHistory(): Flow<List<DailyDietItem>> {
        return dailyDietDao.getAllDietHistory()
    }

    suspend fun addDietItem(dietItem: DailyDietItem) {
        dailyDietDao.insertDietItem(dietItem)
    }

    suspend fun deleteDietItem(dietItem: DailyDietItem) {
        dailyDietDao.deleteDietItem(dietItem)
    }

}