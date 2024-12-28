package com.example.workoutapp.presentation.diet

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.example.workoutapp.data.diet.model.DailyDietItem
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutapp.data.diet.model.DietViewModel
import java.time.LocalDate

@Composable
fun AddCustomFoodScreen(
    navController: NavHostController,
    viewModel: DietViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var caloriesError by remember { mutableStateOf<String?>(null) }
    var proteinError by remember { mutableStateOf<String?>(null) }
    var fatError by remember { mutableStateOf<String?>(null) }
    var carbsError by remember { mutableStateOf<String?>(null) }
    var weightError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Fill in the following fields:",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        if (nameError != null) {
            Text(
                text = nameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories per 100g") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (caloriesError != null) {
            Text(
                text = caloriesError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein per 100g") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (proteinError != null) {
            Text(
                text = proteinError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = fat,
            onValueChange = { fat = it },
            label = { Text("Fat per 100g") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (fatError != null) {
            Text(
                text = fatError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carbohydrates per 100g") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (carbsError != null) {
            Text(
                text = carbsError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (grams)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (weightError != null) {
            Text(
                text = weightError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                var isValid = true

                if (name.isBlank()) {
                    nameError = "Name cannot be empty"
                    isValid = false
                } else {
                    nameError = null
                }

                if (calories.isBlank() || calories.toDoubleOrNull() == null || calories.toDouble() < 0) {
                    caloriesError = "Invalid number for calories"
                    isValid = false
                } else {
                    caloriesError = null
                }

                if (protein.isBlank() || protein.toDoubleOrNull() == null || protein.toDouble() < 0) {
                    proteinError = "Invalid number for protein"
                    isValid = false
                } else {
                    proteinError = null
                }

                if (fat.isBlank() || fat.toDoubleOrNull() == null || fat.toDouble() < 0) {
                    fatError = "Invalid number for fat"
                    isValid = false
                } else {
                    fatError = null
                }

                if (carbs.isBlank() || carbs.toDoubleOrNull() == null || carbs.toDouble() < 0) {
                    carbsError = "Invalid number for carbs"
                    isValid = false
                } else {
                    carbsError = null
                }

                if (weight.isBlank() || weight.toDoubleOrNull() == null  || weight.toDouble() <= 0) {
                    weightError = "Invalid weight"
                    isValid = false
                } else {
                    weightError = null
                }

                if (isValid){
                    val weightInGrams = weight.toDouble()
                    val multiplier = weightInGrams / 100
                    viewModel.addCustomMeal(
                        DailyDietItem(
                            name = name,
                            calories = calories.toDouble() * multiplier,
                            servingSize = weightInGrams,
                            protein = protein.toDouble() * multiplier,
                            fat = fat.toDouble() * multiplier,
                            carbs = carbs.toDouble() * multiplier,
                            dateAdded = LocalDate.now()
                        )
                    )
                    navController.navigateUp()
                }
            }
        ) {
            Text("Add Food")
        }
    }
}
