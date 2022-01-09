package com.example.weatherapptask.data.database

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapptask.data.remote.model.*
import com.example.weatherapptask.data.remote.other.Constants.Companion.FAVORITE_FORECAST_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = FAVORITE_FORECAST_TABLE)
data class FavoritesEntity(
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