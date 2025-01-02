package com.example.workoutapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutapp.data.diet.model.DietViewModel
import com.example.workoutapp.data.history.HistoryViewModel
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import kotlin.math.roundToInt

@Composable
fun HomeView(
    paddingValues: PaddingValues,
    historyViewModel: HistoryViewModel = hiltViewModel(),
    dietViewModel: DietViewModel = hiltViewModel(),
) {
    val workoutHistory by historyViewModel.workoutHistory.collectAsState()
    val lastWorkout = workoutHistory.maxByOrNull { it.workout.workoutDate }
    val weeklyWorkoutCounts by historyViewModel.weeklyWorkoutCounts

    val todayDiet by dietViewModel.todayDiet.collectAsState(initial = emptyList())
    val totalCalories = todayDiet.sumOf { it.calories }.roundToInt()
    val totalProtein = todayDiet.sumOf { it.protein }.roundToInt()
    val totalFat = todayDiet.sumOf { it.fat }.roundToInt()
    val totalCarbs = todayDiet.sumOf { it.carbs }.roundToInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = paddingValues.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "“The secret of getting ahead is getting started.”",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item {
            val es = listOf(
                Pair("12-01", 5),
                Pair("12-08", 3),
                Pair("12-15", 4),
                Pair("12-22", 6),
                Pair("12-29", 7)
            )

            WorkoutLineChart(workoutCounts = es)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Diet Overview",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Icon(
                    imageVector = Icons.Default.FoodBank,
                    contentDescription = "Diet Overview Icon",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (todayDiet.isNotEmpty()) {
                        Text(text = "Total Calories: $totalCalories kcal")
                        Text(text = "Protein: $totalProtein g")
                        Text(text = "Carbs: $totalCarbs g")
                        Text(text = "Fat: $totalFat g")

                        Spacer(modifier = Modifier.height(8.dp))

                        todayDiet.forEachIndexed { index, dietItem ->
                            Text(
                                text = "${index + 1}. ${dietItem.name} - ${dietItem.calories.roundToInt()} kcal",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        Text(text = "No meals added yet.")
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Last Workout",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Icon(
                    imageVector = Icons.Default.DirectionsRun,
                    contentDescription = "Last Workout",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (lastWorkout != null) {
                        Text(
                            text = lastWorkout.workout.workoutName,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Workout Date",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = lastWorkout.workout.workoutDate)

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Workout Duration",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${lastWorkout.workout.duration}")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        lastWorkout.exercises.forEach { exercise ->
                            Text(
                                text = "${exercise.sets.size} x ${exercise.workoutExercise.exerciseName}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    } else {

                        Text(
                            text = "No workout history available."
                        )


                    }
                }
            }
        }
    }
}


@Composable
fun WorkoutLineChart(workoutCounts: List<Pair<String, Int>>) {
    val points = workoutCounts.map { (weekDate, count) ->
        LineChartData.Point(count.toFloat(), weekDate)
    }

    val lineChartData = LineChartData(
        points = points,
        lineDrawer = SolidLineDrawer(thickness = 2.dp, color = Color.Green)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .wrapContentSize(Alignment.Center),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Gray)
    ) {
        LineChart(
            linesChartData = listOf(lineChartData),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            animation = simpleChartAnimation(),
            pointDrawer = FilledCircularPointDrawer(color = Color.Black),
            xAxisDrawer = SimpleXAxisDrawer(),
            yAxisDrawer = SimpleYAxisDrawer(),
            horizontalOffset = 5f,
            labels = workoutCounts.map { it.first },
        )
    }
}






