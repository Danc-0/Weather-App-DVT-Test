package com.danc.weatherdvt.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import com.danc.weatherdvt.domain.usecases.CurrentLocationWeatherUseCase
import com.danc.weatherdvt.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainFragViewModel @Inject constructor(private val openWeatherRepository: OpenWeatherRepository): ViewModel() {

    fun currentWeather(lat: Double, long: Double) = flow {
        emit(Resource.Loading())
        try {
            val data = openWeatherRepository.getCurrentLocationWeather(lat, long)
            Log.d("TAG", "currentWeather: $data")
            emit(Resource.Success(data))
        } catch (exception: Exception){
            exception.printStackTrace()
        }
    }

}