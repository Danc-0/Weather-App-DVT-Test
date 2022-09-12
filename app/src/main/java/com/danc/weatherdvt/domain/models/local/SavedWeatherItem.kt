package com.danc.weatherdvt.domain.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedweather")
class SavedWeatherItem(
    @PrimaryKey
    @ColumnInfo(name = "savedWeather")
    val timezone: Int,
    @ColumnInfo(name = "locationName")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "latitude")
    val lat: Double,
    @ColumnInfo(name = "longitude")
    val lon: Double,
    @ColumnInfo(name = "humidity")
    val humidity: Int,
    @ColumnInfo(name = "pressure")
    val pressure: Int,
    @ColumnInfo(name = "seaLevel")
    val sea_level: Int,
    @ColumnInfo(name = "temperature")
    val temp: Double,
    @ColumnInfo(name = "maximumTemp")
    val temp_max: Double,
    @ColumnInfo(name = "minimumTemp")
    val temp_min: Double

)