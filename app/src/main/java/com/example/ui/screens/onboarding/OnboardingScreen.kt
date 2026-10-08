package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun OnboardingScreen(
    initialName: String = "Ratul",
    onComplete: (
        name: String,
        goal: String,
        heightCm: Float,
        weightKg: Float,
        targetKg: Float,
        activity: String,
        exerciseLocation: String
    ) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    var name by remember { mutableStateOf(initialName) }
    var goal by remember { mutableStateOf("Lose fat & become lean") }
    var heightCm by remember { mutableStateOf("156") }
    var weightKg by remember { mutableStateOf("60.8") }
    var targetKg by remember { mutableStateOf("58.0") }
    var activity by remember { mutableStateOf("Lightly active") }
    var exerciseLocation by remember { mutableStateOf("Home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("onboarding_screen")
    ) {
        // Top Navigation Bar for Onboarding
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (step > 1) {
                IconButton(onClick = { step-- }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Charcoal
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            Text(
                text = "Step $step of 5",
                style = MaterialTheme.typography.labelMedium,
                color = CharcoalMuted
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        LinearProgressIndicator(
            progress = { step / 5f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = ForestGreen,
            trackColor = Color(0xFFE5E8DE)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (step) {
                1 -> OnboardingGoalStep(
                    selectedGoal = goal,
                    onSelectGoal = { goal = it }
                )
                2 -> OnboardingMeasurementsStep(
                    height = heightCm,
                    onHeightChange = { heightCm = it },
                    weight = weightKg,
                    onWeightChange = { weightKg = it },
                    target = targetKg,
                    onTargetChange = { targetKg = it }
                )
                3 -> OnboardingActivityStep(
                    selectedActivity = activity,
                    onSelectActivity = { activity = it }
                )
                4 -> OnboardingLocationStep(
                    selectedLocation = exerciseLocation,
                    onSelectLocation = { exerciseLocation = it }
                )
                5 -> OnboardingReadyStep(
                    name = name,
                    goal = goal,
                    targetKg = targetKg
                )
            }
        }

        // Bottom CTA Button
        Button(
            onClick = {
                if (step < 5) {
                    step++
                } else {
                    onComplete(
                        name,
                        goal,
                        heightCm.toFloatOrNull() ?: 156f,
                        weightKg.toFloatOrNull() ?: 60.8f,
                        targetKg.toFloatOrNull() ?: 58.0f,
                        activity,
                        exerciseLocation
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_next_button")
        ) {
            Text(
                text = if (step == 5) "Start My Journey" else "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun OnboardingGoalStep(
    selectedGoal: String,
    onSelectGoal: (String) -> Unit
) {
    val goals = listOf(
        "Lose weight",
        "Lose fat & become lean",
        "Build muscle",
        "Improve fitness"
    )

    Column {
        Text(
            text = "What's your primary goal?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Charcoal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "LeanTrack focuses on fat loss, waist reduction, and preserving muscle through sustainable habits.",
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalMuted
        )

        Spacer(modifier = Modifier.height(28.dp))

        goals.forEach { item ->
            val isSelected = item == selectedGoal
            SelectableOptionCard(
                text = item,
                isSelected = isSelected,
                onClick = { onSelectGoal(item) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun OnboardingMeasurementsStep(
    height: String,
    onHeightChange: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    target: String,
    onTargetChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Your measurements",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Charcoal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We use these to calculate your realistic calorie budget and healthy rate of progress.",
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalMuted
        )

        Spacer(modifier = Modifier.height(24.dp))

        MeasurementInputField(
            label = "Height",
            value = height,
            unit = "cm",
            onValueChange = onHeightChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        MeasurementInputField(
            label = "Current Weight",
            value = weight,
            unit = "kg",
            onValueChange = onWeightChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        MeasurementInputField(
            label = "Target Weight",
            value = target,
            unit = "kg",
            onValueChange = onTargetChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        val currentW = weight.toFloatOrNull() ?: 60.8f
        val targetW = target.toFloatOrNull() ?: 58.0f
        val diff = (currentW - targetW).coerceAtLeast(0f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MintSubtle)
                .padding(14.dp)
        ) {
            Text(
                text = "🎯 First milestone: ${String.format("%.1f", diff)} kg to your initial goal. A safe, achievable deficit.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = ForestGreen
            )
        }
    }
}

@Composable
fun MeasurementInputField(
    label: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WarmSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = CharcoalMuted)
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = CharcoalMuted
            )
        }
    }
}

@Composable
fun OnboardingActivityStep(
    selectedActivity: String,
    onSelectActivity: (String) -> Unit
) {
    val activities = listOf(
        "Mostly sitting" to "Desk job, little regular exercise",
        "Lightly active" to "1–3 light walks or workout sessions/week",
        "Moderately active" to "3–5 regular workout sessions/week",
        "Very active" to "High physical movement throughout day"
    )

    Column {
        Text(
            text = "How active are you?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Charcoal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Helps determine your true total daily energy expenditure (TDEE).",
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalMuted
        )

        Spacer(modifier = Modifier.height(24.dp))

        activities.forEach { (title, subtitle) ->
            val isSelected = title == selectedActivity
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) MintSubtle else WarmSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (isSelected) ForestGreen else CardBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectActivity(title) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectActivity(title) },
                        colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun OnboardingLocationStep(
    selectedLocation: String,
    onSelectLocation: (String) -> Unit
) {
    val locations = listOf(
        "Home" to "Bodyweight & household resistance (No gym needed)",
        "Gym" to "Full access to machines & barbells",
        "Both" to "Mix of home sessions and gym days"
    )

    Column {
        Text(
            text = "Where will you exercise?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Charcoal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "LeanTrack tailors simple 20–30 min routines that fit into your everyday routine.",
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalMuted
        )

        Spacer(modifier = Modifier.height(24.dp))

        locations.forEach { (title, subtitle) ->
            val isSelected = title == selectedLocation
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) MintSubtle else WarmSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (isSelected) ForestGreen else CardBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectLocation(title) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectLocation(title) },
                        colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun OnboardingReadyStep(
    name: String,
    goal: String,
    targetKg: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MintSubtle),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🔥", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "You're ready.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Charcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "We'll build your plan around consistency, not extreme dieting.",
            style = MaterialTheme.typography.bodyLarge,
            color = CharcoalMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WarmSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Your Blueprint Summary",
                    style = MaterialTheme.typography.labelMedium,
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• First Goal: $targetKg kg with focus on waist reduction",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal
                )
                Text(
                    text = "• Moderate calorie target adapted to your metabolic rate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal
                )
                Text(
                    text = "• Everyday Bengali & local foods with flexible swaps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal
                )
                Text(
                    text = "• 5 quick home exercises, 0 equipment needed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal
                )
            }
        }
    }
}

@Composable
fun SelectableOptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MintSubtle else WarmSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                if (isSelected) ForestGreen else CardBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Charcoal
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
            )
        }
    }
}
