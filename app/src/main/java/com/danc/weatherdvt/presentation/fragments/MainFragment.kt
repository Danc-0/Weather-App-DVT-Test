package com.danc.weatherdvt.presentation.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.danc.weatherdvt.R
import com.danc.weatherdvt.domain.models.coordinates.LocationCoords
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.presentation.MainActivity
import com.danc.weatherdvt.utils.Utils.Companion.tempInCelsius
import com.danc.weatherdvt.presentation.adapter.MainFragmentAdapter
import com.danc.weatherdvt.presentation.viewmodels.MainFragViewModel
import com.danc.weatherdvt.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
@FragmentScoped
class MainFragment : Fragment(R.layout.fragment_main), CustomDialog.OnClick {

    private val TAG = "MainFragment"
    private val viewmodel: MainFragViewModel by viewModels()
    private var mainFragmentAdapter: MainFragmentAdapter? = null
    private lateinit var currentLocationWeather: CurrentLocationWeather
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentLocation(requireContext())

    }

    private fun getCurrentLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.supportFragmentManager?.let {
                CustomDialog("Enable Location", "Proceed","Your Locations Settings is required'.\nPlease Enable Location to " + "use this app", this).show(
                    it, "CustomDialog")
            }
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                        Log.d(TAG, "getCurrentLocation: $location")
                    currentWeather(location.latitude, location.longitude)
                    currentWeatherForecast(location.latitude, location.longitude, 16)
                }
            }
    }

    private fun currentWeather(latitude: Double, longitude: Double) {
        lifecycleScope.launchWhenCreated {
            viewmodel.currentWeather(latitude,  longitude)
                .collect() {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            currentLocationWeather = it.data!!
                            main_temp.text = tempInCelsius(it.data.main.temp)
                            main_description.text = it.data.weather[0].description
                            min_temperature.text = tempInCelsius(it.data.main.temp_min)
                            current_temperature.text = tempInCelsius(it.data.main.temp)
                            max_temperature.text = tempInCelsius(it.data.main.temp_max)
                            city_name.text = it.data.name
                            addToFavourites(currentLocationWeather)
                            when(it.data.weather[0].main) {
                                "Clouds" -> {
                                    main_image.setImageResource(R.drawable.sea_cloudy)
                                    main_fragment.setBackgroundColor(resources.getColor(R.color.cloudy))
                                    rv_weather_forecast.setBackgroundColor(resources.getColor(R.color.cloudy))
                                }

                                "Rain" -> {
                                    main_image.setImageResource(R.drawable.sea_rainy)
                                    main_fragment.setBackgroundColor(resources.getColor(R.color.rainy))
                                    rv_weather_forecast.setBackgroundColor(resources.getColor(R.color.rainy))
                                }

                                "Sun" -> {
                                    main_image.setImageResource(R.drawable.sea_sunnypng)
                                    main_fragment.setBackgroundColor(resources.getColor(R.color.sunny))
                                    rv_weather_forecast.setBackgroundColor(resources.getColor(R.color.sunny))
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun currentWeatherForecast(latitude: Double, longitude: Double, cnt: Int) {
        lifecycleScope.launchWhenCreated {
            viewmodel.currentForecast(latitude,  longitude,cnt)
                .collect() {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            mainFragmentAdapter = it.data?.let { item -> MainFragmentAdapter(item.list) }
                            rv_weather_forecast.apply {
                                layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false)
                                setHasFixedSize(true)
                                adapter = mainFragmentAdapter
                            }

                        }
                    }
                }
        }
    }

    override fun dialogContinue() {
        val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(myIntent)
    }

    private fun addToFavourites(currentLocationWeather: CurrentLocationWeather) {
        val savedWeatherItem = SavedWeatherItem(
            currentLocationWeather.timezone,
            currentLocationWeather.name,
            currentLocationWeather.sys.country,
            currentLocationWeather.coord.lat,
            currentLocationWeather.coord.lon,
            currentLocationWeather.main.humidity,
            currentLocationWeather.main.pressure,
            currentLocationWeather.main.sea_level,
            currentLocationWeather.main.temp,
            currentLocationWeather.main.temp_max,
            currentLocationWeather.main.temp_min,
        )
        viewmodel.addFavourite(savedWeatherItem)
    }

}