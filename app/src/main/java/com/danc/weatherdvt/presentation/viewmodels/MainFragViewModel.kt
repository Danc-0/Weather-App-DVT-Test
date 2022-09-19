package com.danc.weatherdvt.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import com.danc.weatherdvt.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragViewModel @Inject constructor(private val openWeatherRepository: OpenWeatherRepository) :
    ViewModel() {

    fun currentWeather(lat: Double, long: Double) = flow {
        emit(Resource.Loading())
        try {
            val data = openWeatherRepository.getCurrentLocationWeather(lat, long)
            emit(Resource.Success(data))
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun currentForecast(lat: Double, long: Double) = flow {
        emit(Resource.Loading())
        try {
            val data = openWeatherRepository.getCurrentWeatherForecast(lat, long)
            emit(Resource.Success(data))
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun addFavourite(savedWeatherItem: SavedWeatherItem) = viewModelScope.launch {
        openWeatherRepository.addToFavourites(savedWeatherItem)
    }

    fun getSavedLocations() = flow {
        emit(openWeatherRepository.getAllSavedWeather())
    }.flowOn(Dispatchers.Default)

}