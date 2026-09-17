package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmbientShadow
import com.example.ui.theme.CardSurfaceLight
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.CrispBorderLight
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceElevated
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SoftShadowPurple
import com.example.ui.theme.SubtleCardBorder
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextLight

@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 26.dp,
    elevation: Dp = 10.dp,
    isDark: Boolean = false,
    backgroundColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val bgColor = backgroundColor ?: if (isDark) DarkCardSurface else CardSurfaceWhite
    val borderColor = if (isDark) DarkBorder else CrispBorderLight
    val shadowColor = if (isDark) Color(0x66000000) else SoftShadowPurple

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && onClick != null) 0.98f else 1f, label = "card_scale")

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .background(bgColor, shape)
            .border(1.2.dp, borderColor, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun NeumorphicHighlightCard(
    modifier: Modifier = Modifier,
    gradient: Brush,
    cornerRadius: Dp = 26.dp,
    elevation: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && onClick != null) 0.98f else 1f, label = "hl_card_scale")

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = SoftShadowPurple,
                spotColor = SoftShadowPurple
            )
            .background(gradient, shape)
            .border(1.2.dp, Color(0x4DFFFFFF), shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun NeumorphicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = PrimaryButtonGradient,
    textColor: Color = TextLight,
    height: Dp = 54.dp,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(27.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && enabled) 0.96f else 1f, label = "btn_scale")

    Box(
        modifier = modifier
            .scale(scale)
            .height(height)
            .shadow(
                elevation = if (enabled) 10.dp else 2.dp,
                shape = shape,
                ambientColor = SoftShadowPurple,
                spotColor = SoftShadowPurple
            )
            .background(if (enabled) gradient else Brush.horizontalGradient(listOf(Color(0xFFB4AEE8), Color(0xFFC7C2F0))), shape)
            .border(1.dp, Color(0x40FFFFFF), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Box(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun NeumorphicIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    size: Dp = 46.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "icon_btn_scale")

    val bgColor = if (isDark) DarkCardSurfaceElevated else CardSurfaceWhite
    val borderColor = if (isDark) DarkBorder else CrispBorderLight

    Box(
        modifier = modifier
            .scale(scale)
            .size(size)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = AmbientShadow,
                spotColor = AmbientShadow
            )
            .background(bgColor, shape)
            .border(1.dp, borderColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun NeumorphicSegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    val containerShape = RoundedCornerShape(22.dp)
    val containerBg = if (isDark) DarkCardSurfaceElevated else Color(0xFFEBEBF7)

    Row(
        modifier = modifier
            .background(containerBg, containerShape)
            .border(1.dp, if (isDark) DarkBorder else SubtleCardBorder, containerShape)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            val itemShape = RoundedCornerShape(18.dp)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .then(
                        if (isSelected) {
                            Modifier
                                .shadow(4.dp, itemShape, ambientColor = AmbientShadow, spotColor = AmbientShadow)
                                .background(if (isDark) PrimaryPurple else CardSurfaceWhite, itemShape)
                                .border(1.dp, if (isDark) Color(0x33FFFFFF) else CrispBorderLight, itemShape)
                        } else {
                            Modifier
                        }
                    )
                    .clip(itemShape)
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        isSelected && isDark -> Color.White
                        isSelected -> PrimaryPurple
                        isDark -> Color(0xFFB2A9E6)
                        else -> Color(0xFF6B658E)
                    }
                )
            }
        }
    }
}
