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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Direction
import com.example.data.model.Market
import com.example.data.model.Strategy
import com.example.data.model.Timeframe
import com.example.data.model.TradingSession
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.components.NeumorphicSegmentedToggle
import com.example.ui.theme.CrispBorderLight
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.SetupDanger
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TradeEntryScreen(
    viewModel: TradeScoreViewModel,
    onBack: () -> Unit,
    onStartAssessment: () -> Unit,
    isDark: Boolean = false
) {
    val symbol by viewModel.draftSymbol.collectAsState()
    val market by viewModel.draftMarket.collectAsState()
    val direction by viewModel.draftDirection.collectAsState()
    val entryPrice by viewModel.draftEntryPrice.collectAsState()
    val stopLoss by viewModel.draftStopLoss.collectAsState()
    val takeProfit by viewModel.draftTakeProfit.collectAsState()
    val riskPercentage by viewModel.draftRiskPercentage.collectAsState()
    val accountBalance by viewModel.draftAccountBalance.collectAsState()
    val session by viewModel.draftTradingSession.collectAsState()
    val timeframe by viewModel.draftTimeframe.collectAsState()
    val strategy by viewModel.draftStrategy.collectAsState()
    val tradeReason by viewModel.draftTradeReason.collectAsState()
    val notes by viewModel.draftNotes.collectAsState()

    val rrRatio = viewModel.calculateRiskRewardRatio()
    val potentialRisk = viewModel.calculatePotentialRisk()
    val potentialReward = viewModel.calculatePotentialReward()
    val positionSize = viewModel.calculatePositionSize()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("trade_entry_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Setup Evaluation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) TextLight else Color.White
                    )
                    Text(
                        text = "Enter setup specs before checking score",
                        fontSize = 12.sp,
                        color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC)
                    )
                }
            }
        }

        // ==========================================
        // SECTION 1: CORE INSTRUMENT & DIRECTION
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text(
                    text = "TRADE INSTRUMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Symbol Input
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { viewModel.updateDraftSymbol(it) },
                    label = { Text("Trading Symbol (e.g. EURUSD, BTC, NVDA)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_symbol"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = Color(0xFFD6D3F5)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Direction Toggle (BUY vs SELL)
                Text(
                    text = "Direction",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) TextLightSecondary else TextDarkSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                NeumorphicSegmentedToggle(
                    options = listOf("BUY / LONG", "SELL / SHORT"),
                    selectedIndex = if (direction == Direction.BUY) 0 else 1,
                    onSelect = { idx ->
                        viewModel.updateDraftDirection(if (idx == 0) Direction.BUY else Direction.SELL)
                    },
                    isDark = isDark,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Market Chips
                Text(
                    text = "Market Asset Class",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) TextLightSecondary else TextDarkSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Market.entries.forEach { m ->
                        val isSelected = m == market
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) PrimaryPurple else Color(0xFFEBEAF8),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.updateDraftMarket(m) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = m.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else PrimaryPurple
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 2: PRICE TARGETS & RISK
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text(
                    text = "PRICE TARGETS & RISK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Entry Price
                OutlinedTextField(
                    value = entryPrice,
                    onValueChange = { viewModel.updateDraftEntryPrice(it) },
                    label = { Text("Entry Price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_entry_price"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = Color(0xFFD6D3F5)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = stopLoss,
                        onValueChange = { viewModel.updateDraftStopLoss(it) },
                        label = { Text("Stop Loss") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_stop_loss"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SetupDanger,
                            unfocusedBorderColor = Color(0xFFFFD4D8)
                        )
                    )

                    OutlinedTextField(
                        value = takeProfit,
                        onValueChange = { viewModel.updateDraftTakeProfit(it) },
                        label = { Text("Take Profit") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_take_profit"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SetupHighQuality,
                            unfocusedBorderColor = Color(0xFFD3F5E4)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Risk Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Account Risk Percentage",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) TextLightSecondary else TextDarkSecondary
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f%%", riskPercentage),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryPurple
                    )
                }
                Slider(
                    value = riskPercentage.toFloat(),
                    onValueChange = { viewModel.updateDraftRiskPercentage(it.toDouble()) },
                    valueRange = 0.25f..5.0f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = PrimaryPurple,
                        activeTrackColor = PrimaryPurple,
                        inactiveTrackColor = Color(0xFFDDD9FA)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 3: REAL-TIME RISK/REWARD CALCULATOR CARD
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 12.dp
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RISK / REWARD METRICS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple,
                            letterSpacing = 1.sp
                        )
                    }

                    val rrAcceptable = rrRatio >= 2.0
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (rrAcceptable) SetupHighQuality.copy(alpha = 0.15f) else SetupDanger.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (rrAcceptable) "Valid R:R (>= 1:2)" else "Low R:R (< 1:2)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (rrAcceptable) SetupHighQuality else SetupDanger
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Risk / Reward", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "1 : ${String.format(Locale.US, "%.2f", rrRatio)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (rrRatio >= 2.0) SetupHighQuality else PrimaryPurple
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Potential Risk", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "-$${String.format(Locale.US, "%.2f", potentialRisk)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SetupDanger
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Potential Reward", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "+$${String.format(Locale.US, "%.2f", potentialReward)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SetupHighQuality
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Position Size", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "${String.format(Locale.US, "%.1f", positionSize)} units",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else TextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 4: CONTEXT & STRATEGY
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text(
                    text = "EXECUTION CONTEXT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Timeframe Chips
                Text("Timeframe", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Timeframe.entries.forEach { tf ->
                        val isSelected = tf == timeframe
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (isSelected) PrimaryPurple else Color(0xFFEBEAF8),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.updateDraftTimeframe(tf) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tf.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else PrimaryPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Strategy Chips
                Text("Strategy", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Strategy.entries.forEach { s ->
                        val isSelected = s == strategy
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) PrimaryPurple else Color(0xFFEBEAF8),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.updateDraftStrategy(s) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = s.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else PrimaryPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Trade Reason
                OutlinedTextField(
                    value = tradeReason,
                    onValueChange = { viewModel.updateDraftReason(it) },
                    label = { Text("Primary Setup Reason") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = Color(0xFFD6D3F5)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { viewModel.updateDraftNotes(it) },
                    label = { Text("Higher Timeframe & Notes") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = Color(0xFFD6D3F5)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Action: START ASSESSMENT
        NeumorphicButton(
            text = "CHECK TRADE SCORE",
            onClick = {
                viewModel.startAssessment()
                onStartAssessment()
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("start_assessment_button"),
            gradient = PrimaryButtonGradient
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = if (isDark) TextLightSecondary else Color(0xFFE2DFFC),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Opens interactive 10-point trade questionnaire",
                fontSize = 11.sp,
                color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
