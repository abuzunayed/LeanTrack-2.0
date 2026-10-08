package com.example.ui.screens.more

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.UserProfileEntity
import com.example.ui.components.LeanCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.SoftMint
import com.example.ui.theme.WarmBackground
import com.example.ui.theme.WarmSurface

@Composable
fun MoreScreen(
    userProfile: UserProfileEntity,
    onUpdateProfile: (
        name: String,
        goal: String,
        heightCm: Float,
        currentWeightKg: Float,
        targetWeightKg: Float,
        currentWaistCm: Float,
        activityLevel: String,
        exerciseLocation: String,
        age: Int,
        sex: String
    ) -> Unit,
    onRestartOnboarding: () -> Unit
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showCalculatorDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    // Notification reminder toggles
    var breakfastReminder by remember { mutableStateOf(true) }
    var waterReminder by remember { mutableStateOf(true) }
    var workoutReminder by remember { mutableStateOf(true) }
    var nightLogReminder by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 20.dp)
            .testTag("more_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Settings & Preferences",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Manage your profile, target calculations & reminders",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalMuted
            )
        }

        // Profile summary card
        item {
            LeanCard(
                backgroundColor = MintSubtle.copy(alpha = 0.5f),
                borderColor = SoftMint.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(ForestGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = userProfile.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Charcoal
                            )
                            Text(
                                text = userProfile.goal,
                                style = MaterialTheme.typography.bodySmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${userProfile.heightCm.toInt()} cm • ${userProfile.currentWeightKg} kg • Target: ${userProfile.targetWeightKg} kg",
                                style = MaterialTheme.typography.labelSmall,
                                color = CharcoalMuted
                            )
                        }
                    }

                    TextButton(onClick = { showEditProfileDialog = true }) {
                        Text("Edit", color = ForestGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Logic & Targets
        item {
            Text(
                text = "Behind-The-Scenes Logic",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Charcoal,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            LeanCard {
                SettingsOptionRow(
                    icon = Icons.Default.Calculate,
                    title = "Calorie & Macro Logic",
                    subtitle = "Mifflin-St Jeor formula + ${userProfile.targetCalories} kcal target",
                    onClick = { showCalculatorDialog = true }
                )
                Spacer(modifier = Modifier.height(10.dp))
                SettingsOptionRow(
                    icon = Icons.Default.Straighten,
                    title = "Units of Measurement",
                    subtitle = "Metric (${userProfile.unitWeight}, ${userProfile.unitHeight})",
                    onClick = {}
                )
            }
        }

        // Section: Reminders
        item {
            Text(
                text = "Daily Reminders (Optional & Gentle)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
        }

        item {
            LeanCard {
                ReminderToggleRow(
                    emoji = "☀️",
                    title = "Morning Breakfast",
                    time = "08:30 AM",
                    checked = breakfastReminder,
                    onCheckedChange = { breakfastReminder = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ReminderToggleRow(
                    emoji = "💧",
                    title = "Afternoon Hydration",
                    time = "02:00 PM",
                    checked = waterReminder,
                    onCheckedChange = { waterReminder = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ReminderToggleRow(
                    emoji = "🏋️",
                    title = "Evening Workout",
                    time = "06:30 PM",
                    checked = workoutReminder,
                    onCheckedChange = { workoutReminder = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ReminderToggleRow(
                    emoji = "📊",
                    title = "Night Habit Log",
                    time = "09:30 PM",
                    checked = nightLogReminder,
                    onCheckedChange = { nightLogReminder = it }
                )
            }
        }

        // Section: App info & Reset
        item {
            Text(
                text = "Preferences & App Info",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
        }

        item {
            LeanCard {
                SettingsOptionRow(
                    icon = Icons.Default.Refresh,
                    title = "Relaunch Onboarding",
                    subtitle = "Reset goals and walkthrough questionnaire",
                    onClick = onRestartOnboarding
                )
                Spacer(modifier = Modifier.height(10.dp))
                SettingsOptionRow(
                    icon = Icons.Default.Info,
                    title = "About LeanTrack",
                    subtitle = "V1.0 • Small steps. Visible change.",
                    onClick = { showAboutDialog = true }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            userProfile = userProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, goal, h, w, targetW, waist, act, loc, age, sex ->
                onUpdateProfile(name, goal, h, w, targetW, waist, act, loc, age, sex)
                showEditProfileDialog = false
            }
        )
    }

    if (showCalculatorDialog) {
        BehindTheScenesDialog(
            userProfile = userProfile,
            onDismiss = { showCalculatorDialog = false }
        )
    }

    if (showAboutDialog) {
        AboutLeanTrackDialog(onDismiss = { showAboutDialog = false })
    }
}

@Composable
fun SettingsOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF2F4ED)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
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
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = CharcoalMuted
        )
    }
}

@Composable
fun ReminderToggleRow(
    emoji: String,
    title: String,
    time: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Charcoal
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreen
            )
        )
    }
}

@Composable
fun EditProfileDialog(
    userProfile: UserProfileEntity,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        goal: String,
        h: Float,
        w: Float,
        targetW: Float,
        waist: Float,
        act: String,
        loc: String,
        age: Int,
        sex: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var height by remember { mutableStateOf(userProfile.heightCm.toString()) }
    var weight by remember { mutableStateOf(userProfile.currentWeightKg.toString()) }
    var targetWeight by remember { mutableStateOf(userProfile.targetWeightKg.toString()) }
    var waist by remember { mutableStateOf(userProfile.currentWaistCm.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Edit Profile & Targets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Current Weight (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = targetWeight,
                    onValueChange = { targetWeight = it },
                    label = { Text("Target Goal (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = waist,
                    onValueChange = { waist = it },
                    label = { Text("Waist circumference (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

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
                            onSave(
                                name,
                                userProfile.goal,
                                height.toFloatOrNull() ?: userProfile.heightCm,
                                weight.toFloatOrNull() ?: userProfile.currentWeightKg,
                                targetWeight.toFloatOrNull() ?: userProfile.targetWeightKg,
                                waist.toFloatOrNull() ?: userProfile.currentWaistCm,
                                userProfile.activityLevel,
                                userProfile.exerciseLocation,
                                userProfile.age,
                                userProfile.sex
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }
    }
}

@Composable
fun BehindTheScenesDialog(
    userProfile: UserProfileEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "🧮 Behind-The-Scenes Logic",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "LeanTrack calculates rather than blindly prescribing:",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF3F5EE))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Inputs:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "Age (${userProfile.age}) + Sex (${userProfile.sex}) + Height (${userProfile.heightCm}cm) + Weight (${userProfile.currentWeightKg}kg) + Activity (${userProfile.activityLevel})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Charcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "↓ Estimated Maintenance:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "~2,150 kcal/day (Mifflin-St Jeor formula)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Charcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "↓ Moderate Deficit:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "350 kcal deficit (protects metabolism & prevents binge rebound)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Charcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "↓ Daily Target:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "${userProfile.targetCalories} kcal • ${userProfile.targetProteinG}g protein (1.6g/kg)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Safety Feature: If weight is stable for several weeks, LeanTrack prompts reviewing intake and activity rather than recklessly slashing 500 more calories.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got It")
                }
            }
        }
    }
}

@Composable
fun AboutLeanTrackDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(ForestGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌱", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "LEANTRACK",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )

                Text(
                    text = "Small steps. Visible change.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A simple personal fitness companion built around:\n• Lose fat\n• Reduce waist\n• Maintain muscle\n• Build sustainable habits",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}
