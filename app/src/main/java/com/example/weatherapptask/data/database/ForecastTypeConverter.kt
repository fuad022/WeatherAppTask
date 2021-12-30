package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.LocationModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ForecastTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun locationForecastToString(locationModel: LocationModel): String {
        return gson.toJson(locationModel)
    }

    @TypeConverter
    fun stringToLocationForecast(data: String): LocationModel {
        val listType = object : TypeToken<LocationModel>() {}.type
        return gson.fromJson(data, listType)
    }
}
