package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trade_checks")
data class TradeCheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val market: String,
    val direction: String, // BUY or SELL
    val entryPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val riskPercentage: Double = 1.0,
    val accountBalance: Double = 10000.0,
    val positionSize: Double = 0.0,
    val riskRewardRatio: Double = 0.0,
    val potentialRisk: Double = 0.0,
    val potentialReward: Double = 0.0,
    val tradingSession: String = "London",
    val timeframe: String = "15m",
    val strategy: String = "Breakout & Retest",
    val tradeReason: String = "",
    val notes: String = "",
    val score: Int = 0,
    val scoreCategory: String = "HIGH_QUALITY",
    val passedCount: Int = 0,
    val failedCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isSavedToJournal: Boolean = true,
    val isDemo: Boolean = false,

    // Journal fields
    val outcome: String = "PENDING", // PENDING, WIN, LOSS, BREAKEVEN
    val actualExitPrice: Double? = null,
    val realizedPnl: Double? = null,
    val emotion: String = "CALM",
    val whatWentRight: String = "",
    val whatWentWrong: String = "",
    val lessonLearned: String = ""
)
