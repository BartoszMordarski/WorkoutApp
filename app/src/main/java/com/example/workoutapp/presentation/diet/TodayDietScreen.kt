package com.example.workoutapp.presentation.diet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.workoutapp.data.diet.model.DailyDietItem
import com.example.workoutapp.data.diet.model.DietViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun TodayDietScreen(navController: NavHostController, viewModel: DietViewModel = hiltViewModel()) {
    val currentDate = remember { Calendar.getInstance().time }
    val dateFormatter = SimpleDateFormat("EEEE dd.MM", Locale.ENGLISH)
    val formattedDate = dateFormatter.format(currentDate)

    val todayDiet by viewModel.todayDiet.collectAsState(initial = emptyList())

    val totalCalories = todayDiet.sumOf { it.calories }.roundToInt()
    val totalProtein = todayDiet.sumOf { it.protein }.roundToInt()
    val totalFat = todayDiet.sumOf { it.fat }.roundToInt()
    val totalCarbs = todayDiet.sumOf { it.carbs }.roundToInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                )
            }

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        if (todayDiet.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No meals added today", style = MaterialTheme.typography.headlineMedium)
            }
        } else {
            Text("Total Calories: $totalCalories kcal", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Total Protein: $totalProtein g", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFFFFC107))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Total Fat: $totalFat g", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFF2196F3))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Total Carbs: $totalCarbs g", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            PieChart(
                pieChartData = PieChartData(
                    slices = listOf(
                        PieChartData.Slice(totalProtein.toFloat(), color = Color(0xFF4CAF50)),
                        PieChartData.Slice(totalFat.toFloat(), color =  Color(0xFFFFC107)),
                        PieChartData.Slice(totalCarbs.toFloat(), color = Color(0xFF2196F3))
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                animation = simpleChartAnimation(),
                sliceDrawer = SimpleSliceDrawer()
            )

            Text("Meals:", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                itemsIndexed(todayDiet) { index, dietItem ->
                    DietItemCard(
                        index + 1,
                        dietItem,
                        onDelete = {
                            viewModel.deleteDietItem(it)
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun DietItemCard(
    number: Int,
    dietItem: DailyDietItem,
    onDelete: (DailyDietItem) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding( vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$number. " + dietItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Meal",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Text(
                text = "Serving Size: ${dietItem.servingSize} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Calories: ${String.format("%.1f", dietItem.calories)} kcal",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Protein: ${String.format("%.1f", dietItem.protein)} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fat: ${String.format("%.1f", dietItem.fat)} g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Carbohydrates: ${String.format("%.1f", dietItem.carbs)} g",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Meal") },
            text = { Text("Are you sure you want to delete this meal?") },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onDelete(dietItem)
                            showDialog = false
                        }
                    ) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

