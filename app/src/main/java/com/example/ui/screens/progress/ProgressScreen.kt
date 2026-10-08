package com.example.ui.screens.progress

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AchievementEntity
import com.example.data.entity.ProgressPhotoEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.entity.WeightEntryEntity
import com.example.ui.components.LeanCard
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

@Composable
fun ProgressScreen(
    userProfile: UserProfileEntity,
    weightEntries: List<WeightEntryEntity>,
    photos: List<ProgressPhotoEntity>,
    achievements: List<AchievementEntity>,
    smartFeedbackMessage: String,
    onLogWeightClick: () -> Unit,
    onOpenWeeklyReport: () -> Unit
) {
    var selectedSectionTab by remember { mutableIntStateOf(0) } // 0: Metrics & Trajectory, 1: Photos, 2: Achievements

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .testTag("progress_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Transformation Progress",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Track the trends, not single-day fluctuations",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MintSubtle,
                    modifier = Modifier.clickable { onOpenWeeklyReport() }
                ) {
                    Text(
                        text = "Weekly Report 📈",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedSectionTab,
                containerColor = Color.Transparent,
                contentColor = ForestGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedSectionTab]),
                        color = ForestGreen
                    )
                }
            ) {
                Tab(
                    selected = selectedSectionTab == 0,
                    onClick = { selectedSectionTab = 0 },
                    text = { Text("Metrics", fontWeight = if (selectedSectionTab == 0) FontWeight.Bold else FontWeight.Medium) }
                )
                Tab(
                    selected = selectedSectionTab == 1,
                    onClick = { selectedSectionTab = 1 },
                    text = { Text("Photos", fontWeight = if (selectedSectionTab == 1) FontWeight.Bold else FontWeight.Medium) }
                )
                Tab(
                    selected = selectedSectionTab == 2,
                    onClick = { selectedSectionTab = 2 },
                    text = { Text("Milestones 🏆", fontWeight = if (selectedSectionTab == 2) FontWeight.Bold else FontWeight.Medium) }
                )
            }
        }

        when (selectedSectionTab) {
            0 -> MetricsTab(
                userProfile = userProfile,
                weightEntries = weightEntries,
                smartFeedback = smartFeedbackMessage,
                onLogWeightClick = onLogWeightClick,
                onOpenWeeklyReport = onOpenWeeklyReport
            )
            1 -> ProgressPhotosTab(photos = photos)
            2 -> AchievementsTab(achievements = achievements)
        }
    }
}

@Composable
fun MetricsTab(
    userProfile: UserProfileEntity,
    weightEntries: List<WeightEntryEntity>,
    smartFeedback: String,
    onLogWeightClick: () -> Unit,
    onOpenWeeklyReport: () -> Unit
) {
    val heightM = userProfile.heightCm / 100f
    val bmi = userProfile.currentWeightKg / (heightM * heightM)
    val targetBmi = userProfile.targetWeightKg / (heightM * heightM)

    val lostSoFar = (userProfile.startingWeightKg - userProfile.currentWeightKg).coerceAtLeast(0f)
    val totalGoalLoss = (userProfile.startingWeightKg - userProfile.targetWeightKg).coerceAtLeast(0.1f)
    val progressPct = ((lostSoFar / totalGoalLoss) * 100).toInt().coerceIn(0, 100)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 12. Smart Progress Message banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MintSubtle)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "🧠", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Smart Coach Feedback",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = smartFeedback,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Charcoal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Goal summary card
        item {
            LeanCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎯 First Goal: ${userProfile.targetWeightKg} kg",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                    Text(
                        text = "$progressPct%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You're $progressPct% of the way to your first goal.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progressPct / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ForestGreen,
                    trackColor = Color(0xFFE9EBE2)
                )
            }
        }

        // 10. Key Metrics Grid: Weight, Waist, BMI
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Weight Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WarmSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Weight",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${userProfile.currentWeightKg} kg",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Goal: ${userProfile.targetWeightKg} kg",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftMint,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Waist Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WarmSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Waist",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${userProfile.currentWaistCm.toInt()} cm",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target: ${userProfile.targetWaistCm.toInt()} cm",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftMint,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // BMI Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WarmSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "BMI",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format("%.1f", bmi),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target: ${String.format("%.1f", targetBmi)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftMint,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Weight Trajectory Graph Simulation Card
        item {
            LeanCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Weight Trajectory",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Text(
                        text = "60.8 → 58.0 kg",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestGreen
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Trajectory Step Milestones
                val steps = listOf(
                    "60.8 kg" to "Start",
                    "60.2 kg" to "Week 2",
                    "59.7 kg" to "Week 4",
                    "59.1 kg" to "Week 6",
                    "58.0 kg" to "🎯 Goal"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.forEachIndexed { index, (w, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (index <= 1) ForestGreen else Color(0xFFE2E5DC)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (index <= 1) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CharcoalMuted
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = w,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Charcoal
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = CharcoalMuted
                            )
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(if (index == 0) ForestGreen else Color(0xFFE2E5DC))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogWeightClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Log New Weight & Waist Entry", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // 14. Weekly Report Card Preview
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF4F6EE),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenWeeklyReport() }
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sunday Weekly Report",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "🔥 86% Consistency",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = OrangeFlame
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Weight: 60.8 → 60.1 kg • Workouts: 3/3 ✅ • Steps: 7,240 avg",
                        style = MaterialTheme.typography.bodySmall,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "\"Excellent week. Keep the same rhythm next week.\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = CharcoalMuted
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun ProgressPhotosTab(photos: List<ProgressPhotoEntity>) {
    var selectedAngle by remember { mutableStateOf("Front") }
    val angles = listOf("Front", "Side", "Back")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MintSubtle)
                    .padding(14.dp)
            ) {
                Text(
                    text = "📸 Three simple angles: Front | Side | Back. No AI body modification—just honest, private tracking.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForestGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                angles.forEach { angle ->
                    val isSelected = angle == selectedAngle
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) ForestGreen else Color(0xFFF1F3EC),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedAngle = angle }
                    ) {
                        Text(
                            text = angle,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Charcoal,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Side-by-Side Timeline cards: Week 1, Week 4, Week 8
        val weeks = listOf(
            Triple("Week 1", "60.8 kg • 78 cm", "Baseline starting form"),
            Triple("Week 4", "59.5 kg • 76 cm", "Noticeable waist slimming"),
            Triple("Week 8", "58.0 kg • 73 cm", "Target athletic lean build")
        )

        items(weeks) { (week, metrics, note) ->
            LeanCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$week — $selectedAngle View",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    Text(
                        text = metrics,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestGreen
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEAECE3)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = CharcoalMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$selectedAngle Silhouette Snapshot ($week)",
                            style = MaterialTheme.typography.labelMedium,
                            color = CharcoalMuted
                        )
                        Text(
                            text = note,
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun AchievementsTab(achievements: List<AchievementEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Milestones & Consistency Badges",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Meaningful habit milestones, not childish points.",
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalMuted
            )
        }

        items(achievements) { achievement ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (achievement.isUnlocked) WarmSurface else Color(0xFFF6F7F2),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (achievement.isUnlocked) SoftMint else CardBorder,
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (achievement.isUnlocked) MintSubtle else Color(0xFFEAECE4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = achievement.iconEmoji, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = achievement.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (achievement.isUnlocked) Charcoal else CharcoalMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = achievement.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalMuted
                            )
                        }
                    }

                    if (achievement.isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MintSubtle
                        ) {
                            Text(
                                text = "Unlocked ✨",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = CharcoalMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
