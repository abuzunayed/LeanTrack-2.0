package com.example.ui.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.UserProfileEntity
import com.example.ui.components.NutritionSummaryCard
import com.example.ui.components.TodayScoreCard
import com.example.ui.components.TodayWorkoutCard
import com.example.ui.components.WalkingMovementCard
import com.example.ui.components.WeightGoalCard
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.WarmBackground

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity,
    todayLog: DailyLogEntity,
    onToggleHabit: (String) -> Unit,
    onAddWater: (Float) -> Unit,
    onAddSteps: (Int) -> Unit,
    onStartWorkout: () -> Unit,
    onLogWeightClick: () -> Unit
) {
    Scaffold(
        containerColor = WarmBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onLogWeightClick,
                containerColor = ForestGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_log_weight")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Log Measurements")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("home_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Header: Good morning, Ratul 👋 / Small steps. Big changes.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good morning, ${userProfile.name} 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Small steps. Big changes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CharcoalMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MintSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌱", fontSize = 20.sp)
                    }
                }
            }

            // Weight card
            item {
                WeightGoalCard(
                    currentWeight = userProfile.currentWeightKg,
                    targetWeight = userProfile.targetWeightKg,
                    startingWeight = userProfile.startingWeightKg,
                    onLogClick = onLogWeightClick
                )
            }

            // Today's score (Habits: Nutrition, Workout, Steps, Water)
            item {
                TodayScoreCard(
                    dailyLog = todayLog,
                    onToggleHabit = onToggleHabit
                )
            }

            // Today's nutrition
            item {
                NutritionSummaryCard(
                    caloriesConsumed = todayLog.caloriesConsumed,
                    targetCalories = userProfile.targetCalories,
                    proteinConsumed = todayLog.proteinConsumedG,
                    targetProtein = userProfile.targetProteinG,
                    waterConsumed = todayLog.waterConsumedL,
                    targetWater = userProfile.targetWaterL,
                    onAddWater = onAddWater
                )
            }

            // Today's workout
            item {
                TodayWorkoutCard(
                    onStartWorkout = onStartWorkout
                )
            }

            // Today's movement
            item {
                WalkingMovementCard(
                    stepsWalked = todayLog.stepsWalked,
                    targetSteps = userProfile.targetSteps,
                    onAddSteps = onAddSteps
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp)) // padding for bottom nav & FAB
            }
        }
    }
}
