package com.danc.weatherdvt.domain.repositories

import androidx.annotation.WorkerThread
import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import kotlinx.coroutines.flow.Flow

interface OpenWeatherRepository {

    suspend fun getCurrentLocationWeather(lat: Double, long: Double): CurrentLocationWeather

    suspend fun getCurrentWeatherForecast(lat: Double, long: Double, cnt: Int): CurrentWeatherForecast

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addToFavourites(savedWeatherItem: SavedWeatherItem)

    suspend fun getAllSavedWeather(): Flow<List<SavedWeatherItem>>

}