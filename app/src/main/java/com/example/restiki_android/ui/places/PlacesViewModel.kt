package com.example.restiki_android.ui.places

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiki_android.data.api.SpotItem
import com.example.restiki_android.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repository: PlacesRepository
) : ViewModel() {
    private val _spots = mutableStateListOf<SpotItem>()
    val spots: SnapshotStateList<SpotItem> = _spots

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun fetchNearestSpots(
        geolat: Double = 46.440576142941445,
        geolng: Double = 30.723982473461987,
        radius: Int? = 3000,
        limit: Int? = 50,
        skip: Int? = 0,
        stars: Boolean = true,
        worktime: Boolean = false,
        electricity: Boolean = true
    ) {
        println("PlacesViewModel: fetchNearestSpots called with geolat=$geolat, geolng=$geolng")
        viewModelScope.launch {
            _isLoading.value = true
            repository.getNearestSpots(geolat, geolng, radius, limit, skip, stars, worktime, electricity).onSuccess { fetchedSpots ->
                _spots.clear()
                _spots.addAll(fetchedSpots)
                println("Spots size after fetch: ${_spots.size}") // Отладка
                _isLoading.value = false
            }.onFailure {
                println("PlacesViewModel: Failed to fetch spots - ${it.message}")
                _isLoading.value = false
            }
        }
    }
}