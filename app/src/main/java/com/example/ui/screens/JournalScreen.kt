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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradeCheckEntity
import com.example.ui.components.NeumorphicCard
import com.example.ui.theme.LossRed
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

@Composable
fun JournalScreen(
    viewModel: TradeScoreViewModel,
    onSelectTrade: (TradeCheckEntity) -> Unit,
    isDark: Boolean = false
) {
    val trades by viewModel.journalEntries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredTrades = trades.filter { trade ->
        val matchesSearch = trade.symbol.contains(searchQuery, ignoreCase = true) ||
                trade.strategy.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "ALL" -> true
            "WIN" -> trade.outcome.equals("WIN", ignoreCase = true)
            "LOSS" -> trade.outcome.equals("LOSS", ignoreCase = true)
            "BREAKEVEN" -> trade.outcome.equals("BREAKEVEN", ignoreCase = true)
            "PENDING" -> trade.outcome.equals("PENDING", ignoreCase = true)
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("journal_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Trading Journal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) TextLight else Color.White
                )
                Text(
                    text = "${trades.size} Total Setup Logs",
                    fontSize = 12.sp,
                    color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC)
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by symbol or strategy...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = PrimaryPurple
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("journal_search_input"),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (isDark) Color(0xFF231C44) else Color.White,
                unfocusedContainerColor = if (isDark) Color(0xFF231C44) else Color.White,
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = Color(0x33FFFFFF)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills
        val filterOptions = listOf("ALL", "WIN", "LOSS", "BREAKEVEN", "PENDING")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filterOptions.forEach { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) Color.White else Color(0x33FFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (isSelected) PrimaryPurple else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Trades List
        if (filteredTrades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                NeumorphicCard(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    isDark = isDark
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Journal Entries Found",
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else TextDark,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search query or filters.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrades, key = { it.id }) { trade ->
                    JournalItemCard(
                        trade = trade,
                        isDark = isDark,
                        onClick = { onSelectTrade(trade) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun JournalItemCard(
    trade: TradeCheckEntity,
    isDark: Boolean = false,
    onClick: () -> Unit
) {
    val scoreColor = when {
        trade.score >= 80 -> SetupHighQuality
        trade.score >= 60 -> SetupModerate
        trade.score >= 40 -> SetupWeak
        trade.score >= 20 -> SetupVeryWeak
        else -> SetupDanger
    }

    val outcomeColor = when (trade.outcome.uppercase()) {
        "WIN" -> WinGreen
        "LOSS" -> LossRed
        "BREAKEVEN" -> Color(0xFF6B7280)
        else -> PrimaryPurple
    }

    val isBuy = trade.direction.equals("BUY", ignoreCase = true)
    val formattedDate = SimpleDateFormat("dd MMM, HH:mm", Locale.US).format(Date(trade.timestamp))

    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 20.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (isBuy) Color(0xFFEDEBFD) else Color(0xFFFFECEE),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isBuy) "BUY" else "SELL",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            color = if (isBuy) PrimaryPurple else SetupDanger
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = trade.symbol,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) TextLight else TextDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Outcome Pill
                            Box(
                                modifier = Modifier
                                    .background(outcomeColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = trade.outcome.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = outcomeColor
                                )
                            }
                        }
                        Text(
                            text = "${trade.market} • $formattedDate",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${trade.score}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor
                        )
                        Text(
                            text = "/100",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    if (trade.realizedPnl != null) {
                        val pnl = trade.realizedPnl
                        val isPositive = pnl >= 0
                        Text(
                            text = "${if (isPositive) "+" else ""}$${String.format(Locale.US, "%.2f", pnl)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) WinGreen else LossRed
                        )
                    } else {
                        Text(
                            text = "R:R 1:${String.format(Locale.US, "%.1f", trade.riskRewardRatio)}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Strategy & Notes preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDark) Color(0xFF2C2450) else Color(0xFFF0EFFC), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trade.strategy,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPurple
                )
                Text(
                    text = "${trade.passedCount} Passed / ${trade.failedCount} Failed",
                    fontSize = 11.sp,
                    color = TextDarkSecondary
                )
            }
        }
    }
}
