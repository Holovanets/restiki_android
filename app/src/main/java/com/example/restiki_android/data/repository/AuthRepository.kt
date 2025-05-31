package com.example.restiki_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.restiki_android.data.api.AuthApi
import com.example.restiki_android.data.api.CodeRequest
import com.example.restiki_android.data.api.RequestCodeRequest
import com.example.restiki_android.data.api.RequestCodeResponse
import com.example.restiki_android.data.api.VerifyResponse
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun requestCode(phone: String): Result<RequestCodeResponse> {
        println("requestCode: phone=$phone")
        return try {
            val response = authApi.requestCode(RequestCodeRequest(phone))
            println("requestCode: HTTP response code=${response.code()}, message=${response.message()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Відсутня відповідь сервера"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                println("requestCode: Failed with code=${response.code()}, error=$errorBody")
                Result.failure(Exception("Помилка запиту: ${response.message()} (code=${response.code()})"))
            }
        } catch (e: Exception) {
            println("requestCode: Exception occurred - ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun verifyCode(phone: String, code: String): Result<VerifyResponse> {
        println("verifyCode: phone=$phone, code=$code")
        return try {
            val response = authApi.verifyCode(CodeRequest(phone, code))
            println("verifyCode: HTTP response code=${response.code()}, message=${response.message()}")
            if (response.isSuccessful) {
                response.body()?.let {
                    println("verifyCode: Response body=$it")
                    if (it.nameNeeded) {
                        Result.failure(Exception("Потрібно ввести ім'я (функція відключена)"))
                    } else if (it.tokens != null) {
                        dataStore.edit { prefs ->
                            prefs[stringPreferencesKey("auth_token")] = it.tokens.auth_token
                            prefs[stringPreferencesKey("access_token")] = it.tokens.access_token
                            prefs[stringPreferencesKey("refresh_token")] = it.tokens.refresh_token
                            println("verifyCode: Tokens saved - auth_token=${it.tokens.auth_token}, access_token=${it.tokens.access_token}, refresh_token=${it.tokens.refresh_token}")
                        }
                        Result.success(it)
                    } else {
                        Result.failure(Exception("Токени відсутні"))
                    }
                } ?: Result.failure(Exception("Відсутня відповідь сервера"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                println("verifyCode: Failed with code=${response.code()}, error=$errorBody")
                Result.failure(Exception("Невірний код: ${response.message()} (code=${response.code()})"))
            }
        } catch (e: Exception) {
            println("verifyCode: Exception occurred - ${e.message}")
            Result.failure(e)
        }
    }
}