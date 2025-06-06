package com.example.restiki_android.ui.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restiki_android.data.api.AuthApi
import com.example.restiki_android.data.repository.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModelFactory(
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            println("Creating AuthViewModel")
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://cityfood.com.ua/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            println("Retrofit created")
            val authApi = retrofit.create(AuthApi::class.java)
            println("AuthApi created")
            val repository = AuthRepository(authApi, dataStore)
            println("AuthRepository created")
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}