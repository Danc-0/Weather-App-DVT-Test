package com.danc.weatherdvt.domain.models.weather

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val current: Current,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)