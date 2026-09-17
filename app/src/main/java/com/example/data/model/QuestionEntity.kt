package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val category: String, // Strategy, Technical Analysis, Risk Management, Psychology, Market Conditions, Trade Management
    val weight: Int = 10,
    val yesScore: Int = 10,
    val noScore: Int = 0,
    val isActive: Boolean = true,
    val orderIndex: Int = 0
)
