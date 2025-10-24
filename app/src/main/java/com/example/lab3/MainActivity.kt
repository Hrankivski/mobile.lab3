package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.lab3.ui.theme.Lab3Theme
import com.example.lab3.screens.SolarScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    SolarScreen()
                }
            }
        }
    }
}
