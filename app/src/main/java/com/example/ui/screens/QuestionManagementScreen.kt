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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.QuestionEntity
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.theme.CrispBorderLight
import com.example.ui.theme.PrimaryButtonGradient
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SetupDanger
import com.example.ui.theme.SetupHighQuality
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.TradeScoreViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionManagementScreen(
    viewModel: TradeScoreViewModel,
    onBack: () -> Unit,
    isDark: Boolean = false
) {
    val allQuestions by viewModel.allQuestions.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingQuestion by remember { mutableStateOf<QuestionEntity?>(null) }

    val categories = listOf(
        "Strategy",
        "Technical Analysis",
        "Risk Management",
        "Psychology",
        "Market Conditions",
        "Trade Management"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("question_management_screen")
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
                text = "Checklist Questions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextLight else Color.White
            )
            NeumorphicIconButton(
                onClick = { showAddDialog = true },
                isDark = isDark,
                size = 42.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Question",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Summary Info Card
        NeumorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Dynamic Assessment Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${allQuestions.count { it.isActive }} Active • ${allQuestions.size} Total Questions", fontSize = 11.sp, color = TextMuted)
                }
                Box(
                    modifier = Modifier
                        .background(PrimaryPurple.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Editable DB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Questions List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allQuestions, key = { it.id }) { question ->
                QuestionItemCard(
                    question = question,
                    isDark = isDark,
                    onToggleActive = { viewModel.toggleQuestionActive(question) },
                    onEdit = { editingQuestion = question },
                    onDelete = { viewModel.deleteQuestion(question) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // ADD QUESTION DIALOG
    if (showAddDialog) {
        QuestionEditorDialog(
            title = "Add Checklist Question",
            initialText = "",
            initialCategory = "Strategy",
            initialWeight = 10,
            categories = categories,
            onDismiss = { showAddDialog = false },
            onConfirm = { text, category, weight ->
                viewModel.addQuestion(text, category, weight)
                showAddDialog = false
            }
        )
    }

    // EDIT QUESTION DIALOG
    editingQuestion?.let { q ->
        QuestionEditorDialog(
            title = "Edit Question",
            initialText = q.text,
            initialCategory = q.category,
            initialWeight = q.weight,
            categories = categories,
            onDismiss = { editingQuestion = null },
            onConfirm = { text, category, weight ->
                viewModel.updateQuestion(
                    q.copy(
                        text = text,
                        category = category,
                        weight = weight,
                        yesScore = weight
                    )
                )
                editingQuestion = null
            }
        )
    }
}

@Composable
fun QuestionItemCard(
    question: QuestionEntity,
    isDark: Boolean = false,
    onToggleActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    NeumorphicCard(
        modifier = Modifier.fillMaxWidth(),
        isDark = isDark,
        cornerRadius = 18.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = question.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${question.weight} pts",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (question.isActive) SetupHighQuality else TextMuted,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = question.isActive,
                        onCheckedChange = { onToggleActive() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryPurple,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color(0xFFE2E0FB)
                        ),
                        modifier = Modifier.size(38.dp, 24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (question.isActive) (if (isDark) TextLight else TextDark) else TextMuted,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                NeumorphicIconButton(
                    onClick = onEdit,
                    isDark = isDark,
                    size = 32.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                NeumorphicIconButton(
                    onClick = onDelete,
                    isDark = isDark,
                    size = 32.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = SetupDanger,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionEditorDialog(
    title: String,
    initialText: String,
    initialCategory: String,
    initialWeight: Int,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (text: String, category: String, weight: Int) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var weight by remember { mutableFloatStateOf(initialWeight.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) PrimaryPurple else Color(0xFFEBEAF8),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else PrimaryPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Score Weight", fontSize = 12.sp, color = TextMuted)
                    Text("${weight.toInt()} points", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                }
                Slider(
                    value = weight,
                    onValueChange = { weight = it },
                    valueRange = 5f..20f,
                    steps = 14,
                    colors = SliderDefaults.colors(thumbColor = PrimaryPurple, activeTrackColor = PrimaryPurple)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onConfirm(text.trim(), selectedCategory, weight.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("Save Question")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
