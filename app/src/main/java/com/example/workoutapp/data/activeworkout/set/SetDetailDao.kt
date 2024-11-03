package com.example.workoutapp.data.activeworkout.set

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetDetail(setDetail: SetDetail)

    @Query("SELECT * FROM set_details WHERE workoutExerciseId = :workoutExerciseId")
    fun getSetsByWorkoutExerciseId(workoutExerciseId: Long): Flow<List<SetDetail>>

    @Delete
    suspend fun deleteSetDetail(setDetail: SetDetail)

    @Query("DELETE FROM set_details WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteSetsByWorkoutExerciseId(workoutExerciseId: Long)

    @Query("SELECT COUNT(*) FROM set_details")
    fun getNumberOfAllSets(): Flow<Int>
}