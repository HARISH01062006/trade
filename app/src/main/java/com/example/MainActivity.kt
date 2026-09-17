package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MainTab
import com.example.ui.components.TradeBottomBar
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DailyChecklistScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EducationScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.QuestionManagementScreen
import com.example.ui.screens.QuestionnaireScreen
import com.example.ui.screens.ScoreResultScreen
import com.example.ui.screens.TradeDetailScreen
import com.example.ui.screens.TradeEntryScreen
import com.example.ui.theme.DarkCanvasGradient
import com.example.ui.theme.PurpleCanvasGradient
import com.example.ui.theme.TradeScoreTheme
import com.example.ui.viewmodel.TradeScoreViewModel

enum class AppScreen {
    MAIN_TAB,
    QUESTIONNAIRE,
    SCORE_RESULT,
    TRADE_DETAIL,
    DAILY_CHECKLIST,
    QUESTION_MANAGEMENT,
    EDUCATION
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TradeScoreViewModel = viewModel()
            val settings by viewModel.settings.collectAsState()
            val isDark = settings?.isDarkMode ?: false

            TradeScoreTheme(darkTheme = isDark) {
                TradeScoreApp(viewModel = viewModel, isDark = isDark)
            }
        }
    }
}

@Composable
fun TradeScoreApp(
    viewModel: TradeScoreViewModel,
    isDark: Boolean
) {
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var currentScreen by remember { mutableStateOf(AppScreen.MAIN_TAB) }

    // Hardware back button navigation handling
    BackHandler(enabled = currentScreen != AppScreen.MAIN_TAB || currentTab != MainTab.HOME) {
        if (currentScreen != AppScreen.MAIN_TAB) {
            when (currentScreen) {
                AppScreen.QUESTIONNAIRE -> currentScreen = AppScreen.MAIN_TAB
                AppScreen.SCORE_RESULT -> currentScreen = AppScreen.MAIN_TAB
                AppScreen.TRADE_DETAIL -> currentScreen = AppScreen.MAIN_TAB
                AppScreen.DAILY_CHECKLIST -> currentScreen = AppScreen.MAIN_TAB
                AppScreen.QUESTION_MANAGEMENT -> currentScreen = AppScreen.MAIN_TAB
                AppScreen.EDUCATION -> currentScreen = AppScreen.MAIN_TAB
                else -> currentScreen = AppScreen.MAIN_TAB
            }
        } else if (currentTab != MainTab.HOME) {
            currentTab = MainTab.HOME
        }
    }

    val backgroundBrush = if (isDark) DarkCanvasGradient else PurpleCanvasGradient

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                // Show bottom navigation bar when on the main tabs
                if (currentScreen == AppScreen.MAIN_TAB) {
                    TradeBottomBar(
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it },
                        isDark = isDark,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Crossfade(
                    targetState = Pair(currentScreen, currentTab),
                    label = "screen_crossfade"
                ) { (screen, tab) ->
                    when (screen) {
                        AppScreen.MAIN_TAB -> {
                            when (tab) {
                                MainTab.HOME -> {
                                    DashboardScreen(
                                        viewModel = viewModel,
                                        onNavigateToTradeEntry = { currentTab = MainTab.CHECK },
                                        onNavigateToChecklist = { currentScreen = AppScreen.DAILY_CHECKLIST },
                                        onNavigateToSettings = { currentTab = MainTab.PROFILE },
                                        onSelectTrade = { trade ->
                                            viewModel.selectTradeForDetail(trade)
                                            currentScreen = AppScreen.TRADE_DETAIL
                                        },
                                        isDark = isDark
                                    )
                                }
                                MainTab.CHECK -> {
                                    TradeEntryScreen(
                                        viewModel = viewModel,
                                        onBack = { currentTab = MainTab.HOME },
                                        onStartAssessment = { currentScreen = AppScreen.QUESTIONNAIRE },
                                        isDark = isDark
                                    )
                                }
                                MainTab.JOURNAL -> {
                                    JournalScreen(
                                        viewModel = viewModel,
                                        onSelectTrade = { trade ->
                                            viewModel.selectTradeForDetail(trade)
                                            currentScreen = AppScreen.TRADE_DETAIL
                                        },
                                        isDark = isDark
                                    )
                                }
                                MainTab.ANALYTICS -> {
                                    AnalyticsScreen(
                                        viewModel = viewModel,
                                        isDark = isDark
                                    )
                                }
                                MainTab.PROFILE -> {
                                    ProfileSettingsScreen(
                                        viewModel = viewModel,
                                        onNavigateToQuestions = { currentScreen = AppScreen.QUESTION_MANAGEMENT },
                                        onNavigateToEducation = { currentScreen = AppScreen.EDUCATION },
                                        isDark = isDark
                                    )
                                }
                            }
                        }
                        AppScreen.QUESTIONNAIRE -> {
                            QuestionnaireScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = AppScreen.MAIN_TAB },
                                onScoreCalculated = { currentScreen = AppScreen.SCORE_RESULT },
                                isDark = isDark
                            )
                        }
                        AppScreen.SCORE_RESULT -> {
                            ScoreResultScreen(
                                viewModel = viewModel,
                                onBackToDashboard = {
                                    currentScreen = AppScreen.MAIN_TAB
                                    currentTab = MainTab.HOME
                                },
                                onReevaluate = {
                                    viewModel.startAssessment()
                                    currentScreen = AppScreen.QUESTIONNAIRE
                                },
                                onViewInJournal = {
                                    currentScreen = AppScreen.MAIN_TAB
                                    currentTab = MainTab.JOURNAL
                                },
                                isDark = isDark
                            )
                        }
                        AppScreen.TRADE_DETAIL -> {
                            TradeDetailScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = AppScreen.MAIN_TAB },
                                isDark = isDark
                            )
                        }
                        AppScreen.DAILY_CHECKLIST -> {
                            DailyChecklistScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = AppScreen.MAIN_TAB },
                                isDark = isDark
                            )
                        }
                        AppScreen.QUESTION_MANAGEMENT -> {
                            QuestionManagementScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = AppScreen.MAIN_TAB },
                                isDark = isDark
                            )
                        }
                        AppScreen.EDUCATION -> {
                            EducationScreen(
                                onBack = { currentScreen = AppScreen.MAIN_TAB },
                                isDark = isDark
                            )
                        }
                    }
                }
            }
        }
    }
}
