package com.danc.weatherdvt.data.services

import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.domain.models.weather.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("data/2.5/weather")
    suspend fun getCurrentLocationWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ) : CurrentLocationWeather

    @GET("/data/2.5/forecast")
    suspend fun getCurrentWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") cnt: Int
    ): CurrentWeatherForecast

    @GET("data/2.5/onecall?exclude=minutely,hourly,alerts&units=metric")
    suspend fun getWeatherData(
        @Query(value = "lat") latitude: Double,
        @Query(value = "lon") longitude: Double
    ): CurrentWeather

}