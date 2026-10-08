package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AchievementDao
import com.example.data.dao.DailyLogDao
import com.example.data.dao.FoodItemDao
import com.example.data.dao.MealDao
import com.example.data.dao.ProgressPhotoDao
import com.example.data.dao.UserProfileDao
import com.example.data.dao.WeightEntryDao
import com.example.data.entity.AchievementEntity
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.data.entity.ProgressPhotoEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.entity.WeightEntryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        UserProfileEntity::class,
        DailyLogEntity::class,
        WeightEntryEntity::class,
        FoodItemEntity::class,
        MealEntity::class,
        ProgressPhotoEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LeanTrackDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun mealDao(): MealDao
    abstract fun progressPhotoDao(): ProgressPhotoDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: LeanTrackDatabase? = null

        fun getDatabase(context: Context): LeanTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeanTrackDatabase::class.java,
                    "leantrack_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedDatabase(database: LeanTrackDatabase) {
            val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // 1. Initial User Profile (Ratul)
            database.userProfileDao().insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Ratul",
                    goal = "Lose fat & become lean",
                    heightCm = 156f,
                    currentWeightKg = 60.8f,
                    startingWeightKg = 62.0f,
                    targetWeightKg = 58.0f,
                    currentWaistCm = 78f,
                    targetWaistCm = 72f,
                    age = 26,
                    sex = "Male",
                    activityLevel = "Lightly active",
                    exerciseLocation = "Home",
                    targetCalories = 1800,
                    targetProteinG = 95,
                    targetWaterL = 2.5f,
                    targetSteps = 8000,
                    isOnboarded = true
                )
            )

            // 2. Today's Daily Log
            database.dailyLogDao().insertOrUpdateDailyLog(
                DailyLogEntity(
                    date = todayDate,
                    caloriesConsumed = 1240,
                    proteinConsumedG = 68,
                    waterConsumedL = 1.5f,
                    stepsWalked = 4892,
                    habitNutrition = true,
                    habitWorkout = true,
                    habitSteps = false,
                    habitWater = true,
                    notes = "Feeling energetic after the morning session."
                )
            )

            // 3. Weight history progression entries
            val initialEntries = listOf(
                WeightEntryEntity(date = "Week 1", weightKg = 62.0f, waistCm = 80f, timestamp = 1700000000000L),
                WeightEntryEntity(date = "Week 2", weightKg = 61.4f, waistCm = 79f, timestamp = 1700600000000L),
                WeightEntryEntity(date = "Week 3", weightKg = 60.8f, waistCm = 78f, timestamp = System.currentTimeMillis())
            )
            initialEntries.forEach { database.weightEntryDao().insertWeightEntry(it) }

            // 4. Local Bangladeshi & Common foods
            val foods = listOf(
                FoodItemEntity(name = "White Rice (Cooked)", banglaName = "সাদা ভাত", category = "Rice & Carbs", portionDescription = "1 cup cooked (~160g)", calories = 205, proteinG = 4.3f, carbsG = 45f, fatG = 0.4f, iconEmoji = "🍚"),
                FoodItemEntity(name = "Brown Rice (Cooked)", banglaName = "লাল চালের ভাত", category = "Rice & Carbs", portionDescription = "1 cup cooked (~160g)", calories = 216, proteinG = 5.0f, carbsG = 45f, fatG = 1.6f, iconEmoji = "🍚"),
                FoodItemEntity(name = "Roti (Whole Wheat)", banglaName = "হাতে তৈরি লাল আটার রুটি", category = "Rice & Carbs", portionDescription = "1 medium piece (~35g)", calories = 100, proteinG = 3.5f, carbsG = 20f, fatG = 0.5f, iconEmoji = "🫓"),
                FoodItemEntity(name = "Paratha (Home-made)", banglaName = "পরোটা", category = "Rice & Carbs", portionDescription = "1 medium piece with oil", calories = 260, proteinG = 4.5f, carbsG = 32f, fatG = 12.0f, iconEmoji = "🥞"),
                FoodItemEntity(name = "Chira (Flattened Rice)", banglaName = "চিঁড়া", category = "Rice & Carbs", portionDescription = "1 cup soaked (~50g dry)", calories = 180, proteinG = 3.0f, carbsG = 40f, fatG = 0.2f, iconEmoji = "🥣"),
                FoodItemEntity(name = "Oats (Cooked in water)", banglaName = "ওটস", category = "Rice & Carbs", portionDescription = "1 cup cooked (~40g dry)", calories = 150, proteinG = 5.5f, carbsG = 27f, fatG = 2.5f, iconEmoji = "🥣"),
                FoodItemEntity(name = "Boiled Potato", banglaName = "সেদ্ধ আলু", category = "Rice & Carbs", portionDescription = "1 medium (~100g)", calories = 110, proteinG = 2.5f, carbsG = 25f, fatG = 0.2f, iconEmoji = "🥔"),
                FoodItemEntity(name = "Boiled Egg", banglaName = "সেদ্ধ ডিম", category = "Protein", portionDescription = "1 whole large egg", calories = 72, proteinG = 6.3f, carbsG = 0.4f, fatG = 4.8f, iconEmoji = "🥚"),
                FoodItemEntity(name = "Chicken Breast/Curry", banglaName = "মুরগির মাংস", category = "Protein", portionDescription = "100g cooked, light oil", calories = 165, proteinG = 31.0f, carbsG = 1.0f, fatG = 4.5f, iconEmoji = "🍗"),
                FoodItemEntity(name = "Rui Fish Curry", banglaName = "রুই মাছ ভুনা/ঝোল", category = "Protein", portionDescription = "1 medium piece (~100g)", calories = 140, proteinG = 20.0f, carbsG = 1.0f, fatG = 5.5f, iconEmoji = "🐟"),
                FoodItemEntity(name = "Katla Fish Curry", banglaName = "কাতলা মাছ", category = "Protein", portionDescription = "1 piece (~100g)", calories = 135, proteinG = 19.0f, carbsG = 0.5f, fatG = 5.0f, iconEmoji = "🐟"),
                FoodItemEntity(name = "Ilish / Hilsa Fish", banglaName = "ইলিশ মাছ", category = "Protein", portionDescription = "1 medium piece (~100g)", calories = 270, proteinG = 22.0f, carbsG = 0.0f, fatG = 19.5f, iconEmoji = "🐟"),
                FoodItemEntity(name = "Beef Curry (Lean)", banglaName = "গরুর মাংসের ঝোল", category = "Protein", portionDescription = "100g lean pieces, light gravy", calories = 220, proteinG = 26.0f, carbsG = 2.0f, fatG = 11.0f, iconEmoji = "🥩"),
                FoodItemEntity(name = "Mug / Masoor Dal", banglaName = "মসুর বা মুগ ডাল", category = "Protein", portionDescription = "1 cup cooked (~150ml)", calories = 180, proteinG = 12.0f, carbsG = 28f, fatG = 1.2f, iconEmoji = "🥣"),
                FoodItemEntity(name = "Milk (Low Fat)", banglaName = "দুধ", category = "Protein", portionDescription = "1 glass (~200ml)", calories = 120, proteinG = 6.5f, carbsG = 10f, fatG = 3.0f, iconEmoji = "🥛"),
                FoodItemEntity(name = "Yogurt / Tok Doi", banglaName = "টক দই", category = "Protein", portionDescription = "1 cup (~150g)", calories = 120, proteinG = 8.5f, carbsG = 9f, fatG = 3.5f, iconEmoji = "🍶"),
                FoodItemEntity(name = "Banana (Shobri/Champa)", banglaName = "কলা", category = "Snacks", portionDescription = "1 medium banana", calories = 105, proteinG = 1.3f, carbsG = 27f, fatG = 0.3f, iconEmoji = "🍌"),
                FoodItemEntity(name = "Apple", banglaName = "আপেল", category = "Snacks", portionDescription = "1 medium apple", calories = 95, proteinG = 0.5f, carbsG = 25f, fatG = 0.3f, iconEmoji = "🍎"),
                FoodItemEntity(name = "Guava (Peyara)", banglaName = "পেয়ারা", category = "Snacks", portionDescription = "1 medium guava", calories = 68, proteinG = 2.6f, carbsG = 14f, fatG = 1.0f, iconEmoji = "🍏"),
                FoodItemEntity(name = "Cucumber", banglaName = "শসা", category = "Snacks", portionDescription = "1 cup sliced (~100g)", calories = 16, proteinG = 0.7f, carbsG = 3.6f, fatG = 0.1f, iconEmoji = "🥒"),
                FoodItemEntity(name = "Roasted Peanuts", banglaName = "বাদাম", category = "Snacks", portionDescription = "1 handful (~30g)", calories = 170, proteinG = 7.5f, carbsG = 6f, fatG = 14.0f, iconEmoji = "🥜"),
                FoodItemEntity(name = "Mixed Vegetable Sabzi", banglaName = "মিক্সড সবজি ভাজি", category = "Snacks", portionDescription = "1 cup cooked, light oil", calories = 85, proteinG = 2.5f, carbsG = 12f, fatG = 3.0f, iconEmoji = "🥬")
            )
            database.foodItemDao().insertFoods(foods)

            // 5. Today's Meals
            val meals = listOf(
                MealEntity(
                    date = todayDate,
                    mealType = "Breakfast",
                    title = "Eggs, Roti & Fruit",
                    description = "2 boiled eggs + 1 whole wheat roti + 1 banana",
                    calories = 349,
                    proteinG = 17.4f,
                    portion = "2 eggs, 1 roti, 1 fruit",
                    isCompleted = true,
                    dayOfWeek = "Today"
                ),
                MealEntity(
                    date = todayDate,
                    mealType = "Lunch",
                    title = "Rice, Fish, Dal & Greens",
                    description = "1 cup cooked rice + 1 piece Rui fish curry + 1 cup Dal + mixed leafy greens",
                    calories = 580,
                    proteinG = 38.0f,
                    portion = "1 cup rice, 1 piece fish, 1 bowl dal",
                    isCompleted = false,
                    dayOfWeek = "Today"
                ),
                MealEntity(
                    date = todayDate,
                    mealType = "Dinner",
                    title = "Chicken, Greens & Roti",
                    description = "150g grilled/curry chicken breast + 1 roti + sauteed vegetables",
                    calories = 380,
                    proteinG = 35.0f,
                    portion = "150g chicken, 1 roti, greens",
                    isCompleted = false,
                    dayOfWeek = "Today"
                ),
                MealEntity(
                    date = todayDate,
                    mealType = "Snack",
                    title = "Afternoon Refuel",
                    description = "1 cup sliced cucumber + 1 handful roasted peanuts / green tea",
                    calories = 186,
                    proteinG = 8.2f,
                    portion = "1 bowl cucumber & nuts",
                    isCompleted = false,
                    dayOfWeek = "Today"
                )
            )
            database.mealDao().insertMeals(meals)

            // 6. Progress Photos Presets
            val photos = listOf(
                ProgressPhotoEntity(weekLabel = "Week 1", angle = "Front", date = "Day 1", weightKg = 62.0f, waistCm = 80f, samplePresetId = 1),
                ProgressPhotoEntity(weekLabel = "Week 1", angle = "Side", date = "Day 1", weightKg = 62.0f, waistCm = 80f, samplePresetId = 2),
                ProgressPhotoEntity(weekLabel = "Week 1", angle = "Back", date = "Day 1", weightKg = 62.0f, waistCm = 80f, samplePresetId = 3),
                ProgressPhotoEntity(weekLabel = "Week 4", angle = "Front", date = "Day 28", weightKg = 60.8f, waistCm = 78f, samplePresetId = 4),
                ProgressPhotoEntity(weekLabel = "Week 4", angle = "Side", date = "Day 28", weightKg = 60.8f, waistCm = 78f, samplePresetId = 5),
                ProgressPhotoEntity(weekLabel = "Week 8", angle = "Front", date = "Target Goal", weightKg = 58.0f, waistCm = 72f, samplePresetId = 6)
            )
            database.progressPhotoDao().insertPhotos(photos)

            // 7. Achievements
            val achievements = listOf(
                AchievementEntity(id = "first_rep", title = "First Rep", description = "Complete your very first workout session", iconEmoji = "🏋️", isUnlocked = true, progress = 100, unlockedDate = "Today"),
                AchievementEntity(id = "week_strong", title = "One Week Strong", description = "Maintain 7 days of daily habit consistency", iconEmoji = "🔥", isUnlocked = false, progress = 4, maxProgress = 7),
                AchievementEntity(id = "building_strength", title = "Building Strength", description = "Complete 10 full body home workouts", iconEmoji = "💪", isUnlocked = false, progress = 3, maxProgress = 10),
                AchievementEntity(id = "first_milestone", title = "First Milestone", description = "Lose first 2 kg towards your target", iconEmoji = "🎯", isUnlocked = false, progress = 12, maxProgress = 20),
                AchievementEntity(id = "habit_builder", title = "Habit Builder", description = "Log your meals and progress for 30 consecutive days", iconEmoji = "🏆", isUnlocked = false, progress = 6, maxProgress = 30)
            )
            database.achievementDao().insertAchievements(achievements)
        }
    }
}
