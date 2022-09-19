package com.danc.weatherdvt.domain.models.weather

data class Current(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_speed: Double,
    val feels_like: Double,
    val humidity: Int,
)