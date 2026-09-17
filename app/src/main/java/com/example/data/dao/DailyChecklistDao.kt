package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyChecklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChecklistDao {
    @Query("SELECT * FROM daily_checklist WHERE dateString = :dateString LIMIT 1")
    fun getChecklistForDate(dateString: String): Flow<DailyChecklistEntity?>

    @Query("SELECT * FROM daily_checklist WHERE dateString = :dateString LIMIT 1")
    suspend fun getChecklistEntityForDate(dateString: String): DailyChecklistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateChecklist(checklist: DailyChecklistEntity)
}
