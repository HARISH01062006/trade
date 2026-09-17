package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = "trader_primary",
    val email: String = "trader@tradescore.io",
    val displayName: String = "Alex Rivers",
    val title: String = "Forex & Index Trader",
    val experienceLevel: String = "Intermediate (3+ Yrs)",
    val preferredMarket: String = "Forex & Crypto",
    val preferredStrategy: String = "Breakout & Retest",
    val disciplineScore: Int = 88,
    val avatarInitials: String = "AR",
    val isLoggedIn: Boolean = true
)
