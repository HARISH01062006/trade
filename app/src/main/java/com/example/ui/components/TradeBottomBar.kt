package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmbientShadow
import com.example.ui.theme.CardSurfaceLight
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.CrispBorderLight
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SoftShadowPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

enum class MainTab(
    val title: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    CHECK("Check", Icons.Default.Speed),
    JOURNAL("Journal", Icons.Default.Book),
    ANALYTICS("Analytics", Icons.Default.Analytics),
    PROFILE("Profile", Icons.Default.Person)
}

@Composable
fun TradeBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    isDark: Boolean = false,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(32.dp)
    val bgColor = if (isDark) DarkCardSurface else CardSurfaceWhite
    val borderColor = if (isDark) DarkBorder else CrispBorderLight

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(
                elevation = 14.dp,
                shape = barShape,
                ambientColor = SoftShadowPurple,
                spotColor = SoftShadowPurple
            )
            .background(bgColor, barShape)
            .border(1.2.dp, borderColor, barShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = tab == currentTab
                val itemShape = RoundedCornerShape(20.dp)

                val itemBgColor by animateColorAsState(
                    targetValue = when {
                        isSelected && isDark -> PrimaryPurple.copy(alpha = 0.25f)
                        isSelected -> PrimaryPurple.copy(alpha = 0.12f)
                        else -> Color.Transparent
                    },
                    label = "tab_bg_anim"
                )

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) PrimaryPurple else TextMuted,
                    label = "tab_color_anim"
                )

                Box(
                    modifier = Modifier
                        .clip(itemShape)
                        .background(itemBgColor, itemShape)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        AnimatedVisibility(visible = isSelected) {
                            Row {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
