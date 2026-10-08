package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AchievementEntity
import com.example.data.entity.DailyLogEntity
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.data.entity.ProgressPhotoEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}

@Dao
interface DailyLogDao {
    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    fun getLogForDateFlow(date: String): Flow<DailyLogEntity?>

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    suspend fun getLogForDate(date: String): DailyLogEntity?

    @Query("SELECT * FROM daily_logs ORDER BY date DESC LIMIT 30")
    fun getRecentLogsFlow(): Flow<List<DailyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyLog(log: DailyLogEntity)
}

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM weight_entries ORDER BY timestamp ASC")
    fun getAllWeightEntriesFlow(): Flow<List<WeightEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightEntry(entry: WeightEntryEntity): Long

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteWeightEntry(id: Long)
}

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_database ORDER BY category, name ASC")
    fun getAllFoodsFlow(): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_database WHERE category = :category ORDER BY name ASC")
    fun getFoodsByCategoryFlow(category: String): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_database WHERE name LIKE '%' || :query || '%' OR banglaName LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFoodsFlow(query: String): Flow<List<FoodItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodItemEntity): Long
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_items WHERE date = :date ORDER BY id ASC")
    fun getMealsForDateFlow(date: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meal_items WHERE dayOfWeek = :dayOfWeek ORDER BY id ASC")
    fun getMealsForDayFlow(dayOfWeek: String): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Query("UPDATE meal_items SET isCompleted = :completed WHERE id = :id")
    suspend fun setMealCompleted(id: Long, completed: Boolean)

    @Query("UPDATE meal_items SET title = :newTitle, description = :newDescription, calories = :newCalories, proteinG = :newProtein, isReplaced = 1, replacedWith = :replacedWith WHERE id = :id")
    suspend fun replaceMeal(id: Long, newTitle: String, newDescription: String, newCalories: Int, newProtein: Float, replacedWith: String)
}

@Dao
interface ProgressPhotoDao {
    @Query("SELECT * FROM progress_photos ORDER BY weekLabel ASC, angle ASC")
    fun getAllPhotosFlow(): Flow<List<ProgressPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: ProgressPhotoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<ProgressPhotoEntity>)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY id ASC")
    fun getAllAchievementsFlow(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)
}
