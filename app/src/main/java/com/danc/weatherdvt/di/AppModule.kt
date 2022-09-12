package com.danc.weatherdvt.di

import android.content.Context
import androidx.room.Room
import com.danc.weatherdvt.BuildConfig
import com.danc.weatherdvt.data.repository.OpenWeatherRepositoryImpl
import com.danc.weatherdvt.data.room.WeatherDao
import com.danc.weatherdvt.data.room.WeatherDatabase
import com.danc.weatherdvt.data.services.OpenWeatherService
import com.danc.weatherdvt.domain.repositories.OpenWeatherRepository
import com.danc.weatherdvt.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var apiKey: String = BuildConfig.API_KEY

    private val api_interceptor = Interceptor {
        val originalRequest = it.request()
        val newHttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("appid", apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()
        it.proceed(newRequest)
    }

    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(api_interceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideWeatherAPI(): OpenWeatherService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherRepository(service: OpenWeatherService, weatherDao: WeatherDao): OpenWeatherRepository {
        return OpenWeatherRepositoryImpl(service, weatherDao)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(appDatabase: WeatherDatabase): WeatherDao {
        return appDatabase.weatherDao()
    }
}