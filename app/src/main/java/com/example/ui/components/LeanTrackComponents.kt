package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.data.entity.UserProfileEntity
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.OrangeFlame
import com.example.ui.theme.SoftMint
import com.example.ui.theme.WarmBackground
import com.example.ui.theme.WarmSurface
import com.example.ui.theme.WaterBlue

@Composable
fun LeanCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = WarmSurface,
    borderColor: Color = CardBorder,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

@Composable
fun WeightGoalCard(
    currentWeight: Float,
    targetWeight: Float,
    startingWeight: Float,
    onLogClick: () -> Unit
) {
    val remainingToGoal = (currentWeight - targetWeight).coerceAtLeast(0f)
    val totalToLose = (startingWeight - targetWeight).coerceAtLeast(0.1f)
    val lostSoFar = (startingWeight - currentWeight).coerceAtLeast(0f)
    val progressFraction = (lostSoFar / totalToLose).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "weight_progress")

    LeanCard(
        modifier = Modifier.testTag("weight_card"),
        backgroundColor = WarmSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Current Weight",
                    style = MaterialTheme.typography.labelMedium,
                    color = CharcoalMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format("%.1f", currentWeight),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Text(
                        text = " kg",
                        style = MaterialTheme.typography.titleMedium,
                        color = CharcoalMuted,
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MintSubtle,
                modifier = Modifier.clickable { onLogClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "↓ ${String.format("%.1f", remainingToGoal)} kg to first goal",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress bar with starting and goal endpoints
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE9EBE2))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(ForestGreen)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${String.format("%.1f", currentWeight)} kg",
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
                Text(
                    text = "${(progressFraction * 100).toInt()}% on track",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = SoftMint
                )
                Text(
                    text = "${String.format("%.1f", targetWeight)} kg",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
            }
        }
    }
}

@Composable
fun TodayScoreCard(
    dailyLog: DailyLogEntity,
    onToggleHabit: (String) -> Unit
) {
    var completedCount = 0
    if (dailyLog.habitNutrition) completedCount++
    if (dailyLog.habitWorkout) completedCount++
    if (dailyLog.habitSteps) completedCount++
    if (dailyLog.habitWater) completedCount++

    val scoreTitle = when (completedCount) {
        4 -> "Perfect Day! 🌟"
        3 -> "Good Day 🔥"
        2 -> "Solid Progress 💪"
        else -> "Keep Going 🌿"
    }

    LeanCard(modifier = Modifier.testTag("today_score_card")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Score",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Text(
                text = "🔥 $completedCount/4 — $scoreTitle",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Habits list
        HabitToggleRow(
            emoji = "🥗",
            name = "Nutrition",
            subtitle = "Under target kcal + high protein",
            isDone = dailyLog.habitNutrition,
            onClick = { onToggleHabit("nutrition") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitToggleRow(
            emoji = "🏋️",
            name = "Workout",
            subtitle = "Full body home resistance completed",
            isDone = dailyLog.habitWorkout,
            onClick = { onToggleHabit("workout") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitToggleRow(
            emoji = "🚶",
            name = "Steps",
            subtitle = "${dailyLog.stepsWalked} / 8,000 steps logged",
            isDone = dailyLog.habitSteps,
            isPending = !dailyLog.habitSteps,
            onClick = { onToggleHabit("steps") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitToggleRow(
            emoji = "💧",
            name = "Water",
            subtitle = "${dailyLog.waterConsumedL} / 2.5 L hydrated",
            isDone = dailyLog.habitWater,
            onClick = { onToggleHabit("water") }
        )
    }
}

@Composable
fun HabitToggleRow(
    emoji: String,
    name: String,
    subtitle: String,
    isDone: Boolean,
    isPending: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDone) MintSubtle.copy(alpha = 0.5f) else Color(0xFFF7F8F4))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Charcoal
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
            }
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isDone) SoftMint else if (isPending) AmberWarning.copy(alpha = 0.2f) else Color(0xFFE2E4DE)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else if (isPending) {
                Text(text = "⏳", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun NutritionSummaryCard(
    caloriesConsumed: Int,
    targetCalories: Int,
    proteinConsumed: Int,
    targetProtein: Int,
    waterConsumed: Float,
    targetWater: Float,
    onAddWater: (Float) -> Unit
) {
    LeanCard(modifier = Modifier.testTag("nutrition_card")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Nutrition",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Text(
                text = "Calculated Deficit",
                style = MaterialTheme.typography.labelSmall,
                color = SoftMint,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Calories bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Calories",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Charcoal
            )
            Text(
                text = "$caloriesConsumed / $targetCalories kcal",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        val calProgress = (caloriesConsumed.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { calProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ForestGreen,
            trackColor = Color(0xFFE9EBE2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Protein & Water dual cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Protein
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF7F8F4))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Protein",
                        style = MaterialTheme.typography.labelSmall,
                        color = CharcoalMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$proteinConsumed / $targetProtein g",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val proteinProgress = (proteinConsumed.toFloat() / targetProtein.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { proteinProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = OrangeFlame,
                        trackColor = Color(0xFFE9EBE2)
                    )
                }
            }

            // Water
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF7F8F4))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Water",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        // Quick +250ml button
                        Surface(
                            shape = CircleShape,
                            color = WaterBlue.copy(alpha = 0.15f),
                            modifier = Modifier.clickable { onAddWater(0.25f) }
                        ) {
                            Text(
                                text = "+0.25L",
                                style = MaterialTheme.typography.labelSmall,
                                color = WaterBlue,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${String.format("%.1f", waterConsumed)} / ${String.format("%.1f", targetWater)} L",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val waterProgress = (waterConsumed / targetWater).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { waterProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = WaterBlue,
                        trackColor = Color(0xFFE9EBE2)
                    )
                }
            }
        }
    }
}

