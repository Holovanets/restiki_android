package com.example.restiki_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restiki_android.ui.auth.CodeScreen
import com.example.restiki_android.ui.auth.PhoneScreen
import com.example.restiki_android.ui.places.PlacesScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "phone",
        modifier = modifier
    ) {
        composable("phone") {
            PhoneScreen(onNavigateToCode = { navController.navigate("code") })
        }
        composable("code") {
            CodeScreen(onNavigateToPlaces = { navController.navigate("places") })
        }
        composable("places") {
            PlacesScreen(onNavigateToPlace = { /* Будет реализовано позже */ })
        }
    }
}