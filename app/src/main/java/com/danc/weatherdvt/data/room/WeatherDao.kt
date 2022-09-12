package com.danc.weatherdvt.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavourite(vararg savedWeatherItem: SavedWeatherItem)

    @Query("SELECT * FROM savedweather ORDER BY savedWeather")
    fun getAllFavouriteWeather(): Flow<List<SavedWeatherItem>>

    @Query("DELETE FROM savedweather")
    suspend fun deleteAll()

}