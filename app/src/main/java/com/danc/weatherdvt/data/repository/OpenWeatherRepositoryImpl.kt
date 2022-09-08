package com.danc.weatherdvt.data.repository

import com.danc.weatherdvt.data.services.OpenWeatherService
import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(private val service: OpenWeatherService): OpenWeatherRepository {

    override suspend fun getCurrentLocationWeather(lat: Double, long: Double): CurrentLocationWeather {
      return service.getCurrentLocationWeather(lat, long)
    }

    override suspend fun getCurrentWeatherForecast(lat: Double, long: Double): CurrentWeatherForecast {
       return service.getCurrentWeatherForecast(lat, long)
    }
}