package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.LeanTrackDatabase
import com.example.data.entity.AchievementEntity
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.data.entity.ProgressPhotoEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.entity.WeightEntryEntity
import com.example.data.repository.LeanTrackRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    HOME, MEALS, WORKOUT, PROGRESS, MORE
}

data class ExerciseModel(
    val id: Int,
    val name: String,
    val targetMuscles: String,
    val repsTarget: String,
    val setsTotal: Int = 3,
    val durationSeconds: Int? = null,
    val restDurationSeconds: Int = 30,
    val instructions: String,
    val iconEmoji: String
)

class LeanTrackViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LeanTrackRepository

    init {
        val db = LeanTrackDatabase.getDatabase(application)
        repository = LeanTrackRepository(db)
    }

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayLog: StateFlow<DailyLogEntity?> = repository.todayLog
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val weightEntries: StateFlow<List<WeightEntryEntity>> = repository.weightEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFoods: StateFlow<List<FoodItemEntity>> = repository.allFoods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayMeals: StateFlow<List<MealEntity>> = repository.todayMeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressPhotos: StateFlow<List<ProgressPhotoEntity>> = repository.allPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.achievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Tab Navigation
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Onboarding State
    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    fun setShowOnboarding(show: Boolean) {
        _showOnboarding.value = show
    }

    // Workout Mode State
    private val _isWorkoutModeActive = MutableStateFlow(false)
    val isWorkoutModeActive: StateFlow<Boolean> = _isWorkoutModeActive.asStateFlow()

    val workoutExercises = listOf(
        ExerciseModel(
            id = 1,
            name = "Squats",
            targetMuscles = "Quads, Glutes & Core",
            repsTarget = "12 reps",
            setsTotal = 3,
            instructions = "Keep chest tall, push hips back like sitting into a chair, drive up through whole foot.",
            iconEmoji = "🏋️"
        ),
        ExerciseModel(
            id = 2,
            name = "Incline Push-ups",
            targetMuscles = "Chest, Shoulders & Triceps",
            repsTarget = "10 reps",
            setsTotal = 3,
            instructions = "Hands on elevated surface (bed or sturdy table), lower chest with elbows at 45°, push firmly.",
            iconEmoji = "💪"
        ),
        ExerciseModel(
            id = 3,
            name = "Glute Bridges",
            targetMuscles = "Glutes, Hamstrings & Lower back",
            repsTarget = "15 reps",
            setsTotal = 3,
            instructions = "Lie on back, feet flat on floor. Squeeze glutes and press hips upward into a straight line.",
            iconEmoji = "🧘"
        ),
        ExerciseModel(
            id = 4,
            name = "Backpack Rows",
            targetMuscles = "Upper Back, Lats & Posture",
            repsTarget = "12 reps",
            setsTotal = 3,
            instructions = "Fill a backpack with books for light resistance. Hinge forward at hips and pull elbows back smoothly.",
            iconEmoji = "🎒"
        ),
        ExerciseModel(
            id = 5,
            name = "Plank",
            targetMuscles = "Deep Core & Abdominal Wall",
            repsTarget = "20–30 sec",
            setsTotal = 3,
            durationSeconds = 30,
            instructions = "Hold forearms and toes, keep body in a rigid straight line, brace abs without sagging hips.",
            iconEmoji = "⏱️"
        )
    )

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _currentSetNumber = MutableStateFlow(1)
    val currentSetNumber: StateFlow<Int> = _currentSetNumber.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

    private val _restSecondsRemaining = MutableStateFlow(30)
    val restSecondsRemaining: StateFlow<Int> = _restSecondsRemaining.asStateFlow()

    private val _lastSetFeedbackMessage = MutableStateFlow<String?>(null)
    val lastSetFeedbackMessage: StateFlow<String?> = _lastSetFeedbackMessage.asStateFlow()

    private var restTimerJob: Job? = null

    fun startWorkoutMode() {
        _currentExerciseIndex.value = 0
        _currentSetNumber.value = 1
        _isResting.value = false
        _lastSetFeedbackMessage.value = null
        _isWorkoutModeActive.value = true
    }

    fun completeCurrentSet() {
        val currentEx = workoutExercises[_currentExerciseIndex.value]
        val set = _currentSetNumber.value

        _lastSetFeedbackMessage.value = "Great! Set $set of ${currentEx.name} complete. 💪"

        if (set < currentEx.setsTotal) {
            // Next set of current exercise
            _currentSetNumber.value = set + 1
            startRestTimer(currentEx.restDurationSeconds)
        } else {
            // Finished all sets of this exercise
            if (_currentExerciseIndex.value < workoutExercises.size - 1) {
                _currentExerciseIndex.value += 1
                _currentSetNumber.value = 1
                _lastSetFeedbackMessage.value = "Awesome! ${currentEx.name} finished. Next: ${workoutExercises[_currentExerciseIndex.value].name}!"
                startRestTimer(currentEx.restDurationSeconds + 15)
            } else {
                // All exercises finished!
                finishWorkoutSession()
            }
        }
    }

    private fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _isResting.value = true
        _restSecondsRemaining.value = seconds
        restTimerJob = viewModelScope.launch {
            while (_restSecondsRemaining.value > 0) {
                delay(1000)
                _restSecondsRemaining.value -= 1
            }
            _isResting.value = false
        }
    }

    fun skipRest() {
        restTimerJob?.cancel()
        _isResting.value = false
        _restSecondsRemaining.value = 0
    }

    fun finishWorkoutSession() {
        restTimerJob?.cancel()
        _isWorkoutModeActive.value = false
        _lastSetFeedbackMessage.value = "Workout Complete! You did amazing today. 🔥"
        // mark habit workout complete
        viewModelScope.launch {
            val log = repository.getOrCreateTodayLog()
            repository.updateDailyLog(log.copy(habitWorkout = true))
        }
    }

    fun exitWorkoutMode() {
        restTimerJob?.cancel()
        _isWorkoutModeActive.value = false
    }

    // UI Dialogs & Sheets
    private val _replacingMeal = MutableStateFlow<MealEntity?>(null)
    val replacingMeal: StateFlow<MealEntity?> = _replacingMeal.asStateFlow()

    fun setReplacingMeal(meal: MealEntity?) {
        _replacingMeal.value = meal
    }

    private val _showFoodSearchDialog = MutableStateFlow(false)
    val showFoodSearchDialog: StateFlow<Boolean> = _showFoodSearchDialog.asStateFlow()

    fun setShowFoodSearchDialog(show: Boolean) {
        _showFoodSearchDialog.value = show
    }

    private val _showWeightLogDialog = MutableStateFlow(false)
    val showWeightLogDialog: StateFlow<Boolean> = _showWeightLogDialog.asStateFlow()

    fun setShowWeightLogDialog(show: Boolean) {
        _showWeightLogDialog.value = show
    }

    private val _showWeeklyReport = MutableStateFlow(false)
    val showWeeklyReport: StateFlow<Boolean> = _showWeeklyReport.asStateFlow()

    fun setShowWeeklyReport(show: Boolean) {
        _showWeeklyReport.value = show
    }

    // Habit & Metric Actions
    fun toggleHabit(habitName: String) {
        viewModelScope.launch {
            repository.toggleHabit(habitName)
        }
    }

    fun addWater(amountL: Float) {
        viewModelScope.launch {
            repository.addWater(amountL)
        }
    }

    fun addSteps(steps: Int) {
        viewModelScope.launch {
            repository.addSteps(steps)
        }
    }

    fun logWeightAndWaist(weightKg: Float, waistCm: Float?) {
        viewModelScope.launch {
            repository.logWeight(weightKg, waistCm)
        }
    }

    fun toggleMealCompleted(meal: MealEntity) {
        viewModelScope.launch {
            repository.toggleMealCompleted(meal.id, !meal.isCompleted)
        }
    }

    fun replaceMealWithChoice(
        meal: MealEntity,
        newTitle: String,
        newDescription: String,
        newCalories: Int,
        newProtein: Float,
        replacedWith: String
    ) {
        viewModelScope.launch {
            repository.replaceMeal(
                mealId = meal.id,
                newTitle = newTitle,
                newDescription = newDescription,
                newCalories = newCalories,
                newProtein = newProtein,
                replacedWith = replacedWith
            )
            _replacingMeal.value = null
        }
    }

    fun updateProfile(
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
    ) {
        viewModelScope.launch {
            val (calculatedCalories, calculatedProtein) = calculateTargets(
                weightKg = currentWeightKg,
                heightCm = heightCm,
                age = age,
                sex = sex,
                activity = activityLevel,
                goal = goal
            )

            val current = userProfile.value ?: UserProfileEntity()
            val updated = current.copy(
                name = name,
                goal = goal,
                heightCm = heightCm,
                currentWeightKg = currentWeightKg,
                targetWeightKg = targetWeightKg,
                currentWaistCm = currentWaistCm,
                activityLevel = activityLevel,
                exerciseLocation = exerciseLocation,
                age = age,
                sex = sex,
                targetCalories = calculatedCalories,
                targetProteinG = calculatedProtein,
                isOnboarded = true
            )
            repository.updateProfile(updated)
        }
    }

    /**
     * Harris-Benedict / Mifflin-St Jeor Formula
     * Adapts intake to age, sex, activity, and goals rather than hardcoding.
     */
    fun calculateTargets(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        sex: String,
        activity: String,
        goal: String
    ): Pair<Int, Int> {
        val bmr = if (sex.equals("Female", ignoreCase = true)) {
            (10 * weightKg) + (6.25f * heightCm) - (5 * age) - 161
        } else {
            (10 * weightKg) + (6.25f * heightCm) - (5 * age) + 5
        }

        val activityMultiplier = when (activity) {
            "Mostly sitting" -> 1.2f
            "Lightly active" -> 1.375f
            "Moderately active" -> 1.55f
            "Very active" -> 1.725f
            else -> 1.375f
        }

        val maintenance = bmr * activityMultiplier

        // Moderate sustainable deficit for fat loss without crashing metabolism
        val targetCalories = when {
            goal.contains("fat", ignoreCase = true) || goal.contains("weight", ignoreCase = true) -> {
                (maintenance - 350f).toInt().coerceIn(1350, 3200)
            }
            goal.contains("muscle", ignoreCase = true) -> {
                (maintenance + 200f).toInt().coerceIn(1600, 3500)
            }
            else -> maintenance.toInt()
        }

        // Protein target: 1.6g/kg for lean body & muscle preservation
        val targetProtein = (weightKg * 1.6f).toInt().coerceIn(60, 180)

        return Pair(targetCalories, targetProtein)
    }

    // Dynamic smart progress feedback based on current data
    fun getSmartProgressFeedback(currentWeight: Float, startWeight: Float, targetWeight: Float): String {
        val diffFromStart = startWeight - currentWeight
        return when {
            diffFromStart >= 1.0f -> {
                "You're down ${String.format("%.1f", diffFromStart)} kg so far.\nYour consistency is paying off. 🔥"
            }
            diffFromStart in -0.3f..0.3f -> {
                "Weight is stable this week.\nDon't panic. Check your waist measurement and keep going."
            }
            diffFromStart > 3.0f -> {
                "Your weight is dropping quickly.\nConsider whether you're eating enough and recovering properly."
            }
            else -> {
                "Small steps every day. Focus on completing today's habits and fueling well."
            }
        }
    }
}
