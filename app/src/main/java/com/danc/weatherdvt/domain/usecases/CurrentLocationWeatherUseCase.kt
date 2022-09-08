package com.danc.weatherdvt.domain.usecases

import android.util.Log
import com.danc.weatherdvt.data.services.OpenWeatherService
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import com.danc.weatherdvt.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CurrentLocationWeatherUseCase @Inject constructor(private val openWeatherRepository: OpenWeatherRepository) {

    fun getCurrentWeather(lat: Double, long: Double): Flow<Resource<CurrentLocationWeather>> = flow {
        try {
            emit(Resource.Loading())
            val currentLocationWeather = openWeatherRepository.getCurrentLocationWeather(lat, long)
            emit(Resource.Success(currentLocationWeather))
        } catch (exception: HttpException){
            emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred"))
        } catch (exception: IOException){
            emit(Resource.Error("We could not reach the service. Check your internet connection and try again"))
        }
    }
}