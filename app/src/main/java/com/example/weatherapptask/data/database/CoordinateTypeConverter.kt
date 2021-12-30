package com.example.weatherapptask.data.database

import androidx.room.TypeConverter
import com.example.weatherapptask.data.remote.model.Coord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CoordinateTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun coordinateToString(coordinate: Coord): String {
        return gson.toJson(coordinate)
    }

    @TypeConverter
    fun stringToCoordinate(data: String): Coord {
        val listType = object : TypeToken<Coord>() {}.type
        return gson.fromJson(data, listType)
    }
}