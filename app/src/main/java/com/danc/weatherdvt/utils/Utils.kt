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

    companion object{

        fun tempInCelsius(value: Double?): String {
            return if (value != null) {
                "${(value - 273.15).roundToInt()}℃"
            } else {
                "$0℃"
            }
        }

        fun getShortDate(ts:Long?):String{
            if(ts == null) return ""
            //Get instance of calendar
            val calendar = Calendar.getInstance(Locale.getDefault())
            //get current date from ts
            calendar.timeInMillis = ts
            //return formatted date
            return android.text.format.DateFormat.format("E, dd MMM yyyy", calendar).toString()
        }

        fun stringtoDate(dates: String): Date {
            val sdf = SimpleDateFormat("EEE, MMM dd yyyy",
                Locale.ENGLISH)
            var date: Date? = null
            try {
                date = sdf.parse(dates)
                println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date!!
        }
    }

}