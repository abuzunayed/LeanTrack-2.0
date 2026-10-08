package com.example.ui.screens.meals

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.FoodItemEntity
import com.example.data.entity.MealEntity
import com.example.ui.components.LeanCard
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
fun MealsScreen(
    meals: List<MealEntity>,
    foodDatabase: List<FoodItemEntity>,
    onToggleComplete: (MealEntity) -> Unit,
    onReplaceMealClick: (MealEntity) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Today, 1: Week, 2: Food DB
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .testTag("meals_screen")
    ) {
        // Screen Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Meal Plan & Nutrition",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Realistic local nutrition with flexible substitutions",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation tabs: Today, Week, Local Foods
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = ForestGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = ForestGreen
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Today",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Week Plan",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "Food Database 🇧🇩",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> TodayMealsTab(
                meals = meals,
                onToggleComplete = onToggleComplete,
                onReplaceMealClick = onReplaceMealClick
            )
            1 -> WeekMealsTab()
            2 -> FoodDatabaseTab(
                foodDatabase = foodDatabase,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it }
            )
        }
    }
}

@Composable
fun TodayMealsTab(
    meals: List<MealEntity>,
    onToggleComplete: (MealEntity) -> Unit,
    onReplaceMealClick: (MealEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Helpful coaching banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MintSubtle)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💡", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Eat with your family without guilt. Use \"Portion Control Mode\" or swap ingredients anytime with ↻ Replace.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        items(meals) { meal ->
            MealCardItem(
                meal = meal,
                onToggleComplete = { onToggleComplete(meal) },
                onReplaceClick = { onReplaceMealClick(meal) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun MealCardItem(
    meal: MealEntity,
    onToggleComplete: () -> Unit,
    onReplaceClick: () -> Unit
) {
    val statusSymbol = if (meal.isCompleted) "✅" else "⬜"
    val isReplaced = meal.isReplaced

    LeanCard(
        modifier = Modifier.testTag("meal_card_${meal.mealType.lowercase()}"),
        backgroundColor = if (meal.isCompleted) Color(0xFFF9FAF7) else WarmSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${meal.mealType} — $statusSymbol",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )
                if (isReplaced) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F1EC)
                    ) {
                        Text(
                            text = "Custom Swap",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "${meal.calories} kcal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = meal.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Charcoal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = meal.description,
            style = MaterialTheme.typography.bodyMedium,
            color = CharcoalMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Protein: ${meal.proteinG}g  •  Portion: ${meal.portion}",
                style = MaterialTheme.typography.labelSmall,
                color = CharcoalMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onToggleComplete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (meal.isCompleted) SoftMint else ForestGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("complete_meal_${meal.mealType.lowercase()}")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (meal.isCompleted) "Completed" else "Complete",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            OutlinedButton(
                onClick = onReplaceClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("replace_meal_${meal.mealType.lowercase()}")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ForestGreen
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Replace",
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun WeekMealsTab() {
    val weekDays = listOf(
        "Monday" to "🍳 Eggs & Roti • 🐟 Rui Fish Curry (1 cup rice) • 🍗 Chicken & Greens",
        "Tuesday" to "🥣 Oats & Milk • 🍗 Chicken Curry & Dal • 🐟 Katla Fish & Roti",
        "Wednesday" to "🍳 2 Boiled Eggs • 🥩 Lean Beef Curry (1 cup rice) • 🥣 Dal, Veggies & Roti",
        "Thursday" to "🫓 Roti & Banana • 🐟 Ilish Fish piece & Greens • 🍗 Grilled Chicken Salad",
        "Friday" to "🍳 Eggs & Chira • 🍗 Chicken Roast (portion-controlled rice) • 🥣 Light Dal & Veg",
        "Saturday" to "🥣 Tok Doi & Fruit • 🐟 Rui Fish & Mixed Sabzi • 🫓 Roti & Egg Bhurji",
        "Sunday" to "🍳 2 Eggs & Roti • 🍛 Family Lunch (Portion Mode) • 🍗 Lean Chicken Breast"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(weekDays) { (day, plan) ->
            LeanCard {
                Text(
                    text = day,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = plan,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Average: ~1,750 kcal • ~90g protein",
                    style = MaterialTheme.typography.labelSmall,
                    color = CharcoalMuted
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun FoodDatabaseTab(
    foodDatabase: List<FoodItemEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {
    val categories = listOf("All", "Rice & Carbs", "Protein", "Snacks")

    val filteredFoods = foodDatabase.filter { food ->
        val matchesCategory = selectedCategory == "All" || food.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                food.name.contains(searchQuery, ignoreCase = true) ||
                food.banglaName.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search local Bangladeshi foods (e.g. Rui, Roti, Chira)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("food_search_input"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category filter pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ForestGreen else Color(0xFFF1F3EC),
                        modifier = Modifier.clickable { onCategorySelect(category) }
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Charcoal,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Logged by everyday portions (cups, pieces) rather than requiring kitchen scales.",
                style = MaterialTheme.typography.labelSmall,
                color = CharcoalMuted
            )
        }

        items(filteredFoods) { food ->
            FoodDatabaseItemRow(food = food)
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun FoodDatabaseItemRow(food: FoodItemEntity) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WarmSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F7F1)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = food.iconEmoji, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                    if (food.banglaName.isNotBlank()) {
                        Text(
                            text = food.banglaName,
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen
                        )
                    }
                    Text(
                        text = food.portionDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${food.calories} kcal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
                Text(
                    text = "${food.proteinG}g protein",
                    style = MaterialTheme.typography.labelSmall,
                    color = OrangeFlame,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
