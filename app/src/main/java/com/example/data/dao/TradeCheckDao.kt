package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TradeCheckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeCheckDao {
    @Query("SELECT * FROM trade_checks ORDER BY timestamp DESC")
    fun getAllTradeChecks(): Flow<List<TradeCheckEntity>>

    @Query("SELECT * FROM trade_checks WHERE isSavedToJournal = 1 ORDER BY timestamp DESC")
    fun getJournalEntries(): Flow<List<TradeCheckEntity>>

    @Query("SELECT * FROM trade_checks ORDER BY timestamp DESC LIMIT 5")
    fun getRecentTradeChecks(): Flow<List<TradeCheckEntity>>

    @Query("SELECT * FROM trade_checks WHERE id = :id")
    suspend fun getTradeCheckById(id: Long): TradeCheckEntity?

    @Query("SELECT * FROM trade_checks WHERE id = :id")
    fun getTradeCheckFlowById(id: Long): Flow<TradeCheckEntity?>

    @Query("SELECT COUNT(*) FROM trade_checks")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradeCheck(tradeCheck: TradeCheckEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradeChecks(tradeChecks: List<TradeCheckEntity>)

    @Update
    suspend fun updateTradeCheck(tradeCheck: TradeCheckEntity)

    @Delete
    suspend fun deleteTradeCheck(tradeCheck: TradeCheckEntity)

    @Query("DELETE FROM trade_checks WHERE id = :id")
    suspend fun deleteById(id: Long)
}
