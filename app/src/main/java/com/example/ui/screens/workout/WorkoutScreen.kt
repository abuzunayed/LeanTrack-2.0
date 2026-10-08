package com.example.ui.screens.workout

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.LeanCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.SoftMint
import com.example.ui.theme.WarmBackground
import com.example.ui.theme.WarmSurface
import com.example.ui.viewmodel.ExerciseModel

@Composable
fun WorkoutScreen(
    exercises: List<ExerciseModel>,
    onStartWorkout: () -> Unit
) {
    var selectedDemoExercise by remember { mutableStateOf<ExerciseModel?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 20.dp)
            .testTag("workout_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Today's Workout",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Build lean muscle, boost metabolic rate & burn belly fat.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalMuted
            )
        }

        // Hero Banner Card
        item {
            LeanCard(
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
                            text = "FULL BODY — BEGINNER",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MintLight
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Home Routine",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "⏱️ 30 minutes • 5 exercises • 0 equipment",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD3E0D5)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔥", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onStartWorkout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = ForestGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("start_workout_session_button")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Start Workout",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "Exercise Sequence",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        itemsIndexed(exercises) { index, exercise ->
            ExerciseSequenceCard(
                number = index + 1,
                exercise = exercise,
                onDemoClick = { selectedDemoExercise = exercise },
                onStartExercise = onStartWorkout
            )
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    selectedDemoExercise?.let { ex ->
        ExerciseDemonstrationDialog(
            exercise = ex,
            onDismiss = { selectedDemoExercise = null },
            onStart = {
                selectedDemoExercise = null
                onStartWorkout()
            }
        )
    }
}

@Composable
fun ExerciseSequenceCard(
    number: Int,
    exercise: ExerciseModel,
    onDemoClick: () -> Unit,
    onStartExercise: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WarmSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MintSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$number",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Text(
                            text = "${exercise.repsTarget} × ${exercise.setsTotal} sets",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestGreen
                        )
                    }
                }

                Text(
                    text = exercise.iconEmoji,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Targets: ${exercise.targetMuscles}",
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDemoClick,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "▶ Form Tips",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestGreen
                    )
                }

                Button(
                    onClick = onStartExercise,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Start",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseDemonstrationDialog(
    exercise: ExerciseModel,
    onDismiss: () -> Unit,
    onStart: () -> Unit
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = exercise.iconEmoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3F5EE))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "How to perform correctly:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = exercise.instructions,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Charcoal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• Target: ${exercise.targetMuscles}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )
                Text(
                    text = "• Prescription: ${exercise.repsTarget} for ${exercise.setsTotal} sets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )
                Text(
                    text = "• Rest: ${exercise.restDurationSeconds} seconds between sets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start This Routine", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * 8. Workout Mode (Active Session)
 * Simple, distraction-free interface
 */
@Composable
fun ActiveWorkoutModeScreen(
    exercises: List<ExerciseModel>,
    currentExerciseIndex: Int,
    currentSet: Int,
    isResting: Boolean,
    restSeconds: Int,
    feedbackMessage: String?,
    onCompleteSet: () -> Unit,
    onSkipRest: () -> Unit,
    onExitWorkout: () -> Unit
) {
    val currentExercise = exercises.getOrNull(currentExerciseIndex) ?: exercises.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("active_workout_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE WORKOUT MODE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Text(
                    text = "Exercise ${currentExerciseIndex + 1} of ${exercises.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = CharcoalMuted
                )
            }

            IconButton(onClick = onExitWorkout) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit",
                    tint = CharcoalMuted
                )
            }
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Exercise Icon & Name
            Text(
                text = currentExercise.iconEmoji,
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentExercise.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Charcoal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = currentExercise.repsTarget,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Divider line
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(ForestGreen)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Set counter: Set 1 / 3
            Text(
                text = "Set $currentSet / ${currentExercise.setsTotal}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isResting) {
                // Rest Timer Mode
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MintSubtle,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = ForestGreen
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Rest Period",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val formattedSec = if (restSeconds < 10) "0$restSeconds" else "$restSeconds"
                        Text(
                            text = "00:$formattedSec",
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onSkipRest,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Skip Rest", color = ForestGreen, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            } else {
                // Exercise instructions reminder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmSurface)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "💡 ${currentExercise.instructions}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Encouragement message: "Great! Set 1 complete. 💪"
            feedbackMessage?.let { msg ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftMint.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Bottom Action Button: [ Complete Set ]
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onCompleteSet,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("complete_set_button")
            ) {
                Text(
                    text = if (isResting) "Ready for Next Set" else "Complete Set",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
