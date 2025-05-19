package com.example.restiki_android.ui.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restiki_android.data.api.AuthApi
import com.example.restiki_android.data.repository.AuthRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModelFactory(
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://cityfood.com.ua/api/") // Убедись, что URL верный
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val authApi = retrofit.create(AuthApi::class.java)
            val repository = AuthRepository(authApi, dataStore)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}