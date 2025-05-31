package com.example.restiki_android.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restiki_android.data.api.PlacesApi
import com.example.restiki_android.data.repository.PlacesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            println("Creating PlacesViewModel")
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
            val placesApi = retrofit.create(PlacesApi::class.java)
            println("PlacesApi created")
            val repository = PlacesRepository(placesApi)
            println("PlacesRepository created")
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}