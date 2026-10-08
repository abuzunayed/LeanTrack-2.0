package com.example.data.repository

import com.example.data.database.LeanTrackDatabase
import com.example.data.entity.AchievementEntity
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.data.entity.ProgressPhotoEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeanTrackRepository(private val database: LeanTrackDatabase) {

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getProfileFlow()
    val todayLog: Flow<DailyLogEntity?> = database.dailyLogDao().getLogForDateFlow(getTodayDateString())
    val weightEntries: Flow<List<WeightEntryEntity>> = database.weightEntryDao().getAllWeightEntriesFlow()
    val allFoods: Flow<List<FoodItemEntity>> = database.foodItemDao().getAllFoodsFlow()
    val todayMeals: Flow<List<MealEntity>> = database.mealDao().getMealsForDateFlow(getTodayDateString())
    val allPhotos: Flow<List<ProgressPhotoEntity>> = database.progressPhotoDao().getAllPhotosFlow()
    val achievements: Flow<List<AchievementEntity>> = database.achievementDao().getAllAchievementsFlow()

    suspend fun getProfileDirect(): UserProfileEntity? = database.userProfileDao().getProfile()

    suspend fun updateProfile(profile: UserProfileEntity) {
        database.userProfileDao().insertOrUpdateProfile(profile)
    }

    suspend fun updateDailyLog(log: DailyLogEntity) {
        database.dailyLogDao().insertOrUpdateDailyLog(log)
    }

    suspend fun getOrCreateTodayLog(): DailyLogEntity {
        val today = getTodayDateString()
        val existing = database.dailyLogDao().getLogForDate(today)
        if (existing != null) return existing

        val newLog = DailyLogEntity(
            date = today,
            caloriesConsumed = 0,
            proteinConsumedG = 0,
            waterConsumedL = 0f,
            stepsWalked = 0,
            habitNutrition = false,
            habitWorkout = false,
            habitSteps = false,
            habitWater = false
        )
        database.dailyLogDao().insertOrUpdateDailyLog(newLog)
        return newLog
    }

    suspend fun toggleHabit(habitName: String) {
        val current = getOrCreateTodayLog()
        val updated = when (habitName.lowercase()) {
            "nutrition" -> current.copy(habitNutrition = !current.habitNutrition)
            "workout" -> current.copy(habitWorkout = !current.habitWorkout)
            "steps" -> current.copy(habitSteps = !current.habitSteps)
            "water" -> current.copy(habitWater = !current.habitWater)
            else -> current
        }
        database.dailyLogDao().insertOrUpdateDailyLog(updated)
    }

    suspend fun addWater(amountL: Float) {
        val current = getOrCreateTodayLog()
        val newAmount = (current.waterConsumedL + amountL).coerceAtLeast(0f)
        val profile = database.userProfileDao().getProfile()
        val targetWater = profile?.targetWaterL ?: 2.5f
        val isTargetReached = newAmount >= targetWater
        val updated = current.copy(
            waterConsumedL = ((newAmount * 10).toInt() / 10f),
            habitWater = isTargetReached || current.habitWater
        )
        database.dailyLogDao().insertOrUpdateDailyLog(updated)
    }

    suspend fun addSteps(steps: Int) {
        val current = getOrCreateTodayLog()
        val newSteps = (current.stepsWalked + steps).coerceAtLeast(0)
        val profile = database.userProfileDao().getProfile()
        val targetSteps = profile?.targetSteps ?: 8000
        val updated = current.copy(
            stepsWalked = newSteps,
            habitSteps = newSteps >= targetSteps
        )
        database.dailyLogDao().insertOrUpdateDailyLog(updated)
    }

    suspend fun logWeight(weightKg: Float, waistCm: Float? = null) {
        val today = getTodayDateString()
        database.weightEntryDao().insertWeightEntry(
            WeightEntryEntity(
                date = today,
                weightKg = weightKg,
                waistCm = waistCm,
                timestamp = System.currentTimeMillis()
            )
        )
        val currentProfile = database.userProfileDao().getProfile()
        if (currentProfile != null) {
            database.userProfileDao().insertOrUpdateProfile(
                currentProfile.copy(
                    currentWeightKg = weightKg,
                    currentWaistCm = waistCm ?: currentProfile.currentWaistCm
                )
            )
        }
    }

    suspend fun toggleMealCompleted(mealId: Long, isCompleted: Boolean) {
        database.mealDao().setMealCompleted(mealId, isCompleted)
    }

    suspend fun replaceMeal(
        mealId: Long,
        newTitle: String,
        newDescription: String,
        newCalories: Int,
        newProtein: Float,
        replacedWith: String
    ) {
        database.mealDao().replaceMeal(
            id = mealId,
            newTitle = newTitle,
            newDescription = newDescription,
            newCalories = newCalories,
            newProtein = newProtein,
            replacedWith = replacedWith
        )
    }

    suspend fun addFoodItem(food: FoodItemEntity) {
        database.foodItemDao().insertFood(food)
    }

    suspend fun searchFoods(query: String): Flow<List<FoodItemEntity>> {
        return database.foodItemDao().searchFoodsFlow(query)
    }

    suspend fun saveProgressPhoto(photo: ProgressPhotoEntity) {
        database.progressPhotoDao().insertPhoto(photo)
    }

    suspend fun unlockAchievement(id: String) {
        val today = getTodayDateString()
        val achievements = database.achievementDao().getAllAchievementsFlow()
        // we can update via query
    }
}
