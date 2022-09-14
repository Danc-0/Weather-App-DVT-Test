package com.danc.weatherdvt.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.danc.weatherdvt.R
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import com.danc.weatherdvt.domain.models.weather.CurrentLocationWeather
import com.danc.weatherdvt.presentation.adapter.MainFragmentAdapter
import com.danc.weatherdvt.presentation.viewmodels.MainFragViewModel
import com.danc.weatherdvt.utils.Resource
import com.danc.weatherdvt.utils.Utils.Companion.tempInCelsius
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.concurrent.TimeUnit
import kotlin.math.log


@AndroidEntryPoint
@FragmentScoped
class MainFragment : Fragment(R.layout.fragment_main), CustomDialog.OnClick {

    private val TAG = "MainFragment"
    private val viewmodel: MainFragViewModel by viewModels()
    private var mainFragmentAdapter: MainFragmentAdapter? = null
    private lateinit var currentLocationWeather: CurrentLocationWeather
    private val PERMISSION_ID = 102
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        /**
         *  Check If Location permission is allowed
         *  IF YES check if Location Access is enabled
         *  IF NO Request for Location Permission
         *  IF YES get the Current Location of the User
         *  Then Get the required user Weather and Forecast
         *  IF NO Request for Location access to be enabled
         *  */
        checkLocationPermissions()

    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_ID
            )
            false
        } else {
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)){
                checkLocationIsEnabled()
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkLocationPermissions()
        }

        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentWeather(location.latitude, location.longitude)
                    currentWeatherForecast(location.latitude, location.longitude, 16)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkLocationIsEnabled() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
        }

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder?.build())
        task.addOnSuccessListener {
            getCurrentLocation(requireContext())
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
               locationAccessDialog()
            }
        }
    }
    
    private fun currentWeather(latitude: Double, longitude: Double) {
        lifecycleScope.launchWhenCreated {
            viewmodel.currentWeather(latitude, longitude)
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
                            when (it.data.weather[0].main) {
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
            viewmodel.currentForecast(latitude, longitude, cnt)
                .collect() {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            mainFragmentAdapter =
                                it.data?.let { item -> MainFragmentAdapter(item.list) }
                            rv_weather_forecast.apply {
                                layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                setHasFixedSize(true)
                                adapter = mainFragmentAdapter
                            }

                        }
                    }
                }
        }
    }

    private fun locationAccessDialog(){
        activity?.supportFragmentManager?.let {
            CustomDialog(
                "Enable Location",
                "Proceed",
                "Your Locations Settings is required'.\nPlease Enable Location to " + "use this app",
                this
            ).show(
                it, "CustomDialog"
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(requireContext())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted()) {
            getCurrentLocation(requireContext())
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