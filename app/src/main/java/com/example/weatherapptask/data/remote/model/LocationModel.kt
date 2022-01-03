package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapptask.data.database.CoordinateTypeConverter
import com.example.weatherapptask.data.database.TemperatureTypeConverter
import com.example.weatherapptask.data.database.WeatherListTypeConverter
import com.example.weatherapptask.data.database.WindTypeConverter
import com.example.weatherapptask.data.remote.other.Constants.Companion.LOCATION_FORECAST_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = LOCATION_FORECAST_TABLE)
data class LocationModel(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("coord")
    @TypeConverters(CoordinateTypeConverter::class)
    val cityCoordinate: Coord,
    @SerializedName("dt")
    val dateTime: Int,
    @SerializedName("main")
    @TypeConverters(TemperatureTypeConverter::class)
    val temperatureInfo: Main,
    @SerializedName("name")
    val cityName: String,
    @SerializedName("weather")
    @TypeConverters(WeatherListTypeConverter::class)
    val weather: List<Weather>,
    @SerializedName("wind")
    @TypeConverters(WindTypeConverter::class)
    val wind: Wind
) : Parcelable