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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradeCheckEntity
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicHighlightCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.components.ScoreArchGauge
import com.example.ui.theme.HighlightCardGradient
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: TradeScoreViewModel,
    onNavigateToTradeEntry: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSelectTrade: (TradeCheckEntity) -> Unit,
    isDark: Boolean = false
) {
    val user by viewModel.user.collectAsState()
    val allTrades by viewModel.allTradeChecks.collectAsState()
    val recentTrades by viewModel.recentTradeChecks.collectAsState()
    val todayChecklist by viewModel.todayChecklist.collectAsState()

    // Dynamic Calculations or realistic initial placeholder defaults
    val hasTrades = allTrades.isNotEmpty()
    val totalChecked = if (hasTrades) allTrades.size else 12
    val todayAvgScore = if (hasTrades) {
        allTrades.map { it.score }.average().toInt()
    } else 84

    val highQualityCount = if (hasTrades) allTrades.count { it.score >= 80 } else 8
    val avoidedCount = if (hasTrades) allTrades.count { it.score < 60 } else 4
    val disciplineRate = if (hasTrades) {
        ((highQualityCount.toFloat() / totalChecked.coerceAtLeast(1)) * 100).toInt()
    } else 86

    val greeting = rememberGreeting()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .testTag("dashboard_screen")
    ) {
        // ==========================================
        // 1. WELCOMING HEADER: Avatar, Greeting, Status Pill & Quick Actions
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar pill with initials and subtle online ring
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isDark) PrimaryPurple else Color.White)
                        .border(1.5.dp, Color(0x60FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.avatarInitials ?: "AM",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDark) Color.White else PrimaryPurple
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$greeting, ${user?.displayName?.split(" ")?.firstOrNull() ?: "Trader"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Mini Pro Badge
                        Box(
                            modifier = Modifier
                                .background(Color(0x26FFFFFF), RoundedCornerShape(8.dp))
                                .border(0.8.dp, Color(0x40FFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "PRO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(8.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "London Session Active • Discipline First",
                            fontSize = 11.sp,
                            color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Header Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Checklist shortcut with notification dot if incomplete
                val checklistPercent = todayChecklist?.completionPercentage ?: 60
                Box {
                    NeumorphicIconButton(
                        onClick = onNavigateToChecklist,
                        isDark = isDark,
                        size = 42.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Daily Routine",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (checklistPercent < 100) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFFF59E0B), CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                                .align(Alignment.TopEnd)
                        )
                    }
                }

                NeumorphicIconButton(
                    onClick = onNavigateToSettings,
                    isDark = isDark,
                    size = 42.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ==========================================
        // 2. REDESIGNED PROFESSIONAL SCORE SECTION (Historical Quality Arch)
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 12.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)
        ) {
            ScoreArchGauge(
                score = todayAvgScore,
                maxScore = 100,
                title = "HISTORICAL SETUP QUALITY",
                subtitle = "Pre-trade Confluence Index",
                badgeLabel = "LAST 30 DAYS",
                leftMetricTitle = "Setups Checked",
                leftMetricValue = "$totalChecked",
                leftMetricSubtitle = "Evaluated",
                centerMetricTitle = "Discipline Rate",
                centerMetricValue = "$disciplineRate%",
                centerMetricSubtitle = "Rule Adherence",
                rightMetricTitle = "High Quality",
                rightMetricValue = "$highQualityCount",
                rightMetricSubtitle = "Score ≥ 80",
                isDark = isDark
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // 3. CENTRAL 'CHECK TRADE SCORE' ACTION CARD & HERO BUTTON
        // ==========================================
        NeumorphicHighlightCard(
            modifier = Modifier.fillMaxWidth(),
            gradient = HighlightCardGradient,
            onClick = onNavigateToTradeEntry
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color(0xFFFFD166),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PRE-TRADE SETUP EVALUATOR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFEDEAFD),
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ready to enter a trade?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Score setup confluences, position sizing & stop-loss rules before placing capital on the line.",
                            fontSize = 12.sp,
                            color = Color(0xFFE4E0FD),
                            lineHeight = 17.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0x33FFFFFF), CircleShape)
                            .border(1.dp, Color(0x66FFFFFF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Go",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature check pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TradePrepPill("✓ Confluence Scan")
                    TradePrepPill("✓ Risk Calculator")
                    TradePrepPill("✓ Bias Check")
                }

                Spacer(modifier = Modifier.height(14.dp))

                NeumorphicButton(
                    text = "CHECK TRADE SCORE",
                    onClick = onNavigateToTradeEntry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("check_trade_score_button"),
                    gradient = PrimaryButtonGradient,
                    textColor = Color.White,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // 4. PRE-MARKET ROUTINE CHECKLIST BANNER
        // ==========================================
        val checklistPercent = todayChecklist?.completionPercentage ?: 60
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            onClick = onNavigateToChecklist,
            cornerRadius = 20.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Daily Pre-Market Routine",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(PrimaryPurple.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$checklistPercent% Complete",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (checklistPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryPurple,
                        trackColor = if (isDark) Color(0xFF2C2450) else Color(0xFFE2E0FB)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View checklist",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // 5. TRADING PERFORMANCE STATISTICS (2x2 Neumorphic Grid)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRADING PERFORMANCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLightSecondary else Color.White,
                letterSpacing = 1.sp
            )
            Text(
                text = "Past 30 Days",
                fontSize = 11.sp,
                color = if (isDark) TextMuted else Color(0xFFDDD9FA)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Setups Checked",
                value = "$totalChecked",
                subtitle = "Pre-trade evaluations",
                icon = Icons.Default.Layers,
                tagText = "+3 this week",
                isDark = isDark,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Average Quality",
                value = "$todayAvgScore/100",
                subtitle = if (todayAvgScore >= 75) "Disciplined Execution" else "Selective Execution",
                icon = Icons.Default.TrendingUp,
                accentColor = SetupHighQuality,
                tagText = "Top 15%",
                isDark = isDark,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "A+ Grade Setups",
                value = "$highQualityCount",
                subtitle = "Score ≥ 80 (High Edge)",
                icon = Icons.Default.Verified,
                accentColor = SetupHighQuality,
                tagText = "$disciplineRate% of Total",
                isDark = isDark,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Avoided Bad Setups",
                value = "$avoidedCount",
                subtitle = "Low quality filtered",
                icon = Icons.Default.Security,
                accentColor = SetupWeak,
                tagText = "Capital Saved",
                isDark = isDark,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // 6. RECENT TRADE ASSESSMENTS (History Cards in Neumorphic Style)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECENT ASSESSMENTS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLightSecondary else Color.White,
                letterSpacing = 1.sp
            )
            Text(
                text = if (recentTrades.isNotEmpty()) "${recentTrades.size} assessed" else "Sample Preview",
                fontSize = 11.sp,
                color = if (isDark) TextMuted else Color(0xFFDDD9FA)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (recentTrades.isNotEmpty()) {
            recentTrades.forEach { trade ->
                RecentTradeRowItem(
                    trade = trade,
                    isDark = isDark,
                    onClick = { onSelectTrade(trade) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            // High-fidelity Initial Placeholder Trade Cards in Neumorphic Style
            InitialPlaceholderTradeCards(
                isDark = isDark,
                onCheckTradeClick = onNavigateToTradeEntry
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun TradePrepPill(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0x2EFFFFFF), RoundedCornerShape(12.dp))
            .border(0.8.dp, Color(0x40FFFFFF), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tagText: String? = null,
    accentColor: Color? = null,
    isDark: Boolean = false
) {
    NeumorphicCard(
        modifier = modifier,
        isDark = isDark,
        cornerRadius = 22.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor ?: PrimaryPurple,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor ?: if (isDark) TextLight else TextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = if (isDark) TextLightSecondary else TextDarkSecondary
            )
            if (tagText != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(
                            (accentColor ?: PrimaryPurple).copy(alpha = 0.1f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tagText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor ?: PrimaryPurple
                    )
                }
            }
        }
    }
}

@Composable
fun RecentTradeRowItem(
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

    val isBuy = trade.direction.equals("BUY", ignoreCase = true)
    val formattedDate = SimpleDateFormat("dd MMM, HH:mm", Locale.US).format(Date(trade.timestamp))

    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 20.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Direction icon pill
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = if (isBuy) Color(0xFFEDEBFD) else Color(0xFFFFECEE),
                            shape = CircleShape
                        )
                        .border(
                            1.dp,
                            if (isBuy) Color(0xFFC7BFFB) else Color(0xFFFFCCD2),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBuy) "B" else "S",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = if (isBuy) PrimaryPurple else SetupDanger
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = trade.symbol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isBuy) PrimaryPurple.copy(alpha = 0.1f) else SetupDanger.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = trade.direction,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBuy) PrimaryPurple else SetupDanger
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${trade.market} • ${trade.timeframe} • $formattedDate",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${trade.score}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor
                    )
                    Text(
                        text = "/100",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                val statusText = when {
                    trade.score >= 80 -> "A+ QUALITY"
                    trade.score >= 60 -> "MODERATE"
                    trade.score >= 40 -> "WEAK SETUP"
                    trade.score >= 20 -> "VERY WEAK"
                    else -> "AVOID SETUP"
                }
                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                }
            }
        }
    }
}

@Composable
fun InitialPlaceholderTradeCards(
    isDark: Boolean = false,
    onCheckTradeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Sample Trade 1: EURUSD Long
        PlaceholderTradeRow(
            symbol = "EURUSD",
            direction = "BUY",
            market = "Forex",
            timeframe = "15m",
            timeAgo = "Today, 08:30",
            score = 90,
            status = "A+ QUALITY",
            scoreColor = SetupHighQuality,
            isDark = isDark,
            onClick = onCheckTradeClick
        )

        // Sample Trade 2: BTCUSDT Breakout
        PlaceholderTradeRow(
            symbol = "BTCUSDT",
            direction = "BUY",
            market = "Crypto",
            timeframe = "1h",
            timeAgo = "Yesterday, 14:15",
            score = 85,
            status = "HIGH QUALITY",
            scoreColor = SetupHighQuality,
            isDark = isDark,
            onClick = onCheckTradeClick
        )

        // Sample Trade 3: GBPJPY Short (Avoided)
        PlaceholderTradeRow(
            symbol = "GBPJPY",
            direction = "SELL",
            market = "Forex",
            timeframe = "5m",
            timeAgo = "2 days ago",
            score = 45,
            status = "WEAK (AVOIDED)",
            scoreColor = SetupWeak,
            isDark = isDark,
            onClick = onCheckTradeClick
        )
    }
}

@Composable
fun PlaceholderTradeRow(
    symbol: String,
    direction: String,
    market: String,
    timeframe: String,
    timeAgo: String,
    score: Int,
    status: String,
    scoreColor: Color,
    isDark: Boolean = false,
    onClick: () -> Unit
) {
    val isBuy = direction.equals("BUY", ignoreCase = true)

    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 20.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = if (isBuy) Color(0xFFEDEBFD) else Color(0xFFFFECEE),
                            shape = CircleShape
                        )
                        .border(
                            1.dp,
                            if (isBuy) Color(0xFFC7BFFB) else Color(0xFFFFCCD2),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBuy) "B" else "S",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = if (isBuy) PrimaryPurple else SetupDanger
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = symbol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isBuy) PrimaryPurple.copy(alpha = 0.1f) else SetupDanger.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = direction,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBuy) PrimaryPurple else SetupDanger
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$market • $timeframe • $timeAgo",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$score",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor
                    )
                    Text(
                        text = "/100",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                }
            }
        }
    }
}

@Composable
fun rememberGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

