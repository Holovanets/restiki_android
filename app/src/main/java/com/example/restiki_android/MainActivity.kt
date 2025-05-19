package com.example.restiki_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.restiki_android.ui.navigation.NavGraph
import com.example.restiki_android.ui.theme.Restiki_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Restiki_androidTheme {
                NavGraph(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}