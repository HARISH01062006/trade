package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ScoreArchGauge(
    score: Int,
    maxScore: Int = 100,
    modifier: Modifier = Modifier,
    title: String = "HISTORICAL SETUP QUALITY",
    subtitle: String = "Based on pre-trade evaluations",
    badgeLabel: String = "ALL-TIME",
    leftMetricTitle: String = "Setups Checked",
    leftMetricValue: String = "12",
    leftMetricSubtitle: String = "Evaluated",
    centerMetricTitle: String? = "Discipline",
    centerMetricValue: String? = "86%",
    centerMetricSubtitle: String? = "Consistency",
    rightMetricTitle: String = "High Quality",
    rightMetricValue: String = "8",
    rightMetricSubtitle: String = "Score ≥ 80",
    isDark: Boolean = false
) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(score) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "arch_score_anim"
    )

    val scoreColor = when {
        score >= 80 -> SetupHighQuality
        score >= 60 -> SetupModerate
        score >= 40 -> SetupWeak
        score >= 20 -> SetupVeryWeak
        else -> SetupDanger
    }

    val trackColor = if (isDark) Color(0xFF2C2450) else Color(0xFFE8E5FB)
    val tickInactiveColor = if (isDark) Color(0xFF383060) else Color(0xFFDCD7F8)
    val textColor = if (isDark) TextLight else TextDark
    val subTextColor = if (isDark) TextLightSecondary else TextDarkSecondary
    val insetBg = if (isDark) Color(0xFF1E173D) else Color(0xFFF3F1FD)
    val insetBorder = if (isDark) Color(0x33FFFFFF) else Color(0x1A7061E7)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // -------------------------------------------------------------
        // TOP CARD HEADER: Title, Subtitle & Badge (Cleanly outside arch)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                    .border(1.dp, PrimaryPurple.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badgeLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // -------------------------------------------------------------
        // SEMICIRCLE ARCH GAUGE: Pixel-perfect geometry with ticks & glow bead
        // -------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                val strokeWidth = 14.dp.toPx()
                val radius = (size.width.coerceAtMost(320.dp.toPx()) - strokeWidth * 2) / 2f
                val centerX = size.width / 2f
                val centerY = size.height - 12.dp.toPx()

                val topLeft = Offset(centerX - radius, centerY - radius)
                val arcSize = Size(radius * 2, radius * 2)

                // 1. Sleek graduation ticks around the perimeter
                val tickCount = 20 // every 5%
                val tickRadiusInner = radius + strokeWidth / 2 + 5.dp.toPx()
                for (i in 0..tickCount) {
                    val tickFraction = i.toFloat() / tickCount
                    val angleDeg = 180f + 180f * tickFraction
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val isMajor = i % 5 == 0
                    val tickLen = if (isMajor) 7.dp.toPx() else 4.dp.toPx()
                    val tickColor = if (tickFraction <= animatedProgress) {
                        scoreColor.copy(alpha = if (isMajor) 0.9f else 0.55f)
                    } else {
                        tickInactiveColor
                    }

                    val x1 = centerX + (tickRadiusInner) * cos(angleRad).toFloat()
                    val y1 = centerY + (tickRadiusInner) * sin(angleRad).toFloat()
                    val x2 = centerX + (tickRadiusInner + tickLen) * cos(angleRad).toFloat()
                    val y2 = centerY + (tickRadiusInner + tickLen) * sin(angleRad).toFloat()

                    drawLine(
                        color = tickColor,
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = if (isMajor) 2.dp.toPx() else 1.2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                // 2. Inactive Background Track (180 degrees: 180° to 360°)
                drawArc(
                    color = trackColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 3. Active Gradient Progress Arc
                val sweep = 180f * animatedProgress
                if (sweep > 0f) {
                    drawArc(
                        brush = Brush.horizontalGradient(
                            listOf(PrimaryPurpleLight, scoreColor),
                            startX = topLeft.x,
                            endX = topLeft.x + arcSize.width
                        ),
                        startAngle = 180f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // 4. Glowing Indicator Bead at current score tip
                    val tipAngleRad = Math.toRadians((180f + sweep).toDouble())
                    val tipX = centerX + radius * cos(tipAngleRad).toFloat()
                    val tipY = centerY + radius * sin(tipAngleRad).toFloat()

                    // Outer halo
                    drawCircle(
                        color = scoreColor.copy(alpha = 0.35f),
                        radius = 8.dp.toPx(),
                        center = Offset(tipX, tipY)
                    )
                    // Inner bright core
                    drawCircle(
                        color = Color.White,
                        radius = 4.5.dp.toPx(),
                        center = Offset(tipX, tipY)
                    )
                }
            }

            // Central Score Typography inside the dome
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SETUP SCORE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${(animatedProgress * maxScore).toInt()}",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        lineHeight = 44.sp
                    )
                    Text(
                        text = "/$maxScore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = subTextColor,
                        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Status badge capsule pill
                val (statusText, statusDesc) = when {
                    score >= 80 -> Pair("HIGH QUALITY SETUP", "A+ Grade • Strong Confirmation")
                    score >= 60 -> Pair("MODERATE SETUP", "B Grade • Exercise Caution")
                    score >= 40 -> Pair("WEAK SETUP", "C Grade • Incomplete Confluence")
                    score >= 20 -> Pair("VERY WEAK SETUP", "D Grade • High Risk")
                    else -> Pair("DANGER — AVOID TRADE", "F Grade • Violates Rules")
                }

                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
                        .border(1.2.dp, scoreColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(scoreColor, CircleShape)
                        )
                        Text(
                            text = statusText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // SUB-METRICS TILES: Neumorphic Inset Pill Containers
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(insetBg, RoundedCornerShape(18.dp))
                .border(1.dp, insetBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Metric: Setups Checked
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = leftMetricTitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = leftMetricValue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
                Text(
                    text = leftMetricSubtitle,
                    fontSize = 10.sp,
                    color = subTextColor
                )
            }

            // Vertical Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(if (isDark) Color(0x22FFFFFF) else Color(0x1A7061E7))
            )

            // Center Metric (if provided)
            if (centerMetricTitle != null && centerMetricValue != null) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = centerMetricTitle,
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = centerMetricValue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryPurple
                    )
                    Text(
                        text = centerMetricSubtitle ?: "",
                        fontSize = 10.sp,
                        color = subTextColor
                    )
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(if (isDark) Color(0x22FFFFFF) else Color(0x1A7061E7))
                )
            }

            // Right Metric: High Quality
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = rightMetricTitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = rightMetricValue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SetupHighQuality
                )
                Text(
                    text = rightMetricSubtitle,
                    fontSize = 10.sp,
                    color = subTextColor
                )
            }
        }
    }
}

@Composable
fun CircularScoreGauge(
    score: Int,
    maxScore: Int = 100,
    sizeDp: Int = 180,
    isDark: Boolean = false,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(score) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "circular_score_anim"
    )

    val scoreColor = when {
        score >= 80 -> SetupHighQuality
        score >= 60 -> SetupModerate
        score >= 40 -> SetupWeak
        score >= 20 -> SetupVeryWeak
        else -> SetupDanger
    }

    val trackColor = if (isDark) Color(0xFF2C2450) else Color(0xFFE5E2FC)
    val textColor = if (isDark) TextLight else TextDark

    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizeDp.dp)) {
            val strokeWidth = 16.dp.toPx()
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            // Background full ring
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Foreground animated arc
            val sweep = 360f * animatedProgress
            if (sweep > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(PrimaryPurpleLight, scoreColor, PrimaryPurple)
                    ),
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress * maxScore).toInt()}",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
            Text(
                text = "OUT OF $maxScore",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
            )
        }
    }
}

