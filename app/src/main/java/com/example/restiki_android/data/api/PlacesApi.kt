package com.example.restiki_android.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

enum class WorktimeType {
    WEEKDAYS, WEEKENDS, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

data class Location(
    val geolat: Double,
    val geolng: Double,
    val city: String? = null,
    val address: String? = null,
    val distance: Float? = null
)

data class Wifi(
    val name: String? = null,
    val password: String? = null
)

data class Stars(
    val star1: Int,
    val star2: Int,
    val star3: Int,
    val star4: Int,
    val star5: Int,
    val total: Int
)

data class Worktime(
    val spotId: Int? = null,
    val type: WorktimeType,
    val start: String,
    val end: String,
    val nowork: Boolean? = null
)

data class Social(
    val type: String,
    val link: String,
    val name: String? = null
)

data class Electricity(
    val spotId: Int,
    val status: Int? = null,
    val share: Int? = null
)

data class SpotItem(
    val id: Int,
    val owner: Int,
    val name: String,
    val description: String? = null,
    val slug: String? = null,
    val logo: String? = null,
    val poster: String? = null,
    val cover: String? = null,
    val lastPost: Int,
    val location: Location,
    val wifi: Wifi? = null,
    val stars: Stars? = null,
    val worktime: List<Worktime>,
    val isVerified: Boolean,
    val isVisible: Boolean,
    val socials: List<Social>,
    val electricity: Electricity? = null
)

data class SpotsResponse(
    val spots: List<SpotItem>
)

//interface PlacesApi {
//    @GET("spots/nearest")
//    suspend fun getNearestSpots(
//        @Query("geolat") geolat: Double = 46.3698536,
//        @Query("geolng") geolng: Double = 30.726982,
//        @Query("radius") radius: Int? = null,
//        @Query("limit") limit: Int? = null,
//        @Query("skip") skip: Int? = null,
//        @Query("stars") stars: Boolean = true,
//        @Query("worktime") worktime: Boolean = false,
//        @Query("electricity") electricity: Boolean = true
//    ): Response<SpotsResponse>
//}


interface PlacesApi {
    @GET("spots/nearest")
    suspend fun getNearestSpots(
        @Query("geolat") geolat: Double = 46.3698536,
        @Query("geolng") geolng: Double = 30.726982,
        @Query("radius") radius: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null,
        @Query("stars") stars: Boolean = true,
        @Query("worktime") worktime: Boolean = false,
        @Query("electricity") electricity: Boolean = true
    ): Response<List<SpotItem>>
}