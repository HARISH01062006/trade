package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyChecklistEntity
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel

@Composable
fun DailyChecklistScreen(
    viewModel: TradeScoreViewModel,
    onBack: () -> Unit,
    isDark: Boolean = false
) {
    val checklist = viewModel.todayChecklist.collectAsState().value ?: DailyChecklistEntity(dateString = "today")
    val completion = checklist.completionPercentage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("daily_checklist_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NeumorphicIconButton(
                onClick = onBack,
                isDark = isDark,
                size = 42.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Pre-Market Routine",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLight else Color.White
            )
            Box(modifier = Modifier.size(42.dp))
        }

        // Completion Banner Card
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 12.dp
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DAILY READINESS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "$completion% Prepared",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (completion >= 80) SetupHighQuality else PrimaryPurple
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = if (completion >= 80) SetupHighQuality.copy(alpha = 0.15f) else PrimaryPurple.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (completion >= 80) "READY TO TRADE" else "INCOMPLETE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (completion >= 80) SetupHighQuality else PrimaryPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { (completion / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (completion >= 80) SetupHighQuality else PrimaryPurple,
                    trackColor = Color(0xFFE2E0FB)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "PRE-SESSION ROUTINE ITEMS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) TextLightSecondary else Color.White,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        // Routine Checklist Items
        DailyChecklistItem(
            title = "1. Market Trend & Sentiment Review",
            description = "Analyzed overall market bias on 4H/Daily charts.",
            isChecked = checklist.marketConditionsChecked,
            onToggle = { viewModel.toggleDailyChecklistItem("marketConditions", checklist.marketConditionsChecked) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "2. High-Impact Economic Calendar",
            description = "Confirmed no unplanned CPI, FOMC, or NFP releases during session.",
            isChecked = checklist.economicCalendarChecked,
            onToggle = { viewModel.toggleDailyChecklistItem("economicCalendar", checklist.economicCalendarChecked) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "3. Defined Risk Limit Per Trade",
            description = "Max risk capped strictly between 0.5% - 1.5% of total capital.",
            isChecked = checklist.riskLimitDefined,
            onToggle = { viewModel.toggleDailyChecklistItem("riskLimit", checklist.riskLimitDefined) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "4. Strategy Rules Acknowledged",
            description = "Only entering setups that meet all rules of my defined trading playbook.",
            isChecked = checklist.strategyReviewed,
            onToggle = { viewModel.toggleDailyChecklistItem("strategy", checklist.strategyReviewed) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "5. Review Yesterday's Mistakes",
            description = "Reinforced lessons to prevent repeating past execution errors.",
            isChecked = checklist.previousMistakesReviewed,
            onToggle = { viewModel.toggleDailyChecklistItem("previousMistakes", checklist.previousMistakesReviewed) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "6. Emotional State Confirmation",
            description = "Calm, focused, rested, and not feeling urgency, anger, or FOMO.",
            isChecked = checklist.emotionalStateConfirmed,
            onToggle = { viewModel.toggleDailyChecklistItem("emotionalState", checklist.emotionalStateConfirmed) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailyChecklistItem(
            title = "7. Maximum Daily Trades Limit",
            description = "Hard ceiling of 3 trades today to prevent overtrading.",
            isChecked = checklist.maxTradesSet,
            onToggle = { viewModel.toggleDailyChecklistItem("maxTrades", checklist.maxTradesSet) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Discipline Shield
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Professional Mindset Rule",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isDark) TextLight else TextDark
                    )
                    Text(
                        text = "Good trading is not about being right on every setup; it's about adhering to the system on every trade.",
                        fontSize = 11.sp,
                        color = if (isDark) TextLightSecondary else TextDarkSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun DailyChecklistItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onToggle: () -> Unit,
    isDark: Boolean = false
) {
    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 18.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (isChecked) SetupHighQuality else if (isDark) Color(0xFF2C2450) else Color(0xFFEBEAF8),
                        shape = CircleShape
                    )
                    .border(
                        1.2.dp,
                        if (isChecked) SetupHighQuality else Color(0xFFB4AEE8),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) TextLight else TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
