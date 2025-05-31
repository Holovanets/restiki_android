package com.example.restiki_android.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restiki_android.ui.auth.AuthViewModel
import com.example.restiki_android.ui.auth.AuthViewModelFactory
import com.example.restiki_android.ui.auth.CodeScreen
import com.example.restiki_android.ui.auth.PhoneScreen
import com.example.restiki_android.ui.places.PlacesScreen

private val Context.dataStore by preferencesDataStore(name = "settings")

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current.dataStore)
    )
    NavHost(
        navController = navController,
        startDestination = "phone",
        modifier = modifier
    ) {
        composable("phone") {
            PhoneScreen(
                viewModel = viewModel,
                onNavigateToCode = { navController.navigate("code") }
            )
        }
        composable("code") {
            CodeScreen(
                viewModel = viewModel,
                onNavigateToPlaces = { navController.navigate("places") }
            )
        }
        composable("places") {
            PlacesScreen(
//                viewModel = viewModel,
                onNavigateToPlace = { /* Будет реализовано позже */ }
            )
        }
    }
}