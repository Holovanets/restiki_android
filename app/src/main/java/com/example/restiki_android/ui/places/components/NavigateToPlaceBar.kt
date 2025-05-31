package com.example.restiki_android.ui.places.components.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restiki_android.data.api.SpotItem

@Composable
fun NavigateToPlaceBar(spot: SpotItem) {
    val context = LocalContext.current

    fun openGoogleMaps(lat: Double, lng: Double) {
        val url = "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            println("Couldn't load page: ${e.message}")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x20FFFFFF), RoundedCornerShape(10.dp)) // Colors.light01
            .border(1.dp, Color(0xFF4A4A4A), RoundedCornerShape(10.dp)) // Colors.light20
            .clickable { openGoogleMaps(spot.location.geolat, spot.location.geolng) }
            .padding(horizontal = 5.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Левая часть: иконка, город, адрес
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                contentDescription = "Location",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.size(24.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spot.location.city ?: "Завантажую",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = spot.location.address ?: "",
                    color = Color.White, // Colors.light
                    lineHeight = 14.sp,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier
                .background(Color(0x20FFFFFF), RoundedCornerShape(5.dp))
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_map),
                contentDescription = "Map",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Маршрут",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}