package com.example.restiki_android.ui.places.components.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restiki_android.data.api.SpotItem

@Composable
fun DistanceBadge(spot: SpotItem) {
    Box(
        modifier = Modifier
            .background(Color(0xFF070707), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${spot.location.distance?.toInt() ?: 0} м",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun WalkTimeBadge(spot: SpotItem) {
    Box(
        modifier = Modifier
            .background(Color(0xFF070707), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "4 мин", // Замени на реальную логику расчета времени ходьбы
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ReviewsBadge(spot: SpotItem, modifier: Modifier = Modifier) {
    val totalStars = spot.stars?.total ?: 0
    val avgRating = if (totalStars > 0) {
        (spot.stars?.let { (it.star1 + it.star2 * 2 + it.star3 * 3 + it.star4 * 4 + it.star5 * 5) / totalStars.toFloat() } ?: 0f)
    } else 0f
    Box(
        modifier = modifier
            .background(Color(0xFF070707), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${"%.1f".format(avgRating)} (${totalStars})",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}