package com.example.workoutapp.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.data.diet.model.DietDay
import com.example.workoutapp.data.history.DietHistoryViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun DietHistoryScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: DietHistoryViewModel = hiltViewModel()
) {
    val dietHistory = viewModel.dietHistory.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Diet History",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp).padding(horizontal = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dietHistory) { dietDay ->
                DietCard(
                    dietDay = dietDay,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun DietCard(
    dietDay: DietDay,
    viewModel: DietHistoryViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Diet for ${dietDay.dateAdded}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Delete Diet"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Total Calories: ${String.format("%.1f", dietDay.totalCalories)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Total Protein: ${String.format("%.1f", dietDay.totalProtein)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Total Fat: ${String.format("%.1f", dietDay.totalFat)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Total Carbs: ${String.format("%.1f", dietDay.totalCarbs)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            dietDay.meals.forEachIndexed { index, meal ->
                Text(
                    text = "${index + 1}. ${meal.name} - ${meal.calories} calories",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                DietMacroBarChart(
                    protein = dietDay.totalProtein,
                    fat = dietDay.totalFat,
                    carbs = dietDay.totalCarbs
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete diet day") },
            text = { Text("Are you sure you want to delete all meals from this day?") },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        viewModel.deleteDietByDate(dietDay.dateAdded)
                        showDialog = false
                    }) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

@Composable
fun DietMacroBarChart(
    protein: Double,
    fat: Double,
    carbs: Double,
    modifier: Modifier = Modifier
) {
    val bars = listOf(
        BarChartData.Bar(label = "Protein", value = protein.toFloat(), color = Color(0xFF4CAF50)),
        BarChartData.Bar(label = "Fat", value = fat.toFloat(), color = Color(0xFFFFC107)),
        BarChartData.Bar(label = "Carbs", value = carbs.toFloat(),color = Color(0xFF2196F3)
        )
    )

    BarChart(
        barChartData = BarChartData(bars = bars),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        labelDrawer = SimpleValueDrawer()
    )
}
