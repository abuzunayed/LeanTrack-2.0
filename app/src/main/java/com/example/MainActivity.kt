package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.UserProfileEntity
import com.example.ui.components.FoodReplacementDialog
import com.example.ui.components.LogWeightDialog
import com.example.ui.components.WeeklyReportDialog
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.meals.MealsScreen
import com.example.ui.screens.more.MoreScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.workout.ActiveWorkoutModeScreen
import com.example.ui.screens.workout.WorkoutScreen
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SoftMint
import com.example.ui.theme.WarmBackground
import com.example.ui.theme.WarmSurface
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.LeanTrackViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LeanTrackApp()
            }
        }
    }
}

@Composable
fun LeanTrackApp(viewModel: LeanTrackViewModel = viewModel()) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val todayLog by viewModel.todayLog.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()
    val isWorkoutModeActive by viewModel.isWorkoutModeActive.collectAsStateWithLifecycle()
    val todayMeals by viewModel.todayMeals.collectAsStateWithLifecycle()
    val allFoods by viewModel.allFoods.collectAsStateWithLifecycle()
    val weightEntries by viewModel.weightEntries.collectAsStateWithLifecycle()
    val progressPhotos by viewModel.progressPhotos.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()

    // Workout Mode state
    val currentExerciseIndex by viewModel.currentExerciseIndex.collectAsStateWithLifecycle()
    val currentSet by viewModel.currentSetNumber.collectAsStateWithLifecycle()
    val isResting by viewModel.isResting.collectAsStateWithLifecycle()
    val restSeconds by viewModel.restSecondsRemaining.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.lastSetFeedbackMessage.collectAsStateWithLifecycle()

    // Dialog states
    val replacingMeal by viewModel.replacingMeal.collectAsStateWithLifecycle()
    val showWeightDialog by viewModel.showWeightLogDialog.collectAsStateWithLifecycle()
    val showWeeklyReport by viewModel.showWeeklyReport.collectAsStateWithLifecycle()

    // Splash animation display
    var isSplashVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200) // Brief elegant splash
        isSplashVisible = false
    }

    if (isSplashVisible) {
        SplashScreen()
        return
    }

    // Fallback default profile if not loaded yet
    val profile = userProfile ?: UserProfileEntity(
        id = 1,
        name = "Ratul",
        goal = "Lose fat & become lean",
        heightCm = 156f,
        currentWeightKg = 60.8f,
        targetWeightKg = 58.0f,
        currentWaistCm = 78f,
        targetWaistCm = 72f,
        targetCalories = 1800,
        targetProteinG = 95,
        targetWaterL = 2.5f,
        targetSteps = 8000,
        isOnboarded = true
    )

    val log = todayLog ?: DailyLogEntity(
        date = "Today",
        caloriesConsumed = 1240,
        proteinConsumedG = 68,
        waterConsumedL = 1.5f,
        stepsWalked = 4892,
        habitNutrition = true,
        habitWorkout = true,
        habitSteps = false,
        habitWater = true
    )

    // Handle Active Workout Mode
    if (isWorkoutModeActive) {
        BackHandler {
            viewModel.exitWorkoutMode()
        }
        ActiveWorkoutModeScreen(
            exercises = viewModel.workoutExercises,
            currentExerciseIndex = currentExerciseIndex,
            currentSet = currentSet,
            isResting = isResting,
            restSeconds = restSeconds,
            feedbackMessage = feedbackMessage,
            onCompleteSet = { viewModel.completeCurrentSet() },
            onSkipRest = { viewModel.skipRest() },
            onExitWorkout = { viewModel.exitWorkoutMode() }
        )
        return
    }

    // Handle Onboarding Flow
    if (showOnboarding || !profile.isOnboarded) {
        BackHandler(enabled = showOnboarding) {
            viewModel.setShowOnboarding(false)
        }
        OnboardingScreen(
            initialName = profile.name,
            onComplete = { name, goal, h, w, targetW, act, loc ->
                viewModel.updateProfile(
                    name = name,
                    goal = goal,
                    heightCm = h,
                    currentWeightKg = w,
                    targetWeightKg = targetW,
                    currentWaistCm = profile.currentWaistCm,
                    activityLevel = act,
                    exerciseLocation = loc,
                    age = profile.age,
                    sex = profile.sex
                )
                viewModel.setShowOnboarding(false)
            }
        )
        return
    }

    // Handle Back Press in secondary tabs
    if (currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.selectTab(AppTab.HOME)
        }
    }

    // Main App with Bottom Navigation
    Scaffold(
        containerColor = WarmBackground,
        bottomBar = {
            LeanBottomNavigation(
                currentTab = currentTab,
                onSelectTab = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> HomeScreen(
                    userProfile = profile,
                    todayLog = log,
                    onToggleHabit = { viewModel.toggleHabit(it) },
                    onAddWater = { viewModel.addWater(it) },
                    onAddSteps = { viewModel.addSteps(it) },
                    onStartWorkout = { viewModel.startWorkoutMode() },
                    onLogWeightClick = { viewModel.setShowWeightLogDialog(true) }
                )
                AppTab.MEALS -> MealsScreen(
                    meals = todayMeals,
                    foodDatabase = allFoods,
                    onToggleComplete = { viewModel.toggleMealCompleted(it) },
                    onReplaceMealClick = { viewModel.setReplacingMeal(it) }
                )
                AppTab.WORKOUT -> WorkoutScreen(
                    exercises = viewModel.workoutExercises,
                    onStartWorkout = { viewModel.startWorkoutMode() }
                )
                AppTab.PROGRESS -> ProgressScreen(
                    userProfile = profile,
                    weightEntries = weightEntries,
                    photos = progressPhotos,
                    achievements = achievements,
                    smartFeedbackMessage = viewModel.getSmartProgressFeedback(
                        currentWeight = profile.currentWeightKg,
                        startWeight = profile.startingWeightKg,
                        targetWeight = profile.targetWeightKg
                    ),
                    onLogWeightClick = { viewModel.setShowWeightLogDialog(true) },
                    onOpenWeeklyReport = { viewModel.setShowWeeklyReport(true) }
                )
                AppTab.MORE -> MoreScreen(
                    userProfile = profile,
                    onUpdateProfile = { name, goal, h, w, targetW, waist, act, loc, age, sex ->
                        viewModel.updateProfile(name, goal, h, w, targetW, waist, act, loc, age, sex)
                    },
                    onRestartOnboarding = { viewModel.setShowOnboarding(true) }
                )
            }
        }
    }

    // Dialogs
    replacingMeal?.let { mealToReplace ->
        FoodReplacementDialog(
            meal = mealToReplace,
            onDismiss = { viewModel.setReplacingMeal(null) },
            onSelectReplacement = { title, desc, cal, prot, note ->
                viewModel.replaceMealWithChoice(
                    meal = mealToReplace,
                    newTitle = title,
                    newDescription = desc,
                    newCalories = cal,
                    newProtein = prot,
                    replacedWith = note
                )
            }
        )
    }

    if (showWeightDialog) {
        LogWeightDialog(
            currentWeight = profile.currentWeightKg,
            currentWaist = profile.currentWaistCm,
            onDismiss = { viewModel.setShowWeightLogDialog(false) },
            onConfirm = { weight, waist ->
                viewModel.logWeightAndWaist(weight, waist)
                viewModel.setShowWeightLogDialog(false)
            }
        )
    }

    if (showWeeklyReport) {
        WeeklyReportDialog(
            onDismiss = { viewModel.setShowWeeklyReport(false) }
        )
    }
}

