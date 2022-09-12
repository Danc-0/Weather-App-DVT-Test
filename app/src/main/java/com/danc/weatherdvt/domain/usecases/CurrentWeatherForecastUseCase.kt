package com.danc.weatherdvt.domain.usecases

import com.danc.weatherdvt.data.services.OpenWeatherService
import com.danc.weatherdvt.domain.models.forecast.CurrentWeatherForecast
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import com.danc.weatherdvt.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class CurrentWeatherForecastUseCase(private val openWeatherRepository: OpenWeatherRepository) {

    operator fun invoke(): Flow<Resource<CurrentWeatherForecast>> = flow {
        try {
            emit(Resource.Loading())
            val currentLocationWeather = openWeatherRepository.getCurrentWeatherForecast(44.34, 10.99, 5)
            emit(Resource.Success(currentLocationWeather))
        } catch (exception: HttpException){
            emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred"))
        } catch (exception: IOException){
            emit(Resource.Error("We could not reach the service. Check your internet connection and try again"))
        }
    }

}