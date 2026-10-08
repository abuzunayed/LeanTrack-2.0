package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRatulCalorieAndProteinTargetCalculations() {
        val weightKg = 60.8f
        val heightCm = 156f
        val age = 26
        val sex = "Male"
        val activity = "Lightly active"
        val goal = "Lose fat & become lean"

        // BMR = 10 * 60.8 + 6.25 * 156 - 5 * 26 + 5 = 608 + 975 - 130 + 5 = 1458 kcal
        val bmr = (10 * weightKg) + (6.25f * heightCm) - (5 * age) + 5
        assertEquals(1458f, bmr, 1f)

        // TDEE = 1458 * 1.375 = 2004.75 kcal
        val activityMultiplier = 1.375f
        val maintenance = bmr * activityMultiplier
        assertTrue(maintenance in 1990f..2020f)

        // Moderate sustainable deficit of 350 kcal -> ~1654 kcal (well within healthy 1500-1800 kcal range)
        val targetCalories = (maintenance - 350f).toInt()
        assertTrue(targetCalories in 1600..1700)

        // Protein target: 1.6g per kg of bodyweight
        val targetProtein = (weightKg * 1.6f).toInt()
        assertEquals(97, targetProtein)
    }
}
