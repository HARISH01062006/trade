package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trade_answers")
data class TradeAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tradeCheckId: Long,
    val questionId: Long,
    val questionText: String,
    val category: String,
    val answer: Boolean, // true = YES, false = NO
    val weight: Int
)
