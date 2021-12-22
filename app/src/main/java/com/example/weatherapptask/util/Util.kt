package com.example.weatherapptask.util

import com.example.weatherapptask.R

object Util {
    fun getWeatherAnimation(weather: String): Int {
        val result = when (weather) {
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
        return result
    }
}