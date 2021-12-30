package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherListTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun weatherListToString(weatherList: List<Weather>): String {
        return gson.toJson(weatherList)
    }

    @TypeConverter
    fun stringToWeatherList(data: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(data, listType)
    }
}