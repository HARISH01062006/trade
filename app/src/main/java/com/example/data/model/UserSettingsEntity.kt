package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val defaultRiskPercentage: Double = 1.0,
    val defaultAccountBalance: Double = 10000.0,
    val defaultMarket: String = "Forex",
    val defaultStrategy: String = "Breakout & Retest",
    val isDarkMode: Boolean = false,
    val dailyReminderEnabled: Boolean = true,
    val journalReminderEnabled: Boolean = true,
    val currencySymbol: String = "$"
)
