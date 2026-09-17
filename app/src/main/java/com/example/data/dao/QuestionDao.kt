package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY orderIndex ASC, id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY orderIndex ASC, id ASC")
    fun getActiveQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY orderIndex ASC, id ASC")
    suspend fun getActiveQuestionsList(): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("UPDATE questions SET isActive = :isActive WHERE id = :id")
    suspend fun setQuestionActive(id: Long, isActive: Boolean)
}
