package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Ratul",
    val goal: String = "Lose fat & become lean",
    val heightCm: Float = 156f,
    val currentWeightKg: Float = 60.8f,
    val startingWeightKg: Float = 60.8f,
    val targetWeightKg: Float = 58.0f,
    val currentWaistCm: Float = 78f,
    val targetWaistCm: Float = 72f,
    val age: Int = 26,
    val sex: String = "Male",
    val activityLevel: String = "Lightly active",
    val exerciseLocation: String = "Home",
    val targetCalories: Int = 1800,
    val targetProteinG: Int = 95,
    val targetWaterL: Float = 2.5f,
    val targetSteps: Int = 8000,
    val isOnboarded: Boolean = true,
    val unitWeight: String = "kg",
    val unitHeight: String = "cm"
)

@Entity(tableName = "daily_logs")
data class DailyLogEntity(
    @PrimaryKey val date: String, // format: "YYYY-MM-DD"
    val caloriesConsumed: Int = 1240,
    val proteinConsumedG: Int = 68,
    val waterConsumedL: Float = 1.5f,
    val stepsWalked: Int = 4892,
    val habitNutrition: Boolean = true,
    val habitWorkout: Boolean = true,
    val habitSteps: Boolean = false,
    val habitWater: Boolean = true,
    val notes: String = ""
)

@Entity(tableName = "weight_entries")
data class WeightEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val weightKg: Float,
    val waistCm: Float? = null,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "food_database")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val banglaName: String = "",
    val category: String, // Rice & Carbs, Protein, Snacks, Veg
    val portionDescription: String,
    val defaultQuantity: Float = 1.0f,
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val isBangladeshiLocal: Boolean = true,
    val iconEmoji: String = "🍲"
)

@Entity(tableName = "meal_items")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val title: String,
    val description: String,
    val calories: Int,
    val proteinG: Float,
    val portion: String,
    val isCompleted: Boolean = false,
    val isReplaced: Boolean = false,
    val replacedWith: String = "",
    val dayOfWeek: String = "Today" // "Today" or specific day like "Mon", "Tue"
)

@Entity(tableName = "progress_photos")
data class ProgressPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weekLabel: String, // "Week 1", "Week 4", "Week 8"
    val angle: String, // "Front", "Side", "Back"
    val date: String,
    val weightKg: Float,
    val waistCm: Float,
    val imageUri: String? = null, // URI string if selected
    val samplePresetId: Int = 1 // Preset placeholder identifier
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val maxProgress: Int = 100,
    val unlockedDate: String? = null
)
