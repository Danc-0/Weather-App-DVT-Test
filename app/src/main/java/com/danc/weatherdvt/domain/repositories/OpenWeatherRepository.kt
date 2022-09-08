package com.danc.weatherdvt.domain.repositories

import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather

interface OpenWeatherRepository {

    suspend fun getCurrentLocationWeather(lat: Double, long: Double): CurrentLocationWeather

    suspend fun getCurrentWeatherForecast(lat: Double, long: Double): CurrentWeatherForecast

}