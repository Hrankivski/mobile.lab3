package com.example.lab3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab3.screens.SolarScreen

@Composable
fun CalculatorApp() {
    var selected by remember { mutableStateOf(0) }
    val tabs = listOf("Прогноз & Прибуток")

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text(
            text = "Калькулятор прибутку сонячної електростанції",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TabRow(selectedTabIndex = selected) {
            tabs.forEachIndexed { i, t ->
                Tab(selected = selected == i, onClick = { selected = i }, text = { Text(t) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selected) {
            0 -> SolarScreen()
        }
    }
}
