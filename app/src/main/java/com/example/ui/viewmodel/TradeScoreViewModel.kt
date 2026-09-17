package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.TradeScoreDatabase
import com.example.data.model.DailyChecklistEntity
import com.example.data.model.Direction
import com.example.data.model.Market
import com.example.data.model.QuestionEntity
import com.example.data.model.ScoreCategory
import com.example.data.model.Strategy
import com.example.data.model.Timeframe
import com.example.data.model.TradeAnswerEntity
import com.example.data.model.TradeCheckEntity
import com.example.data.model.TradeOutcome
import com.example.data.model.TraderEmotion
import com.example.data.model.TradingSession
import com.example.data.model.UserEntity
import com.example.data.model.UserSettingsEntity
import com.example.data.repository.TradeScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class TradeScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TradeScoreRepository

    init {
        val db = TradeScoreDatabase.getDatabase(application, viewModelScope)
        repository = TradeScoreRepository(db)
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    val user: StateFlow<UserEntity?> = repository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val settings: StateFlow<UserSettingsEntity?> = repository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allTradeChecks: StateFlow<List<TradeCheckEntity>> = repository.allTradeChecks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val journalEntries: StateFlow<List<TradeCheckEntity>> = repository.journalEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentTradeChecks: StateFlow<List<TradeCheckEntity>> = repository.recentTradeChecks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allQuestions: StateFlow<List<QuestionEntity>> = repository.allQuestions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeQuestions: StateFlow<List<QuestionEntity>> = repository.activeQuestions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val todayDateString = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val todayChecklist: StateFlow<DailyChecklistEntity?> = repository.getDailyChecklist(todayDateString).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // ==========================================
    // TRADE DRAFT STATE (Form Inputs)
    // ==========================================
    var draftSymbol = MutableStateFlow("EURUSD")
        private set
    var draftMarket = MutableStateFlow(Market.FOREX)
        private set
    var draftDirection = MutableStateFlow(Direction.BUY)
        private set
    var draftEntryPrice = MutableStateFlow("1.08500")
        private set
    var draftStopLoss = MutableStateFlow("1.08200")
        private set
    var draftTakeProfit = MutableStateFlow("1.09250")
        private set
    var draftRiskPercentage = MutableStateFlow(1.0)
        private set
    var draftAccountBalance = MutableStateFlow(25000.0)
        private set
    var draftTradingSession = MutableStateFlow(TradingSession.LONDON)
        private set
    var draftTimeframe = MutableStateFlow(Timeframe.M15)
        private set
    var draftStrategy = MutableStateFlow(Strategy.BREAKOUT)
        private set
    var draftTradeReason = MutableStateFlow("London session breakout of resistance with clean 15m retest and order flow support.")
        private set
    var draftNotes = MutableStateFlow("High probability setup. 4H chart in strong uptrend.")
        private set

    fun updateDraftSymbol(v: String) { draftSymbol.value = v.uppercase().trim() }
    fun updateDraftMarket(v: Market) { draftMarket.value = v }
    fun updateDraftDirection(v: Direction) { draftDirection.value = v }
    fun updateDraftEntryPrice(v: String) { draftEntryPrice.value = v }
    fun updateDraftStopLoss(v: String) { draftStopLoss.value = v }
    fun updateDraftTakeProfit(v: String) { draftTakeProfit.value = v }
    fun updateDraftRiskPercentage(v: Double) { draftRiskPercentage.value = v }
    fun updateDraftAccountBalance(v: Double) { draftAccountBalance.value = v }
    fun updateDraftSession(v: TradingSession) { draftTradingSession.value = v }
    fun updateDraftTimeframe(v: Timeframe) { draftTimeframe.value = v }
    fun updateDraftStrategy(v: Strategy) { draftStrategy.value = v }
    fun updateDraftReason(v: String) { draftTradeReason.value = v }
    fun updateDraftNotes(v: String) { draftNotes.value = v }

    // Risk calculations
    fun calculateRiskRewardRatio(): Double {
        val entry = draftEntryPrice.value.toDoubleOrNull() ?: 0.0
        val sl = draftStopLoss.value.toDoubleOrNull() ?: 0.0
        val tp = draftTakeProfit.value.toDoubleOrNull() ?: 0.0
        val risk = abs(entry - sl)
        val reward = abs(tp - entry)
        return if (risk > 0.0) Math.round((reward / risk) * 100.0) / 100.0 else 0.0
    }

    fun calculatePotentialRisk(): Double {
        val balance = draftAccountBalance.value
        val riskPct = draftRiskPercentage.value
        return (balance * (riskPct / 100.0))
    }

    fun calculatePotentialReward(): Double {
        val riskAmount = calculatePotentialRisk()
        val rr = calculateRiskRewardRatio()
        return (riskAmount * rr)
    }

    fun calculatePositionSize(): Double {
        val entry = draftEntryPrice.value.toDoubleOrNull() ?: 0.0
        val sl = draftStopLoss.value.toDoubleOrNull() ?: 0.0
        val risk = abs(entry - sl)
        val riskAmount = calculatePotentialRisk()
        return if (risk > 0.0) Math.round((riskAmount / risk) * 100.0) / 100.0 else 0.0
    }

    // ==========================================
    // QUESTIONNAIRE STATE
    // ==========================================
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _answersMap = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val answersMap = _answersMap.asStateFlow()

    private val _lastResultTrade = MutableStateFlow<TradeCheckEntity?>(null)
    val lastResultTrade = _lastResultTrade.asStateFlow()

    private val _lastResultAnswers = MutableStateFlow<List<TradeAnswerEntity>>(emptyList())
    val lastResultAnswers = _lastResultAnswers.asStateFlow()

    fun startAssessment() {
        _currentQuestionIndex.value = 0
        _answersMap.value = emptyMap()
    }

    fun answerQuestion(questionId: Long, answer: Boolean) {
        val updated = _answersMap.value.toMutableMap()
        updated[questionId] = answer
        _answersMap.value = updated

        val questions = activeQuestions.value
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun goToPreviousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun finishAssessment(onFinished: (TradeCheckEntity) -> Unit) {
        viewModelScope.launch {
            val questions = activeQuestions.value
            val answers = _answersMap.value

            var totalPossibleWeight = 0
            var earnedWeight = 0
            var passedCount = 0
            var failedCount = 0

            val answerEntities = mutableListOf<TradeAnswerEntity>()

            questions.forEach { q ->
                val isYes = answers[q.id] ?: false
                totalPossibleWeight += q.weight
                if (isYes) {
                    earnedWeight += q.weight
                    passedCount++
                } else {
                    failedCount++
                }
                answerEntities.add(
                    TradeAnswerEntity(
                        tradeCheckId = 0,
                        questionId = q.id,
                        questionText = q.text,
                        category = q.category,
                        answer = isYes,
                        weight = q.weight
                    )
                )
            }

            val finalScore = if (totalPossibleWeight > 0) {
                ((earnedWeight.toFloat() / totalPossibleWeight.toFloat()) * 100).toInt().coerceIn(0, 100)
            } else 50

            val category = when {
                finalScore >= 80 -> "HIGH_QUALITY"
                finalScore >= 60 -> "MODERATE"
                finalScore >= 40 -> "WEAK"
                finalScore >= 20 -> "VERY_WEAK"
                else -> "DANGER"
            }

            val entry = draftEntryPrice.value.toDoubleOrNull() ?: 1.0
            val sl = draftStopLoss.value.toDoubleOrNull() ?: 0.99
            val tp = draftTakeProfit.value.toDoubleOrNull() ?: 1.02

            val trade = TradeCheckEntity(
                symbol = draftSymbol.value.ifBlank { "SETUP" },
                market = draftMarket.value.displayName,
                direction = draftDirection.value.name,
                entryPrice = entry,
                stopLoss = sl,
                takeProfit = tp,
                riskPercentage = draftRiskPercentage.value,
                accountBalance = draftAccountBalance.value,
                positionSize = calculatePositionSize(),
                riskRewardRatio = calculateRiskRewardRatio(),
                potentialRisk = calculatePotentialRisk(),
                potentialReward = calculatePotentialReward(),
                tradingSession = draftTradingSession.value.displayName,
                timeframe = draftTimeframe.value.displayName,
                strategy = draftStrategy.value.displayName,
                tradeReason = draftTradeReason.value,
                notes = draftNotes.value,
                score = finalScore,
                scoreCategory = category,
                passedCount = passedCount,
                failedCount = failedCount,
                timestamp = System.currentTimeMillis(),
                isSavedToJournal = true,
                isDemo = false,
                outcome = "PENDING"
            )

            val insertedId = repository.saveTradeAssessment(trade, answerEntities)
            val savedTrade = trade.copy(id = insertedId)
            _lastResultTrade.value = savedTrade
            _lastResultAnswers.value = answerEntities.map { it.copy(tradeCheckId = insertedId) }
            onFinished(savedTrade)
        }
    }

    // ==========================================
    // JOURNAL OUTCOME UPDATES
    // ==========================================
    private val _selectedTrade = MutableStateFlow<TradeCheckEntity?>(null)
    val selectedTrade = _selectedTrade.asStateFlow()

    private val _selectedTradeAnswers = MutableStateFlow<List<TradeAnswerEntity>>(emptyList())
    val selectedTradeAnswers = _selectedTradeAnswers.asStateFlow()

    fun selectTradeForDetail(trade: TradeCheckEntity) {
        _selectedTrade.value = trade
        viewModelScope.launch {
            repository.getAnswersForTrade(trade.id).collect {
                _selectedTradeAnswers.value = it
            }
        }
    }

    fun updateJournalOutcome(
        tradeId: Long,
        outcome: String,
        actualExitPrice: Double?,
        pnl: Double?,
        emotion: String,
        whatWentRight: String,
        whatWentWrong: String,
        lessonLearned: String
    ) {
        viewModelScope.launch {
            val trade = repository.getTradeCheckById(tradeId) ?: return@launch
            val updated = trade.copy(
                outcome = outcome,
                actualExitPrice = actualExitPrice,
                realizedPnl = pnl,
                emotion = emotion,
                whatWentRight = whatWentRight,
                whatWentWrong = whatWentWrong,
                lessonLearned = lessonLearned
            )
            repository.updateTradeCheck(updated)
            _selectedTrade.value = updated
        }
    }

    fun deleteTradeCheck(id: Long) {
        viewModelScope.launch {
            repository.deleteTradeCheck(id)
            if (_selectedTrade.value?.id == id) {
                _selectedTrade.value = null
            }
        }
    }

    // ==========================================
    // DAILY CHECKLIST
    // ==========================================
    fun toggleDailyChecklistItem(
        itemType: String,
        currentValue: Boolean
    ) {
        viewModelScope.launch {
            val current = todayChecklist.value ?: DailyChecklistEntity(dateString = todayDateString)
            val updated = when (itemType) {
                "marketConditions" -> current.copy(marketConditionsChecked = !currentValue)
                "economicCalendar" -> current.copy(economicCalendarChecked = !currentValue)
                "riskLimit" -> current.copy(riskLimitDefined = !currentValue)
                "strategy" -> current.copy(strategyReviewed = !currentValue)
                "previousMistakes" -> current.copy(previousMistakesReviewed = !currentValue)
                "emotionalState" -> current.copy(emotionalStateConfirmed = !currentValue)
                "maxTrades" -> current.copy(maxTradesSet = !currentValue)
                else -> current
            }
            repository.saveDailyChecklist(updated)
        }
    }

    // ==========================================
    // QUESTION MANAGEMENT
    // ==========================================
    fun addQuestion(
        text: String,
        category: String,
        weight: Int
    ) {
        viewModelScope.launch {
            val newQ = QuestionEntity(
                text = text,
                category = category,
                weight = weight,
                yesScore = weight,
                noScore = 0,
                isActive = true,
                orderIndex = (allQuestions.value.maxOfOrNull { it.orderIndex } ?: 0) + 1
            )
            repository.insertQuestion(newQ)
        }
    }

    fun updateQuestion(question: QuestionEntity) {
        viewModelScope.launch {
            repository.updateQuestion(question)
        }
    }

    fun deleteQuestion(question: QuestionEntity) {
        viewModelScope.launch {
            repository.deleteQuestion(question)
        }
    }

    fun toggleQuestionActive(question: QuestionEntity) {
        viewModelScope.launch {
            repository.setQuestionActive(question.id, !question.isActive)
        }
    }

    // ==========================================
    // USER SETTINGS & PROFILE
    // ==========================================
    fun toggleDarkMode() {
        viewModelScope.launch {
            val cur = settings.value ?: UserSettingsEntity()
            repository.updateSettings(cur.copy(isDarkMode = !cur.isDarkMode))
        }
    }

    fun updateUserSettings(
        defaultRisk: Double,
        defaultBalance: Double,
        defaultMarket: String,
        defaultStrategy: String
    ) {
        viewModelScope.launch {
            val cur = settings.value ?: UserSettingsEntity()
            val updated = cur.copy(
                defaultRiskPercentage = defaultRisk,
                defaultAccountBalance = defaultBalance,
                defaultMarket = defaultMarket,
                defaultStrategy = defaultStrategy
            )
            repository.updateSettings(updated)
            draftRiskPercentage.value = defaultRisk
            draftAccountBalance.value = defaultBalance
        }
    }

    fun updateUserProfile(
        displayName: String,
        experienceLevel: String,
        preferredMarket: String,
        preferredStrategy: String
    ) {
        viewModelScope.launch {
            val cur = user.value ?: UserEntity()
            val updated = cur.copy(
                displayName = displayName,
                experienceLevel = experienceLevel,
                preferredMarket = preferredMarket,
                preferredStrategy = preferredStrategy,
                avatarInitials = displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
            )
            repository.updateUser(updated)
        }
    }
}
