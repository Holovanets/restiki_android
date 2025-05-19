package com.example.restiki_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.restiki_android.data.api.AuthApi
import com.example.restiki_android.data.api.CodeRequest
import com.example.restiki_android.data.api.PhoneRequest
import com.example.restiki_android.data.api.RequestCodeResponse
import com.example.restiki_android.data.api.VerifyResponse
//import com.example.restiki_android.data.model.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun requestCode(phone: String): Result<RequestCodeResponse> {
        return try {
            val response = authApi.requestCode(PhoneRequest(phone))
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.error?.blocked == true) {
                        Result.failure(Exception("Заблоковано: зачекайте ${it.error.expired} секунд"))
                    } else {
                        Result.success(it)
                    }
                } ?: Result.failure(Exception("Відсутня відповідь сервера"))
            } else {
                Result.failure(Exception("Не вдалося отримати код"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyCode(phone: String, code: String): Result<VerifyResponse> {
        return try {
            val response = authApi.verifyCode(CodeRequest(phone, code))
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.nameNeeded) {
                        Result.failure(Exception("Потрібно ввести ім'я (функція відключена)"))
                    } else if (it.tokens != null) {
                        dataStore.edit { prefs ->
                            prefs[stringPreferencesKey("auth_token")] = it.tokens.auth_token
                            prefs[stringPreferencesKey("access_token")] = it.tokens.access_token
                            prefs[stringPreferencesKey("refresh_token")] = it.tokens.refresh_token
                        }
                        Result.success(it)
                    } else {
                        Result.failure(Exception("Токени відсутні"))
                    }
                } ?: Result.failure(Exception("Відсутня відповідь сервера"))
            } else {
                Result.failure(Exception("Невірний код"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}