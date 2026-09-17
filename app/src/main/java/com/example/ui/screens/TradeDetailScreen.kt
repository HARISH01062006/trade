package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TraderEmotion
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.components.NeumorphicSegmentedToggle
import com.example.ui.theme.LossRed
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
import com.example.ui.theme.WinGreen
import com.example.ui.viewmodel.TradeScoreViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TradeDetailScreen(
    viewModel: TradeScoreViewModel,
    onBack: () -> Unit,
    isDark: Boolean = false
) {
    val trade = viewModel.selectedTrade.collectAsState().value ?: return Box(modifier = Modifier.fillMaxSize())
    val answers by viewModel.selectedTradeAnswers.collectAsState()

    var outcome by remember(trade) { mutableStateOf(trade.outcome) }
    var actualExitPrice by remember(trade) { mutableStateOf(trade.actualExitPrice?.toString() ?: "") }
    var realizedPnl by remember(trade) { mutableStateOf(trade.realizedPnl?.toString() ?: "") }
    var selectedEmotion by remember(trade) { mutableStateOf(trade.emotion) }
    var whatWentRight by remember(trade) { mutableStateOf(trade.whatWentRight) }
    var whatWentWrong by remember(trade) { mutableStateOf(trade.whatWentWrong) }
    var lessonLearned by remember(trade) { mutableStateOf(trade.lessonLearned) }
    var isSavedConfirmation by remember { mutableStateOf(false) }

    val scoreColor = when {
        trade.score >= 80 -> SetupHighQuality
        trade.score >= 60 -> SetupModerate
        trade.score >= 40 -> SetupWeak
        trade.score >= 20 -> SetupVeryWeak
        else -> SetupDanger
    }

    val isBuy = trade.direction.equals("BUY", ignoreCase = true)
    val formattedDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.US).format(Date(trade.timestamp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("trade_detail_screen")
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
                text = "${trade.symbol} • ${trade.direction}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLight else Color.White
            )
            NeumorphicIconButton(
                onClick = {
                    viewModel.deleteTradeCheck(trade.id)
                    onBack()
                },
                isDark = isDark,
                size = 42.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = SetupDanger,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Score Card
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "PRE-TRADE SETUP SCORE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${trade.score}",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor
                        )
                        Text(
                            text = "/100",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    Text(
                        text = "${trade.passedCount} Passed • ${trade.failedCount} Failed",
                        fontSize = 12.sp,
                        color = if (isDark) TextLightSecondary else TextDarkSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .border(1.dp, scoreColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = trade.scoreCategory.replace("_", " "),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Trade Specs Card
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text(
                    text = "SETUP PARAMETERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Entry Price", fontSize = 11.sp, color = TextMuted)
                        Text(String.format(Locale.US, "%.5f", trade.entryPrice), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Stop Loss", fontSize = 11.sp, color = TextMuted)
                        Text(String.format(Locale.US, "%.5f", trade.stopLoss), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetupDanger)
                    }
                    Column {
                        Text("Take Profit", fontSize = 11.sp, color = TextMuted)
                        Text(String.format(Locale.US, "%.5f", trade.takeProfit), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetupHighQuality)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Risk : Reward", fontSize = 11.sp, color = TextMuted)
                        Text("1 : ${String.format(Locale.US, "%.2f", trade.riskRewardRatio)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                    }
                    Column {
                        Text("Potential Risk", fontSize = 11.sp, color = TextMuted)
                        Text("-$${String.format(Locale.US, "%.2f", trade.potentialRisk)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetupDanger)
                    }
                    Column {
                        Text("Potential Reward", fontSize = 11.sp, color = TextMuted)
                        Text("+$${String.format(Locale.US, "%.2f", trade.potentialReward)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetupHighQuality)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Strategy: ${trade.strategy} (${trade.timeframe}, ${trade.tradingSession})", fontSize = 12.sp, color = PrimaryPurple, fontWeight = FontWeight.SemiBold)
                if (trade.tradeReason.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Reason: ${trade.tradeReason}", fontSize = 12.sp, color = if (isDark) TextLightSecondary else TextDarkSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Question Answers Card
        if (answers.isNotEmpty()) {
            NeumorphicCard(
                modifier = Modifier.fillMaxWidth(),
                isDark = isDark
            ) {
                Column {
                    Text(
                        text = "EVALUATION CHECKLIST BREAKDOWN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    answers.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (item.answer) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (item.answer) SetupHighQuality else SetupDanger,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.questionText,
                                fontSize = 12.sp,
                                color = if (isDark) TextLight else TextDark,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (item.answer) "YES" else "NO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.answer) SetupHighQuality else SetupDanger
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // ==========================================
        // POST-TRADE JOURNAL OUTCOME EDITOR
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 12.dp
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POST-TRADE JOURNAL ENTRY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Outcome Toggle
                Text("Trade Outcome", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                val outcomeOptions = listOf("PENDING", "WIN", "LOSS", "BREAKEVEN")
                val curOutcomeIndex = outcomeOptions.indexOf(outcome.uppercase()).let { if (it >= 0) it else 0 }
                NeumorphicSegmentedToggle(
                    options = outcomeOptions,
                    selectedIndex = curOutcomeIndex,
                    onSelect = { outcome = outcomeOptions[it] },
                    isDark = isDark,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = actualExitPrice,
                        onValueChange = { actualExitPrice = it },
                        label = { Text("Actual Exit Price") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = realizedPnl,
                        onValueChange = { realizedPnl = it },
                        label = { Text("Realized P&L ($)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Emotional State Chips
                Text("Trader Emotional State", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TraderEmotion.entries.forEach { emo ->
                        val isSelected = emo.name.equals(selectedEmotion, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) PrimaryPurple else Color(0xFFEBEAF8),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedEmotion = emo.name }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = emo.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else PrimaryPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = whatWentRight,
                    onValueChange = { whatWentRight = it },
                    label = { Text("What went right?") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = whatWentWrong,
                    onValueChange = { whatWentWrong = it },
                    label = { Text("What went wrong / Execution errors?") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lessonLearned,
                    onValueChange = { lessonLearned = it },
                    label = { Text("Lesson Learned for future setups") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                NeumorphicButton(
                    text = if (isSavedConfirmation) "JOURNAL UPDATED!" else "SAVE OUTCOME LOG",
                    onClick = {
                        val exit = actualExitPrice.toDoubleOrNull()
                        val pnl = realizedPnl.toDoubleOrNull()
                        viewModel.updateJournalOutcome(
                            tradeId = trade.id,
                            outcome = outcome,
                            actualExitPrice = exit,
                            pnl = pnl,
                            emotion = selectedEmotion,
                            whatWentRight = whatWentRight,
                            whatWentWrong = whatWentWrong,
                            lessonLearned = lessonLearned
                        )
                        isSavedConfirmation = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    gradient = PrimaryButtonGradient
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
