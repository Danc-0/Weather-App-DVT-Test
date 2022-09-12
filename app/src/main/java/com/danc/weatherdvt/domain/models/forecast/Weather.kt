package com.danc.weatherdvt.domain.models.forecast

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)