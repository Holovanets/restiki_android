package com.example.restiki_android.ui.places.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.restiki_android.data.api.SpotItem
import com.example.restiki_android.ui.places.components.badges.DistanceBadge
import com.example.restiki_android.ui.places.components.badges.ReviewsBadge
import com.example.restiki_android.ui.places.components.badges.WalkTimeBadge
import com.example.restiki_android.ui.places.components.navigation.NavigateToPlaceBar

@Composable
fun PlaceCard(
    spot: SpotItem,
    onNavigateToPlace: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xAA070707))
            .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(16.dp)) // light20
            .clickable { onNavigateToPlace(spot.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
        ) {
            PlaceImage(spot)
            PlaceInfo(spot)
            Text(
                text = spot.description ?: "Без опису",
                color = Color(0x50FFFFFF),
                fontSize = 12.sp,
                lineHeight = 15.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 12.dp)
            )
            NavigateToPlaceBar(spot)
        }
    }
}

@Composable
fun PlaceImage(spot: SpotItem) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF070707))
        )
        // Добавь AsyncImage, если используешь Coil:
         AsyncImage(
             model = ImageRequest.Builder(LocalContext.current)
                 .data(spot.poster ?: null)
                 .crossfade(true)
                 .build(),
             contentDescription = spot.name,
             contentScale = ContentScale.Crop,
             modifier = Modifier.fillMaxSize()
         )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DistanceBadge(spot)
            WalkTimeBadge(spot)
        }
        ReviewsBadge(
            spot,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp)
        )
    }
}

@Composable
fun PlaceInfo(spot: SpotItem) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White) // light
                .border(2.dp, Color(0xFF4A4A4A), RoundedCornerShape(10.dp)) // light20
        ) {
            // Placeholder для логотипа
             AsyncImage(model = spot.logo ?: null, contentDescription = spot.name)
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = spot.name.uppercase(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = spot.slug ?: "",
                color = Color(0xFF808080), // light50
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}