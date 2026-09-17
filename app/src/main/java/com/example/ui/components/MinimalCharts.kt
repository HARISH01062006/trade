package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextMuted

data class BarChartItem(
    val label: String,
    val value: Float, // 0f to 1f
    val displayValue: String = ""
)

@Composable
fun MinimalBarChart(
    items: List<BarChartItem>,
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    barColor: Color = PrimaryPurpleLight,
    selectedBarColor: Color = PrimaryPurple
) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(items) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "bar_chart_anim"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEachIndexed { index, item ->
                val isHighest = item.value == items.maxOfOrNull { it.value }
                val targetHeight = item.value.coerceIn(0.1f, 1f) * animatedProgress

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    if (item.displayValue.isNotEmpty()) {
                        Text(
                            text = item.displayValue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighest) PrimaryPurple else TextMuted,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .fillMaxHeight(targetHeight)
                            .background(
                                color = if (isHighest) selectedBarColor else barColor.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEach { item ->
                Text(
                    text = item.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MinimalLineChart(
    dataPoints: List<Float>, // normalized 0f to 1f
    labels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = PrimaryPurple,
    highlightIndex: Int = 3
) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(dataPoints) {
        animationPlayed = true
    }

    val progress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "line_chart_anim"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (dataPoints.size < 2) return@Canvas

                val width = size.width
                val height = size.height
                val stepX = width / (dataPoints.size - 1)

                val points = dataPoints.mapIndexed { index, value ->
                    val y = height - (value * height * 0.8f * progress) - (height * 0.1f)
                    Offset(index * stepX, y)
                }

                // Draw background grid lines
                for (i in 1..3) {
                    val y = height * (i / 4f)
                    drawLine(
                        color = Color(0x157568EB),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Build smooth path
                val strokePath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val cur = points[i]
                        val control1 = Offset(prev.x + (cur.x - prev.x) / 2f, prev.y)
                        val control2 = Offset(prev.x + (cur.x - prev.x) / 2f, cur.y)
                        cubicTo(control1.x, control1.y, control2.x, control2.y, cur.x, cur.y)
                    }
                }

                // Gradient fill underneath
                val fillPath = Path().apply {
                    addPath(strokePath)
                    lineTo(points.last().x, height)
                    lineTo(points.first().x, height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.28f), Color.Transparent),
                        startY = 0f,
                        endY = height
                    )
                )

                // Draw stroke
                drawPath(
                    path = strokePath,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Highlight dot
                if (highlightIndex in points.indices && progress > 0.8f) {
                    val highlightPoint = points[highlightIndex]
                    drawCircle(
                        color = Color.White,
                        radius = 8.dp.toPx(),
                        center = highlightPoint
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 5.dp.toPx(),
                        center = highlightPoint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = TextDarkSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
