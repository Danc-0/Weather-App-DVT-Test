package com.danc.weatherdvt.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danc.weatherdvt.R
import com.danc.weatherdvt.domain.models.weather.Daily
import com.danc.weatherdvt.utils.Utils.Companion.convertTimeToDayFromUnix
import com.danc.weatherdvt.utils.Utils.Companion.readTemp
import kotlinx.android.synthetic.main.fragment_weather_forecast.view.*

class MainFragmentAdapter(var weatherForecast: List<Daily>): RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_weather_forecast, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val forecastItem: Daily = weatherForecast[position]
        holder.itemView.forecast_day.text = convertTimeToDayFromUnix(forecastItem.dt.toLong())
        holder.itemView.temperature.text = readTemp(forecastItem.temp.day)
        when(forecastItem.weather[0].main){
            "Rain" -> {
               holder.itemView.weather_icon.setImageResource(R.drawable.rain)
            }

            "Clouds" -> {
                holder.itemView.weather_icon.setImageResource(R.drawable.clear)
            }

            "Clear" -> {
                holder.itemView.weather_icon.setImageResource(R.drawable.partlysunny)
            }
        }
    }

    override fun getItemCount(): Int {
        return weatherForecast.size
    }
}