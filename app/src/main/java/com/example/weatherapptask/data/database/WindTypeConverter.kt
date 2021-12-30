package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WindTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun windToString(wind: Wind): String {
        return gson.toJson(wind)
    }

    @TypeConverter
    fun stringToWind(data: String): Wind {
        val listType = object : TypeToken<Wind>() {}.type
        return gson.fromJson(data, listType)
    }
}