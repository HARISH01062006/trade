package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.DailyChecklistDao
import com.example.data.dao.QuestionDao
import com.example.data.dao.TradeAnswerDao
import com.example.data.dao.TradeCheckDao
import com.example.data.dao.UserDao
import com.example.data.model.DailyChecklistEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.TradeAnswerEntity
import com.example.data.model.TradeCheckEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserSettingsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TradeCheckEntity::class,
        TradeAnswerEntity::class,
        QuestionEntity::class,
        DailyChecklistEntity::class,
        UserSettingsEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TradeScoreDatabase : RoomDatabase() {
    abstract fun tradeCheckDao(): TradeCheckDao
    abstract fun tradeAnswerDao(): TradeAnswerDao
    abstract fun questionDao(): QuestionDao
    abstract fun dailyChecklistDao(): DailyChecklistDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TradeScoreDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TradeScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TradeScoreDatabase::class.java,
                    "tradescore_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: TradeScoreDatabase) {
            val questionDao = database.questionDao()
            val tradeCheckDao = database.tradeCheckDao()
            val userDao = database.userDao()
            val dailyChecklistDao = database.dailyChecklistDao()

            // 1. Initial User
            userDao.insertUser(
                UserEntity(
                    id = "trader_primary",
                    email = "harishwebdevelop@gmail.com",
                    displayName = "Alex Morgan",
                    title = "Senior FX & Crypto Trader",
                    experienceLevel = "Advanced (4+ Yrs)",
                    preferredMarket = "Forex / Major Pairs",
                    preferredStrategy = "Breakout & Retest",
                    disciplineScore = 86,
                    avatarInitials = "AM",
                    isLoggedIn = true
                )
            )

            // 2. Initial Settings
            userDao.insertSettings(
                UserSettingsEntity(
                    id = 1,
                    defaultRiskPercentage = 1.0,
                    defaultAccountBalance = 25000.0,
                    defaultMarket = "Forex",
                    defaultStrategy = "Breakout & Retest",
                    isDarkMode = false,
                    dailyReminderEnabled = true,
                    journalReminderEnabled = true,
                    currencySymbol = "$"
                )
            )

            // 3. Initial dynamic questionnaire
            val defaultQuestions = listOf(
                QuestionEntity(
                    id = 1,
                    text = "Is the trade setup aligned with your trading strategy?",
                    category = "Strategy",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 1
                ),
                QuestionEntity(
                    id = 2,
                    text = "Is the current market trend supporting your trade direction?",
                    category = "Technical Analysis",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 2
                ),
                QuestionEntity(
                    id = 3,
                    text = "Is there a clear entry confirmation?",
                    category = "Technical Analysis",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 3
                ),
                QuestionEntity(
                    id = 4,
                    text = "Is the stop loss placed at a logical level?",
                    category = "Risk Management",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 4
                ),
                QuestionEntity(
                    id = 5,
                    text = "Is the risk/reward ratio acceptable (>= 1:2)?",
                    category = "Risk Management",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 5
                ),
                QuestionEntity(
                    id = 6,
                    text = "Is your risk within your predefined risk limit?",
                    category = "Risk Management",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 6
                ),
                QuestionEntity(
                    id = 7,
                    text = "Are you entering because of a valid setup rather than emotion?",
                    category = "Psychology",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 7
                ),
                QuestionEntity(
                    id = 8,
                    text = "Have you checked the higher timeframe trend and structure?",
                    category = "Technical Analysis",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 8
                ),
                QuestionEntity(
                    id = 9,
                    text = "Is there sufficient market liquidity and normal spread?",
                    category = "Market Conditions",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 9
                ),
                QuestionEntity(
                    id = 10,
                    text = "Is there any major high-impact news event due shortly?",
                    category = "Market Conditions",
                    weight = 10,
                    yesScore = 10,
                    noScore = 0,
                    isActive = true,
                    orderIndex = 10
                )
            )
            questionDao.insertQuestions(defaultQuestions)

            // 4. Initial demo trades to populate journal & analytics
            val now = System.currentTimeMillis()
            val day = 86400000L
            val demoTrades = listOf(
                TradeCheckEntity(
                    id = 1,
                    symbol = "EURUSD",
                    market = "Forex",
                    direction = "BUY",
                    entryPrice = 1.0850,
                    stopLoss = 1.0820,
                    takeProfit = 1.0930,
                    riskPercentage = 1.0,
                    accountBalance = 25000.0,
                    positionSize = 83333.0,
                    riskRewardRatio = 2.67,
                    potentialRisk = 250.0,
                    potentialReward = 667.5,
                    tradingSession = "London",
                    timeframe = "15m",
                    strategy = "Breakout & Retest",
                    tradeReason = "London open breakout of key resistance at 1.0845 with clean retest.",
                    notes = "Higher timeframe 4H is bullish. Strong volume surge.",
                    score = 90,
                    scoreCategory = "HIGH_QUALITY",
                    passedCount = 9,
                    failedCount = 1,
                    timestamp = now - day * 1,
                    isSavedToJournal = true,
                    isDemo = true,
                    outcome = "WIN",
                    actualExitPrice = 1.0925,
                    realizedPnl = 625.0,
                    emotion = "CONFIDENT",
                    whatWentRight = "Waited patiently for 15m candle close above level.",
                    whatWentWrong = "Slightly hesitated on initial entry.",
                    lessonLearned = "Patience at key zones always yields better risk-reward."
                ),
                TradeCheckEntity(
                    id = 2,
                    symbol = "BTCUSDT",
                    market = "Crypto",
                    direction = "BUY",
                    entryPrice = 64200.0,
                    stopLoss = 63400.0,
                    takeProfit = 66600.0,
                    riskPercentage = 1.0,
                    accountBalance = 25000.0,
                    positionSize = 0.3125,
                    riskRewardRatio = 3.0,
                    potentialRisk = 250.0,
                    potentialReward = 750.0,
                    tradingSession = "New York",
                    timeframe = "1h",
                    strategy = "ICT / Smart Money Concepts",
                    tradeReason = "Fair value gap fill into order block during NY session open.",
                    notes = "Liquidity sweep below previous day low confirmed.",
                    score = 80,
                    scoreCategory = "HIGH_QUALITY",
                    passedCount = 8,
                    failedCount = 2,
                    timestamp = now - day * 2,
                    isSavedToJournal = true,
                    isDemo = true,
                    outcome = "WIN",
                    actualExitPrice = 66500.0,
                    realizedPnl = 718.75,
                    emotion = "CALM",
                    whatWentRight = "Identified liquidity sweep correctly.",
                    whatWentWrong = "None. Handled trade mechanically.",
                    lessonLearned = "Trust the pre-defined stop loss without micro-managing."
                ),
                TradeCheckEntity(
                    id = 3,
                    symbol = "XAUUSD",
                    market = "Commodities",
                    direction = "SELL",
                    entryPrice = 2510.50,
                    stopLoss = 2519.00,
                    takeProfit = 2490.00,
                    riskPercentage = 1.5,
                    accountBalance = 25000.0,
                    positionSize = 44.1,
                    riskRewardRatio = 2.41,
                    potentialRisk = 375.0,
                    potentialReward = 904.5,
                    tradingSession = "London",
                    timeframe = "5m",
                    strategy = "Key Support / Resistance",
                    tradeReason = "Double top rejection at psychological 2510 barrier.",
                    notes = "Fed speaker later today, but scalp horizon.",
                    score = 70,
                    scoreCategory = "MODERATE",
                    passedCount = 7,
                    failedCount = 3,
                    timestamp = now - day * 3,
                    isSavedToJournal = true,
                    isDemo = true,
                    outcome = "BREAKEVEN",
                    actualExitPrice = 2510.50,
                    realizedPnl = 0.0,
                    emotion = "CALM",
                    whatWentRight = "Moved stop to breakeven after 1R reached.",
                    whatWentWrong = "Took partials a bit late.",
                    lessonLearned = "Gold volatility requires swift trailing rules."
                ),
                TradeCheckEntity(
                    id = 4,
                    symbol = "NVDA",
                    market = "Stocks",
                    direction = "BUY",
                    entryPrice = 122.40,
                    stopLoss = 120.20,
                    takeProfit = 125.00,
                    riskPercentage = 1.0,
                    accountBalance = 25000.0,
                    positionSize = 113.6,
                    riskRewardRatio = 1.18,
                    potentialRisk = 250.0,
                    potentialReward = 295.0,
                    tradingSession = "New York",
                    timeframe = "5m",
                    strategy = "Breakout & Retest",
                    tradeReason = "Impulsive push at market open, FOMO entry.",
                    notes = "R:R was subpar and news was imminent.",
                    score = 30,
                    scoreCategory = "VERY_WEAK",
                    passedCount = 3,
                    failedCount = 7,
                    timestamp = now - day * 4,
                    isSavedToJournal = true,
                    isDemo = true,
                    outcome = "LOSS",
                    actualExitPrice = 120.20,
                    realizedPnl = -250.0,
                    emotion = "FOMO",
                    whatWentRight = "Cut trade immediately at stop loss.",
                    whatWentWrong = "Ignored checklist score warning of 30/100!",
                    lessonLearned = "NEVER trade setups scored under 60. The checklist predicted this."
                )
            )
            tradeCheckDao.insertTradeChecks(demoTrades)

            // 5. Initial daily checklist for today
            dailyChecklistDao.insertOrUpdateChecklist(
                DailyChecklistEntity(
                    dateString = "2026-09-17",
                    marketConditionsChecked = true,
                    economicCalendarChecked = true,
                    riskLimitDefined = true,
                    strategyReviewed = true,
                    previousMistakesReviewed = true,
                    emotionalStateConfirmed = false,
                    maxTradesSet = true,
                    maxTradesLimit = 3,
                    dailyRiskPercentage = 2.0
                )
            )
        }
    }
}
