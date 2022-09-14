package com.danc.weatherdvt.utils

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Utils {

    companion object {

        fun tempInCelsius(value: Double?): String {
            return if (value != null) {
                "${(value - 273.15).roundToInt()}℃"
            } else {
                "$0℃"
            }
        }
    }

}