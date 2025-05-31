package com.example.restiki_android.ui.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restiki_android.ui.places.components.PlaceCard

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = viewModel(
        factory = PlacesViewModelFactory()
    ),
    onNavigateToPlace: (Int) -> Unit
) {
    val spots = viewModel.spots
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNearestSpots()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF110021),
                        Color(0xFF5B000A)
                    )
                )),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        if (isLoading || spots.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Завантаження списку закладів...",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        } else {
            items(spots) { spot ->
                PlaceCard(spot = spot, onNavigateToPlace = onNavigateToPlace)
            }
        }
    }
}