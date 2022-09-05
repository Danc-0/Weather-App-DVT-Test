package com.danc.weatherdvt.domain.repositories

import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather

interface OpenWeatherRepository {

    suspend fun getCurrentLocationWeather(): CurrentLocationWeather

    suspend fun getCurrentWeatherForecast(): CurrentWeatherForecast

}