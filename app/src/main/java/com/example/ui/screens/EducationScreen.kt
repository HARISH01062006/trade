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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted

data class EducationalTopic(
    val id: String,
    val title: String,
    val category: String,
    val icon: ImageVector,
    val summary: String,
    val content: String
)

@Composable
fun EducationScreen(
    onBack: () -> Unit,
    isDark: Boolean = false
) {
    val topics = remember {
        listOf(
            EducationalTopic(
                id = "risk",
                title = "Risk Management Mastery",
                category = "Capital Preservation",
                icon = Icons.Default.Security,
                summary = "The math behind the 1% risk rule and position sizing.",
                content = """
                    1. Never Risk More Than 1-2% Per Trade:
                    Even the best trading setups fail 30-40% of the time. By strictly capping risk at 1% of total equity, a losing streak of 5 trades only draws down ~5% of your account, which is easily recoverable.

                    2. Fixed Dollar Risk vs Flexible Lot Sizes:
                    Your lot size should always be calculated from your stop-loss distance, never arbitrarily chosen.
                    Formula: Units = (Account Balance × Risk %) / (Entry Price - Stop Loss Price).

                    3. Positive Expectancy (1:2+ R:R):
                    If your average winner yields 2R and your average loss is 1R, you only need a 35% win rate to be consistently profitable over time.
                """.trimIndent()
            ),
            EducationalTopic(
                id = "psychology",
                title = "Trading Psychology & FOMO",
                category = "Mindset & Emotional Control",
                icon = Icons.Default.Psychology,
                summary = "How systematic checklist scoring overrides destructive emotions.",
                content = """
                    1. The FOMO Trap:
                    Entering a trade after a big green candle without waiting for a retest is the #1 cause of retail drawdown. If the checklist gives you a score below 60, walk away. There is always another setup.

                    2. Revenge Trading Antidote:
                    After taking a loss, the human brain seeks instant dopamine restitution. TradeScore enforces a pre-market max trades rule to force physical pauses and prevent spiral trading.

                    3. Treating Trading as a Business:
                    A professional surgeon follows a checklist before entering an operating room. A pilot follows a checklist before takeoff. Treat your capital with equal professional rigor.
                """.trimIndent()
            ),
            EducationalTopic(
                id = "confluence",
                title = "How to Improve Setup Quality",
                category = "Technical Confluence",
                icon = Icons.Default.TrendingUp,
                summary = "Stacking multiple independent edges before pulling the trigger.",
                content = """
                    1. Higher Timeframe Dominance:
                    Never take a 15m BUY setup against a strong 4H/Daily bearish trend unless it is a confirmed key support bounce with clear structure shift.

                    2. Liquidity Sweeps & Key Levels:
                    High-quality breakouts occur after liquidity has been engineered and swept. Avoid trading in the middle of trading ranges.

                    3. Session Volume Alignment:
                    Forex setups have significantly higher win rates during London (08:00-16:00 GMT) and New York (13:00-21:00 GMT) sessions due to institutional volume and tighter spreads.
                """.trimIndent()
            ),
            EducationalTopic(
                id = "scoring",
                title = "Understanding the Scoring Engine",
                category = "Algorithm & Weights",
                icon = Icons.Default.Calculate,
                summary = "How the 0-100 score translates into trade safety.",
                content = """
                    • 80 - 100 (HIGH QUALITY):
                    All primary pillars (Risk, Strategy, Higher Timeframe, Emotion) are green. These are your 'A+' setups where full planned risk is justified.

                    • 60 - 79 (MODERATE):
                    Setup has merit, but 1-2 criteria are borderline (e.g. news in 45 minutes or lower R:R). Consider reducing position size by 50%.

                    • 0 - 59 (WEAK & DANGER):
                    Statistically proven to incur negative expected value. Passing on these trades is what creates a profitable equity curve.
                """.trimIndent()
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("education_screen")
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
                text = "Trading Playbook & Education",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLight else Color.White
            )
            Box(modifier = Modifier.size(42.dp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(topics, key = { it.id }) { topic ->
                ExpandableTopicCard(
                    topic = topic,
                    isDark = isDark
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ExpandableTopicCard(
    topic: EducationalTopic,
    isDark: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 20.dp,
        onClick = { expanded = !expanded }
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = topic.icon,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = topic.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = topic.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else TextDark
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = PrimaryPurple
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = topic.summary,
                fontSize = 12.sp,
                color = if (isDark) TextLightSecondary else TextDarkSecondary,
                lineHeight = 16.sp
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isDark) Color(0xFF2C2450) else Color(0xFFEBEAF8), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = topic.content,
                        fontSize = 13.sp,
                        color = if (isDark) TextLight else TextDark,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}