@Composable
fun LeanBottomNavigation(
    currentTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = WarmSurface,
        tonalElevation = 2.dp,
        modifier = Modifier.testTag("bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.HOME,
            onClick = { onSelectTab(AppTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestGreen,
                selectedTextColor = ForestGreen,
                indicatorColor = MintSubtle
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.MEALS,
            onClick = { onSelectTab(AppTab.MEALS) },
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Meals") },
            label = { Text("Meals", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestGreen,
                selectedTextColor = ForestGreen,
                indicatorColor = MintSubtle
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.WORKOUT,
            onClick = { onSelectTab(AppTab.WORKOUT) },
            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workout") },
            label = { Text("Workout", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestGreen,
                selectedTextColor = ForestGreen,
                indicatorColor = MintSubtle
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.PROGRESS,
            onClick = { onSelectTab(AppTab.PROGRESS) },
            icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Progress") },
            label = { Text("Progress", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestGreen,
                selectedTextColor = ForestGreen,
                indicatorColor = MintSubtle
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.MORE,
            onClick = { onSelectTab(AppTab.MORE) },
            icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "More") },
            label = { Text("More", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestGreen,
                selectedTextColor = ForestGreen,
                indicatorColor = MintSubtle
            )
        )
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForestGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌱", fontSize = 42.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "LEANTRACK",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Small steps. Visible change.",
                style = MaterialTheme.typography.bodyLarge,
                color = MintSubtle,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            CircularProgressIndicator(
                color = MintLight,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
