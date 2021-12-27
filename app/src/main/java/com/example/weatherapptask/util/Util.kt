package com.example.weatherapptask.util

import android.annotation.SuppressLint
import com.example.weatherapptask.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object Util {
    fun getWeatherAnimation(weather: String): Int {
        return when (weather) {
            "01d" -> R.raw.clear_sky_day
            "01n" -> R.raw.clear_sky_night
            "02d" -> R.raw.few_clouds_day
            "02n" -> R.raw.few_clouds_night
            "03d" -> R.raw.scattered_clouds_day
            "03n" -> R.raw.scattered_clouds_night
            "04d" -> R.raw.broken_clouds_day
            "04n" -> R.raw.broken_clouds_night
            "09d" -> R.raw.shower_rain_day
            "09n" -> R.raw.shower_rain_night
            "10d" -> R.raw.rain_day
            "10n" -> R.raw.rain_night
            "11d" -> R.raw.thunderstorm_day
            "11n" -> R.raw.thunderstorm_night
            "13d" -> R.raw.snow_day
            "13n" -> R.raw.snow_night
            "50d" -> R.raw.mist_day
            "50n" -> R.raw.mist_night
            else -> 0
        }
    }

    fun getWholeNum(d: Double): String {
        val str = d.toString()
        return when (d) {
            in -0.9..0.0 -> str.substring(str.indexOf("-") + 1, str.indexOf("."))
            else -> str.substringBefore('.')
        }
    }

    fun convertDate(s: String, dp: String): String? {
        val pattern = when (dp) {
            "1" -> "MMMM dd, yyyy"
            "2" -> "HH:mm"
            "3" -> "dd.MM"
            "4" -> "MMMM dd"
            else -> null
        }

        return try {
            val sdf = SimpleDateFormat(pattern, Locale.US)
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getWeekDayName(s: String): String {
        val sdf = SimpleDateFormat("EEEE", Locale.US)
        val d = Date(s.toLong() * 1000)
        return sdf.format(d)
    }

}