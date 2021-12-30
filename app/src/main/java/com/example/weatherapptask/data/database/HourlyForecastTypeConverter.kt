package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyForecastTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun hourlyForecastToString(hourlyForecastModel: HourlyForecastModel): String {
        return gson.toJson(hourlyForecastModel)
    }

    @TypeConverter
    fun stringToHourlyForecast(data: String): HourlyForecastModel {
        val listType = object : TypeToken<HourlyForecastModel>() {}.type
        return gson.fromJson(data, listType)
    }
}