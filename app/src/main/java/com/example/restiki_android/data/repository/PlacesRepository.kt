package com.example.restiki_android.data.repository

import com.example.restiki_android.data.api.PlacesApi
import com.example.restiki_android.data.api.SpotItem

class PlacesRepository(
    private val placesApi: PlacesApi
) {
    suspend fun getNearestSpots(
        geolat: Double = 46.440576142941445,
        geolng: Double = 30.723982473461987,
        radius: Int? = 2000,
        limit: Int? = 10,
        skip: Int? = 0,
        stars: Boolean = true,
        worktime: Boolean = false,
        electricity: Boolean = true
    ): Result<List<SpotItem>> {
        println("getNearestSpots: geolat=$geolat, geolng=$geolng, radius=$radius, limit=$limit, skip=$skip, stars=$stars, worktime=$worktime, electricity=$electricity")
        return try {
            val response = placesApi.getNearestSpots(geolat, geolng, radius, limit, skip, stars, worktime, electricity)
            println("getNearestSpots: HTTP response code=${response.code()}, message=${response.message()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Відсутня відповідь сервера"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                println("getNearestSpots: Failed with code=${response.code()}, error=$errorBody")
                Result.failure(Exception("Помилка запиту: ${response.message()} (code=${response.code()})"))
            }
        } catch (e: Exception) {
            println("getNearestSpots: Exception occurred - ${e.message}")
            Result.failure(e)
        }
    }
}