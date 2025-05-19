package com.example.restiki_android.ui.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CodeScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToPlaces: () -> Unit
) {
    val state by viewModel.authState.collectAsState()
    var passcode by rememberSaveable { mutableStateOf(listOf<String>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Обработка ошибок
    LaunchedEffect(state.error) {
        if (state.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.error)
            passcode = emptyList() // Сбрасываем код при ошибке
        }
    }

    // Навигация
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToPlaces()
        }
    }

    // Автоматическая отправка кода при вводе 4 цифр
    LaunchedEffect(passcode) {
        if (passcode.size == 4) {
            viewModel.verifyCode(passcode.joinToString(""))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C2526)) // Colors.darky
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Текст с номером телефона
            Text(
                text = "Введіть код надісланий на ${viewModel.getCurrentPhone()}",
                color = Color(0x80FFFFFF), // Colors.light50
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            // Поле для кода
            PassCode(
                passcode = passcode,
                isValid = passcode.size != 4 || state.isSuccess
            )

            // Кастомная клавиатура
            PassCodeKeyboard(
                onPress = { char ->
                    if (char == "delete") {
                        passcode = passcode.dropLast(1)
                    } else if (passcode.size < 4) {
                        passcode = passcode + char
                    }
                }
            )

            // Кнопка Telegram
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/golovanetss"))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x26007FBA), // Colors.mDark15
                    contentColor = Color(0xFF007FBA) // Colors.mDark
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Код не дійшов або не підходить",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Snackbar для ошибок
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun PassCode(passcode: List<String>, isValid: Boolean) {
    val shakeAnimation = rememberInfiniteTransition(label = "shake")
    val translateX by shakeAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if (isValid) 0f else 5f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0
                5f at 100
                -5f at 200
                5f at 300
                -5f at 400
                5f at 500
                0f at 600
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake"
    )

    Row(
        modifier = Modifier
            .offset(x = translateX.dp)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) { index ->
            val scale = if (passcode.size == 4 && isValid) 1.1f else 1f
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (index < passcode.size) Color(0xFF007FBA) else Color(0x26007FBA), // Colors.mDark или mDark15
                        shape = MaterialTheme.shapes.medium
                    )
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                if (index < passcode.size) {
                    Text(
                        text = passcode[index],
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PassCodeKeyboard(onPress: (String) -> Unit) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete")
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(keys.size) { index ->
            val key = keys[index]
            if (key.isEmpty()) {
                Spacer(modifier = Modifier.size(64.dp))
            } else {
                Button(
                    onClick = { onPress(key) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x26007FBA), // Colors.mDark15
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    if (key == "delete") {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Delete",
                            tint = Color(0x80FFFFFF) // Colors.light50
                        )
                    } else {
                        Text(
                            text = key,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}