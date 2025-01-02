package com.example.workoutapp.presentation.diet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Diet Search",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search for foods") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { query = "" },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

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
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "No results found.\nTry a different query or add a custom meal.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { navController.navigate("add_custom_meal_screen") },
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .defaultMinSize(minWidth = 150.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(text = "Add Custom Meal")
                            }
                        }
                    }
                }

                showResults -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f).padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(foodItems) { food ->
                            FoodItemRow(
                                food = food,
                                onAddToDiet = { viewModel.addFoodToDiet(food) },
                                viewModel
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Search for foods...",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
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
fun FoodItemRow(food: FoodItem, onAddToDiet: (FoodItem) -> Unit, viewModel: DietViewModel) {
    val isAdded = viewModel.isFoodAdded(food)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary
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

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
            {
                Button(
                    onClick = {
                        if (!isAdded) {
                            onAddToDiet(food)
                        }
                    },
                    enabled = !isAdded,
                    modifier = Modifier
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF99FF99),
                        contentColor = Color.Black
                    )
                ) {
                    if (isAdded) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Added!")
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Added"
                            )
                        }
                    } else {
                        Text(text = "Add to Today Diet")
                    }
                }
            }

        }
    }
}


