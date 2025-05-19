package com.example.restiki_android.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Composable
fun PhoneScreen(
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current.dataStore)
    ),
    onNavigateToCode: () -> Unit // Исправлено с maga на onNavigateToCode
) {
    val state by viewModel.authState.collectAsState()
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var isNumberComplete by rememberSaveable { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    fun formatPhoneNumber(text: String): String {
        val cleaned = text.replace("[^0-9]".toRegex(), "").take(9)
        var formatted = ""
        if (cleaned.isNotEmpty()) formatted += cleaned.substring(0, minOf(cleaned.length, 2))
        if (cleaned.length > 2) formatted += " ${cleaned.substring(2, minOf(cleaned.length, 5))}"
        if (cleaned.length > 5) formatted += " ${cleaned.substring(5, minOf(cleaned.length, 7))}"
        if (cleaned.length > 7) formatted += " ${cleaned.substring(7, cleaned.length)}"
        isNumberComplete = cleaned.length == 9
        return formatted
    }

    LaunchedEffect(state.error, showError) {
        if (state.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.error)
        } else if (showError) {
            snackbarHostState.showSnackbar("Номер неповний або неправильний")
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToCode()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C2526))
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x1AFFFFFF), shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+380",
                    color = Color(0xFFFFFFFF),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = formatPhoneNumber(it) },
                    placeholder = { Text("96 123 45 67", color = Color(0x80FFFFFF)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFFFFFFF),
                        unfocusedTextColor = Color(0xFFFFFFFF),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isNumberComplete) {
                                viewModel.requestCode("+380${phoneNumber.replace(" ", "")}")
                                keyboardController?.hide()
                            } else {
                                showError = true
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "Введіть свій номер телефону. На нього прийде 4-х значний код для підтвердження входу у додаток.",
                color = Color(0xD9FFFFFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
                .padding(bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Натискаючи кнопку \"Продовжити\", ви погоджуєтеся із",
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = "збором та обробкою персональних даних",
                color = Color(0xFF007FBA),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cityfood.com.ua/privacy_policy.pdf"))
                    context.startActivity(intent)
                }
            )

            Button(
                onClick = {
                    if (isNumberComplete) {
                        viewModel.requestCode("+380${phoneNumber.replace(" ", "")}")
                        keyboardController?.hide()
                    } else {
                        showError = true
                    }
                },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNumberComplete) Color(0xFF007FBA) else Color(0x26007FBA),
                    contentColor = if (isNumberComplete) Color(0xFFFFFFFF) else Color(0x80FFFFFF)
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .height(48.dp)
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