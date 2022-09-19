package com.danc.weatherdvt.data.repository

import android.util.Log
import com.danc.weatherdvt.data.room.WeatherDao
import com.danc.weatherdvt.data.services.OpenWeatherService
import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.domain.models.weather.CurrentWeather
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(private val service: OpenWeatherService, private val weatherDao: WeatherDao): OpenWeatherRepository {

    override suspend fun getCurrentLocationWeather(lat: Double, long: Double): CurrentLocationWeather {
      return service.getCurrentLocationWeather(lat, long)
    }

    override suspend fun getCurrentWeatherForecast(lat: Double, long: Double): CurrentWeather {
       return service.getWeatherData(lat, long)
    }

    override suspend fun addToFavourites(savedWeatherItem: SavedWeatherItem) {
        return weatherDao.addFavourite(savedWeatherItem)
    }

    override suspend fun getAllSavedWeather(): Flow<List<SavedWeatherItem>> {
        return weatherDao.getAllFavouriteWeather()
    }
}