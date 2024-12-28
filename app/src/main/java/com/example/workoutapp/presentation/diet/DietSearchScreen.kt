package com.example.workoutapp.presentation.diet

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.workoutapp.R
import com.example.workoutapp.data.diet.api.FoodItem
import com.example.workoutapp.data.diet.model.DietViewModel

@Composable
fun DietSearchScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: DietViewModel = hiltViewModel()
) {
    val foodItems by viewModel.foods.collectAsState()
    var query by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search for foods") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        query = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel"
                    )
                }

                IconButton(
                    onClick = {
                        if (query.isNotBlank()) {
                            viewModel.searchFoods(query)
                            showResults = true
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        } else {
                            showResults = false
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                showResults && foodItems.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = "No results, try a different query or add a custom meal.",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { navController.navigate("add_custom_meal_screen") },
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .defaultMinSize(minWidth = 150.dp)
                            ) {
                                Text(text = "Add Custom Food")
                            }
                        }
                    }
                }

                showResults -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(foodItems) { food ->
                            FoodItemRow(
                                food = food,
                                onAddToDiet = { viewModel.addFoodToDiet(food) }
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Search for foods")
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("today_diet_screen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "See Today's Diet")
        }
    }
}

@Composable
fun FoodItemRow(food: FoodItem, onAddToDiet: (FoodItem) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Calories: ${food.calories} kcal",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Serving size: ${food.serving_size_g} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Protein: ${food.protein_g} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fat: ${food.fat_total_g} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Carbohydrates: ${food.carbohydrates_total_g} g",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { onAddToDiet(food) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add to Today Diet")
            }
        }
    }
}
