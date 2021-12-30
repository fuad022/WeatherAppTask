package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.Main
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TemperatureTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun temperatureToString(temperature: Main): String {
        return gson.toJson(temperature)
    }

    @TypeConverter
    fun stringToTemperature(data: String): Main {
        val listType = object : TypeToken<Main>() {}.type
        return gson.fromJson(data, listType)
    }
}