package com.example.restiki_android.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Composable
fun CodeScreen(
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current.dataStore)
    ),
    onNavigateToPlaces: () -> Unit
) {
    println("Rendering CodeScreen") // Для отладки
    val state by viewModel.authState.collectAsState()
    var code by rememberSaveable { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(state.error, showError) {
        if (state.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.error)
        } else if (showError) {
            snackbarHostState.showSnackbar("Невірний код")
        }
    }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onNavigateToPlaces() // Переход на PlacesScreen только после успешной авторизации
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070707))
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.take(4) }, // Ограничиваем до 4 символов
                label = { Text("Введіть код", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (code.length == 4) {
                            viewModel.verifyCode(code)
                            keyboardController?.hide()
                        } else {
                            showError = true
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (code.length == 4) {
                        viewModel.verifyCode(code)
                        keyboardController?.hide()
                    } else {
                        showError = true
                    }
                },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (code.length == 4) Color(0xFFC1272D) else Color(0x26C1272D),
                    contentColor = if (code.length == 4) Color.White else Color(0x80FFFFFF)
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = if (state.isLoading) "Секунду..." else "Продовжити",
                    fontSize = 16.sp
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}