@Composable
fun TodayWorkoutCard(
    onStartWorkout: () -> Unit
) {
    LeanCard(
        modifier = Modifier.testTag("today_workout_card"),
        backgroundColor = ForestGreen,
        borderColor = ForestGreen
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Today's Workout",
                    style = MaterialTheme.typography.labelMedium,
                    color = MintLight
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Full Body — 30 min",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "5 exercises • Home / No equipment",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD6E3D8)
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏋️", fontSize = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onStartWorkout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = ForestGreen
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("start_workout_button")
        ) {
            Text(
                text = "Start Workout",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun WalkingMovementCard(
    stepsWalked: Int,
    targetSteps: Int,
    onAddSteps: (Int) -> Unit
) {
    val progress = (stepsWalked.toFloat() / targetSteps.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()
    val remainingSteps = (targetSteps - stepsWalked).coerceAtLeast(0)

    LeanCard(modifier = Modifier.testTag("movement_card")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Movement",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F3EC),
                    modifier = Modifier.clickable { onAddSteps(500) }
                ) {
                    Text(
                        text = "+500",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Charcoal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F3EC),
                    modifier = Modifier.clickable { onAddSteps(1000) }
                ) {
                    Text(
                        text = "+1,000",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Charcoal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = String.format("%,d", stepsWalked),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )
                Text(
                    text = " / ${String.format("%,d", targetSteps)} steps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted,
                    modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                )
            }
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoftMint
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = SoftMint,
            trackColor = Color(0xFFE9EBE2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (remainingSteps > 0) "🚶 $remainingSteps steps to your goal" else "🎉 Goal reached! Fantastic walking!",
            style = MaterialTheme.typography.labelMedium,
            color = if (remainingSteps > 0) CharcoalMuted else ForestGreen,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FoodReplacementDialog(
    meal: MealEntity,
    onDismiss: () -> Unit,
    onSelectReplacement: (newTitle: String, newDesc: String, newCalories: Int, newProtein: Float, note: String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Smart Substitution",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Replacing: ${meal.title}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Realistic coaching tip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MintSubtle)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "💡 LeanTrack never tells you \"You can't eat this.\" Instead: Try a smaller portion + add protein and vegetables.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select a flexible alternative:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Charcoal
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Replacement Option 1: Fish -> Chicken
                ReplacementOptionCard(
                    title = "Chicken Curry (150g) + Veg",
                    subtitle = "High lean protein, rich flavor",
                    calories = 360,
                    protein = 32f,
                    onClick = {
                        onSelectReplacement(
                            "Chicken Curry + Vegetables",
                            "150g shallow-cooked chicken breast + leafy greens",
                            360,
                            32f,
                            "Substituted Fish with Lean Chicken"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Replacement Option 2: Roti -> Controlled Rice Portion
                ReplacementOptionCard(
                    title = "Controlled Rice (1/2 cup cooked)",
                    subtitle = "Enjoy rice with exact portion balance",
                    calories = 105,
                    protein = 2.2f,
                    onClick = {
                        onSelectReplacement(
                            "Controlled Cooked Rice (1/2 cup)",
                            "1/2 cup cooked rice (~80g) with protein & sabzi",
                            105,
                            2.2f,
                            "Substituted Roti with Controlled Rice portion"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Replacement Option 3: Family Meal Portion Control Mode
                ReplacementOptionCard(
                    title = "Family Meal (Portion Control Mode)",
                    subtitle = "1/2 plate vegetables + palm-size meat/fish + modest carbs",
                    calories = 450,
                    protein = 28f,
                    onClick = {
                        onSelectReplacement(
                            "Family Meal (Portion Controlled)",
                            "Eat mindfully with family: Half plate greens, 1 piece meat, small rice/roti",
                            450,
                            28f,
                            "Portion Control Mode with family"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ReplacementOptionCard(
    title: String,
    subtitle: String,
    calories: Int,
    protein: Float,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF7F8F4),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$calories kcal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Text(
                    text = "${protein.toInt()}g protein",
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
            }
        }
    }
}

@Composable
fun LogWeightDialog(
    currentWeight: Float,
    currentWaist: Float,
    onDismiss: () -> Unit,
    onConfirm: (weight: Float, waist: Float?) -> Unit
) {
    var weightInput by remember { mutableStateOf(currentWeight.toString()) }
    var waistInput by remember { mutableStateOf(currentWaist.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Log Today's Measurements",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Weigh yourself in the morning for consistent tracking.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = waistInput,
                    onValueChange = { waistInput = it },
                    label = { Text("Waist circumference (cm) - Optional") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = CharcoalMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val w = weightInput.toFloatOrNull() ?: currentWeight
                            val waist = waistInput.toFloatOrNull()
                            onConfirm(w, waist)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                    ) {
                        Text("Save Entry")
                    }
                }
            }
        }
    }
}
