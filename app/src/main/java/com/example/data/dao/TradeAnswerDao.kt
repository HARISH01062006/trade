package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TradeAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeAnswerDao {
    @Query("SELECT * FROM trade_answers WHERE tradeCheckId = :tradeCheckId")
    fun getAnswersForTrade(tradeCheckId: Long): Flow<List<TradeAnswerEntity>>

    @Query("SELECT * FROM trade_answers WHERE tradeCheckId = :tradeCheckId")
    suspend fun getAnswersListForTrade(tradeCheckId: Long): List<TradeAnswerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<TradeAnswerEntity>)

    @Query("DELETE FROM trade_answers WHERE tradeCheckId = :tradeCheckId")
    suspend fun deleteByTradeCheckId(tradeCheckId: Long)
}
