package com.danc.weatherdvt

import android.app.Application
import com.danc.weatherdvt.data.room.WeatherDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class Application: Application() {


    private val applicationScope = CoroutineScope(SupervisorJob())
//    val database by lazy { WeatherDatabase.getWeatherDatabase(this, applicationScope) }

}