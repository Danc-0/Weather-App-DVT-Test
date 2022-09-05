package com.danc.weatherdvt.domain.models.forecast

data class CurrentWeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherItem>,
    val message: Int
)