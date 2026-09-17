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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity
import com.example.data.model.UserSettingsEntity
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.CrispBorderLight
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel
import java.util.Locale

@Composable
fun ProfileSettingsScreen(
    viewModel: TradeScoreViewModel,
    onNavigateToQuestions: () -> Unit,
    onNavigateToEducation: () -> Unit,
    isDark: Boolean = false
) {
    val user by viewModel.user.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val currentUser = user ?: UserEntity()
    val currentSettings = settings ?: UserSettingsEntity()

    var displayName by remember(currentUser) { mutableStateOf(currentUser.displayName) }
    var experienceLevel by remember(currentUser) { mutableStateOf(currentUser.experienceLevel) }
    var defaultRisk by remember(currentSettings) { mutableDoubleStateOf(currentSettings.defaultRiskPercentage) }
    var defaultBalance by remember(currentSettings) { mutableStateOf(currentSettings.defaultAccountBalance.toInt().toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("profile_settings_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Trader Profile & Settings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) TextLight else Color.White
                )
                Text(
                    text = "Risk preferences & customization",
                    fontSize = 12.sp,
                    color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC)
                )
            }
        }

        // ==========================================
        // PROFILE CARD
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            elevation = 12.dp
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple)
                            .border(2.dp, Color(0x40FFFFFF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.avatarInitials,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.displayName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextLight else TextDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Verified Trader",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = currentUser.title,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .background(SetupHighQuality.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Discipline Index: ${currentUser.disciplineScore}/100",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SetupHighQuality
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Trader Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = experienceLevel,
                    onValueChange = { experienceLevel = it },
                    label = { Text("Experience Level") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // NAVIGATION SHORTCUTS: Questions & Education
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            onClick = onNavigateToQuestions
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListBulleted,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Checklist Questions Database",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Text(
                            text = "Edit, add, or adjust question weights",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            onClick = onNavigateToEducation
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(PrimaryPurple.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Trading Psychology & Education",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Text(
                            text = "Guides on risk management & FOMO",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // TRADING PREFERENCES
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text(
                    text = "DEFAULT RISK & ACCOUNT PARAMETERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Default Risk Per Trade", fontSize = 12.sp, color = TextDarkSecondary)
                    Text(
                        text = String.format(Locale.US, "%.1f%%", defaultRisk),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                }
                Slider(
                    value = defaultRisk.toFloat(),
                    onValueChange = { defaultRisk = it.toDouble() },
                    valueRange = 0.25f..3.0f,
                    steps = 10,
                    colors = SliderDefaults.colors(thumbColor = PrimaryPurple, activeTrackColor = PrimaryPurple)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = defaultBalance,
                    onValueChange = { defaultBalance = it },
                    label = { Text("Default Account Balance ($)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // APP THEME PREFERENCES
        // ==========================================
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isDark) "Deep Violet Dark Mode" else "Lavender Neumorphic Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) TextLight else TextDark
                        )
                        Text(
                            text = if (isDark) "Dark canvas style" else "Reference visual theme active",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PrimaryPurple
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save Profile & Preferences Button
        NeumorphicButton(
            text = "SAVE PROFILE SETTINGS",
            onClick = {
                viewModel.updateUserProfile(
                    displayName = displayName,
                    experienceLevel = experienceLevel,
                    preferredMarket = currentUser.preferredMarket,
                    preferredStrategy = currentUser.preferredStrategy
                )
                val bal = defaultBalance.toDoubleOrNull() ?: currentSettings.defaultAccountBalance
                viewModel.updateUserSettings(
                    defaultRisk = defaultRisk,
                    defaultBalance = bal,
                    defaultMarket = currentSettings.defaultMarket,
                    defaultStrategy = currentSettings.defaultStrategy
                )
            },
            modifier = Modifier.fillMaxWidth(),
            gradient = PrimaryButtonGradient
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}
