package com.example.workoutapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeView(paddingValues: PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Text(
            text = "Hello, this is the Home screen!",
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}


