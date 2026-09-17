package com.example.data.repository

import com.example.data.database.TradeScoreDatabase
import com.example.data.model.DailyChecklistEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.TradeAnswerEntity
import com.example.data.model.TradeCheckEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserSettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TradeScoreRepository(
    private val database: TradeScoreDatabase
) {
    private val tradeCheckDao = database.tradeCheckDao()
    private val tradeAnswerDao = database.tradeAnswerDao()
    private val questionDao = database.questionDao()
    private val dailyChecklistDao = database.dailyChecklistDao()
    private val userDao = database.userDao()

    val allTradeChecks: Flow<List<TradeCheckEntity>> = tradeCheckDao.getAllTradeChecks()
    val journalEntries: Flow<List<TradeCheckEntity>> = tradeCheckDao.getJournalEntries()
    val recentTradeChecks: Flow<List<TradeCheckEntity>> = tradeCheckDao.getRecentTradeChecks()
    val activeQuestions: Flow<List<QuestionEntity>> = questionDao.getActiveQuestions()
    val allQuestions: Flow<List<QuestionEntity>> = questionDao.getAllQuestions()
    val user: Flow<UserEntity?> = userDao.getUser()
    val settings: Flow<UserSettingsEntity?> = userDao.getSettings()

    fun getDailyChecklist(dateString: String): Flow<DailyChecklistEntity?> {
        return dailyChecklistDao.getChecklistForDate(dateString)
    }

    fun getAnswersForTrade(tradeCheckId: Long): Flow<List<TradeAnswerEntity>> {
        return tradeAnswerDao.getAnswersForTrade(tradeCheckId)
    }

    suspend fun getTradeCheckById(id: Long): TradeCheckEntity? {
        return tradeCheckDao.getTradeCheckById(id)
    }

    suspend fun getActiveQuestionsList(): List<QuestionEntity> {
        return questionDao.getActiveQuestionsList()
    }

    suspend fun saveTradeAssessment(
        tradeCheck: TradeCheckEntity,
        answers: List<TradeAnswerEntity>
    ): Long = withContext(Dispatchers.IO) {
        val insertedId = tradeCheckDao.insertTradeCheck(tradeCheck)
        val answersWithTradeId = answers.map { it.copy(tradeCheckId = insertedId) }
        tradeAnswerDao.insertAnswers(answersWithTradeId)
        insertedId
    }

    suspend fun updateTradeCheck(tradeCheck: TradeCheckEntity) = withContext(Dispatchers.IO) {
        tradeCheckDao.updateTradeCheck(tradeCheck)
    }

    suspend fun deleteTradeCheck(id: Long) = withContext(Dispatchers.IO) {
        tradeAnswerDao.deleteByTradeCheckId(id)
        tradeCheckDao.deleteById(id)
    }

    suspend fun insertQuestion(question: QuestionEntity): Long = withContext(Dispatchers.IO) {
        questionDao.insertQuestion(question)
    }

    suspend fun updateQuestion(question: QuestionEntity) = withContext(Dispatchers.IO) {
        questionDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: QuestionEntity) = withContext(Dispatchers.IO) {
        questionDao.deleteQuestion(question)
    }

    suspend fun setQuestionActive(id: Long, isActive: Boolean) = withContext(Dispatchers.IO) {
        questionDao.setQuestionActive(id, isActive)
    }

    suspend fun saveDailyChecklist(checklist: DailyChecklistEntity) = withContext(Dispatchers.IO) {
        dailyChecklistDao.insertOrUpdateChecklist(checklist)
    }

    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    suspend fun updateSettings(settings: UserSettingsEntity) = withContext(Dispatchers.IO) {
        userDao.updateSettings(settings)
    }

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        if (questionDao.getCount() == 0) {
            TradeScoreDatabase.populateInitialData(database)
        }
    }
}
