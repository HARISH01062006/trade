package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_checklist")
data class DailyChecklistEntity(
    @PrimaryKey
    val dateString: String, // e.g. "2026-09-17"
    val marketConditionsChecked: Boolean = false,
    val economicCalendarChecked: Boolean = false,
    val riskLimitDefined: Boolean = false,
    val strategyReviewed: Boolean = false,
    val previousMistakesReviewed: Boolean = false,
    val emotionalStateConfirmed: Boolean = false,
    val maxTradesSet: Boolean = false,
    val maxTradesLimit: Int = 3,
    val dailyRiskPercentage: Double = 2.0
) {
    val completionPercentage: Int
        get() {
            val items = listOf(
                marketConditionsChecked,
                economicCalendarChecked,
                riskLimitDefined,
                strategyReviewed,
                previousMistakesReviewed,
                emotionalStateConfirmed,
                maxTradesSet
            )
            val checkedCount = items.count { it }
            return ((checkedCount.toFloat() / items.size) * 100).toInt()
        }
}
