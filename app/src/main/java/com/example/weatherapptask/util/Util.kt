package com.example.weatherapptask.util

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import com.example.weatherapptask.R
import java.text.SimpleDateFormat
import java.util.*

var toast: Toast? = null

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

    fun getCityName(lat: Double, long: Double, context: Context): String {
        val geoCoder = Geocoder(context, Locale.US)
        val address = geoCoder.getFromLocation(lat, long, 1)
        return address.get(0).locality
    }

    fun getCountryName(lat: Double, long: Double, context: Context): String {
        val geoCoder = Geocoder(context, Locale.US)
        val address = geoCoder.getFromLocation(lat, long, 1)
        return address.get(0).countryName
    }

    fun displayToast(response: String, context: Context) {
        if (toast != null) toast!!.cancel()
        toast = Toast.makeText(
            context,
            response,
            Toast.LENGTH_SHORT
        )
        toast?.show()
    }
}