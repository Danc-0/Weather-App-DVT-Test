package com.danc.weatherdvt.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.danc.weatherdvt.R
import com.danc.weatherdvt.presentation.viewmodels.MainFragViewModel
import com.danc.weatherdvt.utils.Resource
import com.danc.weatherdvt.utils.State
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@FragmentScoped
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewmodel: MainFragViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewmodel.currentWeather(44.34,  10.99)
                .collect() {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                           when(it.data?.weather?.get(0)?.main) {
                               "Clouds" -> {
                                   main_fragment.setBackgroundColor(resources.getColor(R.color.cloudy))
                               }
                           }
                        }
                    }
                }
        }

    }

    companion object {

    }
}