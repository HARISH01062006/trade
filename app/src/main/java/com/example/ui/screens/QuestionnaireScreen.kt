package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.SetupDanger
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.SoftShadowPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel

@Composable
fun QuestionnaireScreen(
    viewModel: TradeScoreViewModel,
    onBack: () -> Unit,
    onScoreCalculated: () -> Unit,
    isDark: Boolean = false
) {
    val questions by viewModel.activeQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val answersMap by viewModel.answersMap.collectAsState()
    val symbol by viewModel.draftSymbol.collectAsState()
    val direction by viewModel.draftDirection.collectAsState()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading questions...", color = Color.White)
        }
        return
    }

    val totalQuestions = questions.size
    val currentQuestion = questions.getOrNull(currentIndex) ?: questions.first()
    val currentAnswer = answersMap[currentQuestion.id]
    val progress = (currentIndex + 1).toFloat() / totalQuestions.toFloat()
    val isLastQuestion = currentIndex == totalQuestions - 1
    val isCurrentAnswered = currentAnswer != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag("questionnaire_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Back button, Symbol pill, Progress text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NeumorphicIconButton(
                onClick = {
                    if (currentIndex > 0) {
                        viewModel.goToPreviousQuestion()
                    } else {
                        onBack()
                    }
                },
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

            // Symbol & Direction badge
            Box(
                modifier = Modifier
                    .background(Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$symbol • ${direction.name}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = "${currentIndex + 1} / $totalQuestions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLightSecondary else Color.White
            )
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color.White,
            trackColor = Color(0x33FFFFFF)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Question Card (Animated per question)
        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                } else {
                    (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                }
            },
            label = "question_transition",
            modifier = Modifier.weight(1f)
        ) { targetIndex ->
            val q = questions.getOrNull(targetIndex) ?: currentQuestion

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NeumorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    isDark = isDark,
                    cornerRadius = 28.dp,
                    elevation = 16.dp,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(26.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Category Pill
                        Box(
                            modifier = Modifier
                                .background(PrimaryPurple.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                                .border(1.dp, PrimaryPurple.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = q.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "QUESTION ${targetIndex + 1} OF $totalQuestions",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = q.text,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextLight else TextDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Question Weight / impact indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(PrimaryPurple, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Score Impact: +${q.weight} pts",
                                fontSize = 12.sp,
                                color = TextDarkSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Interactive YES / NO Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // YES BUTTON
            val isSelectedYes = currentAnswer == true
            LargeDecisionButton(
                text = "YES",
                icon = Icons.Default.Check,
                isSelected = isSelectedYes,
                selectedColor = SetupHighQuality,
                defaultColor = Color(0xFFE8F5E9),
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_yes"),
                onClick = {
                    viewModel.answerQuestion(currentQuestion.id, true)
                }
            )

            // NO BUTTON
            val isSelectedNo = currentAnswer == false
            LargeDecisionButton(
                text = "NO",
                icon = Icons.Default.Close,
                isSelected = isSelectedNo,
                selectedColor = SetupDanger,
                defaultColor = Color(0xFFFFEBEE),
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_no"),
                onClick = {
                    viewModel.answerQuestion(currentQuestion.id, false)
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // At final question (or when all answered): SHOW MY SCORE Button
        if (isLastQuestion && isCurrentAnswered) {
            NeumorphicButton(
                text = "SHOW MY SCORE",
                onClick = {
                    viewModel.finishAssessment {
                        onScoreCalculated()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("show_my_score_button"),
                gradient = PrimaryButtonGradient
            )
        } else if (answersMap.size == totalQuestions) {
            NeumorphicButton(
                text = "CALCULATE FINAL SCORE",
                onClick = {
                    viewModel.finishAssessment {
                        onScoreCalculated()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                gradient = PrimaryButtonGradient
            )
        } else {
            // Subtle helper
            Text(
                text = "Select YES or NO to advance to next check",
                fontSize = 12.sp,
                color = if (isDark) TextLightSecondary else Color(0xFFE2DFFC)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun LargeDecisionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    val bgColor = if (isSelected) selectedColor else Color.White
    val contentColor = if (isSelected) Color.White else selectedColor

    Box(
        modifier = modifier
            .height(76.dp)
            .shadow(
                elevation = if (isSelected) 12.dp else 6.dp,
                shape = shape,
                ambientColor = SoftShadowPurple,
                spotColor = SoftShadowPurple
            )
            .background(bgColor, shape)
            .border(
                width = if (isSelected) 2.dp else 1.2.dp,
                color = if (isSelected) Color.White.copy(alpha = 0.5f) else selectedColor.copy(alpha = 0.3f),
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        if (isSelected) Color(0x33FFFFFF) else selectedColor.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor,
                letterSpacing = 1.sp
            )
        }
    }
}
