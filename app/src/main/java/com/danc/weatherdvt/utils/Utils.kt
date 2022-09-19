package com.danc.weatherdvt.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Utils {

    companion object {

        fun readTemp(value: Double?): String {
            return if (value != null) {
                "${(value).roundToInt()}℃"
            } else {
                "$0℃"
            }
        }

        fun tempInCelsius(value: Double?): String {
            return if (value != null) {
                "${(value - 273.15).roundToInt()}℃"
            } else {
                "$0℃"
            }
        }

        fun convertTimeToDayFromUnix(time: Long): String {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            val date = Date(time * 1000)
            return sdf.format(date)
        }

    }

}