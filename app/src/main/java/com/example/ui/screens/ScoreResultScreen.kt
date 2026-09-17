package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CircularScoreGauge
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SetupDanger
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.SetupModerate
import com.example.ui.theme.SetupVeryWeak
import com.example.ui.theme.SetupWeak
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel
import java.util.Locale

@Composable
fun ScoreResultScreen(
    viewModel: TradeScoreViewModel,
    onBackToDashboard: () -> Unit,
    onReevaluate: () -> Unit,
    onViewInJournal: () -> Unit,
    isDark: Boolean = false
) {
    val lastTrade by viewModel.lastResultTrade.collectAsState()
    val lastAnswers by viewModel.lastResultAnswers.collectAsState()

    val trade = lastTrade ?: return Box(modifier = Modifier.fillMaxSize())

    val score = trade.score
    val scoreColor = when {
        score >= 80 -> SetupHighQuality
        score >= 60 -> SetupModerate
        score >= 40 -> SetupWeak
        score >= 20 -> SetupVeryWeak
        else -> SetupDanger
    }

    val statusTitle = when {
        score >= 80 -> "HIGH QUALITY SETUP"
        score >= 60 -> "MODERATE SETUP"
        score >= 40 -> "WEAK SETUP"
        score >= 20 -> "VERY WEAK SETUP"
        else -> "DANGER — CONDITIONS UNCONFIRMED"
    }

    val statusSubtitle = when {
        score >= 80 -> "Strong setup based on your checklist. Discipline verified."
        score >= 60 -> "Setup requires additional confirmation before execution."
        score >= 40 -> "Multiple conditions are not confirmed. High risk of poor trade."
        score >= 20 -> "High risk warning. Low statistical confluence detected."
        else -> "CRITICAL WARNING: 0-19 Score. Do not enter this trade. Check for FOMO or revenge trading."
    }

    val passedAnswers = lastAnswers.filter { it.answer }
    val failedAnswers = lastAnswers.filter { !it.answer }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("score_result_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NeumorphicIconButton(
                onClick = onBackToDashboard,
                isDark = isDark,
                size = 42.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Dashboard",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Setup Assessment Result",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLight else Color.White
            )
            NeumorphicIconButton(
                onClick = onReevaluate,
                isDark = isDark,
                size = 42.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Re-evaluate",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ==========================================
        // SCORE GAUGE CARD
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 14.dp,
            cornerRadius = 30.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularScoreGauge(
                    score = score,
                    maxScore = 100,
                    sizeDp = 180,
                    isDark = isDark
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Status Badge
                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .border(1.5.dp, scoreColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = statusTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = statusSubtitle,
                    fontSize = 13.sp,
                    color = if (isDark) TextLightSecondary else TextDarkSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Passed vs Failed metrics pill
                Row(
                    modifier = Modifier
                        .background(if (isDark) Color(0xFF2C2450) else Color(0xFFEBEAF8), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(SetupHighQuality, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${trade.passedCount} Passed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupHighQuality
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(SetupDanger, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${trade.failedCount} Failed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupDanger
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // DANGER WARNING CARD (For low scores < 60)
        // ==========================================
        if (score < 60) {
            NeumorphicCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFFFF0F2),
                cornerRadius = 22.dp
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Dangerous,
                            contentDescription = null,
                            tint = SetupDanger,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "PSYCHOLOGICAL & DISCIPLINE CHECK",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SetupDanger,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Are you sure you are not entering because of FOMO, boredom, or revenge trading? Professional traders step away when their setup score is below 60.",
                        fontSize = 13.sp,
                        color = Color(0xFF8C1D2A),
                        lineHeight = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // TRADE DETAILS SUMMARY CARD
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRADE SPECIFICATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (trade.direction == "BUY") PrimaryPurple.copy(alpha = 0.1f) else SetupDanger.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${trade.symbol} • ${trade.direction}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (trade.direction == "BUY") PrimaryPurple else SetupDanger
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Entry Price", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = String.format(Locale.US, "%.5f", trade.entryPrice),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else TextDark
                        )
                    }
                    Column {
                        Text("Stop Loss", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = String.format(Locale.US, "%.5f", trade.stopLoss),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupDanger
                        )
                    }
                    Column {
                        Text("Take Profit", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = String.format(Locale.US, "%.5f", trade.takeProfit),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupHighQuality
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Risk : Reward", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "1 : ${String.format(Locale.US, "%.2f", trade.riskRewardRatio)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple
                        )
                    }
                    Column {
                        Text("Potential Risk", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "-$${String.format(Locale.US, "%.2f", trade.potentialRisk)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupDanger
                        )
                    }
                    Column {
                        Text("Potential Reward", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "+$${String.format(Locale.US, "%.2f", trade.potentialReward)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupHighQuality
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // WHY THIS SCORE (PASSED ITEMS)
        // ==========================================
        if (passedAnswers.isNotEmpty()) {
            NeumorphicCard(
                modifier = Modifier.fillMaxWidth(),
                isDark = isDark
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(SetupHighQuality.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SetupHighQuality,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WHY THIS SCORE? (CONFIRMED CRITERIA)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupHighQuality,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    passedAnswers.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SetupHighQuality,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.questionText,
                                fontSize = 13.sp,
                                color = if (isDark) TextLight else TextDark,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "+${item.weight} pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SetupHighQuality
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // AREAS TO REVIEW (FAILED ITEMS)
        // ==========================================
        if (failedAnswers.isNotEmpty()) {
            NeumorphicCard(
                modifier = Modifier.fillMaxWidth(),
                isDark = isDark
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(SetupDanger.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = SetupDanger,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AREAS TO REVIEW (UNCONFIRMED RISKS)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SetupDanger,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    failedAnswers.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = SetupDanger,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.questionText,
                                fontSize = 13.sp,
                                color = if (isDark) TextLight else TextDark,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "0 pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SetupDanger
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // ACTION BUTTONS
        // ==========================================
        NeumorphicButton(
            text = "SAVED TO JOURNAL — VIEW LOGS",
            onClick = onViewInJournal,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("view_in_journal_button"),
            gradient = PrimaryButtonGradient
        )

        Spacer(modifier = Modifier.height(12.dp))

        NeumorphicButton(
            text = "RETURN TO DASHBOARD",
            onClick = onBackToDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("return_to_dashboard_button"),
            gradient = Brush.horizontalGradient(listOf(Color(0xFF8A7EFA), Color(0xFFA59BFC)))
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}
