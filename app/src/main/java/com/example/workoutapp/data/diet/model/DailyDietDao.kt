package com.example.workoutapp.data.diet.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyDietDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietItem(dailyDiet: DailyDietItem)

    @Query("SELECT * FROM daily_diet WHERE dateAdded = :currentDate")
    fun getDietItemsForDate(currentDate: LocalDate): Flow<List<DailyDietItem>>

    @Query("SELECT * FROM daily_diet ORDER BY dateAdded DESC")
    fun getAllDietHistory(): Flow<List<DailyDietItem>>

    @Delete
    suspend fun deleteDietItem(dailyDiet: DailyDietItem)
}