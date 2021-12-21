package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/*
val jsonArray= JSONArray(listOf(resultModel.membersImageList))
            jsonArray.getJSONObject(0)

            val a = it.membersImageModel?.memberImg

            LottieAnimationView
 */

@Parcelize
data class LocationModel(
    val id: Int,
    @SerializedName("coord")
    val cityCoordinate: Coord,
    @SerializedName("dt")
    val dateTime: Int,
    @SerializedName("main")
    val temperatureInfo: Main,
    @SerializedName("name")
    val cityName: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind,
    val base: String,
    val clouds: Clouds,
    val cod: Int
) : Parcelable

@Parcelize
data class Clouds(
    val all: Int
) : Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
) : Parcelable

@Parcelize
data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
) : Parcelable

@Parcelize
data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
) : Parcelable

@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    @SerializedName("main")
    val currentWeather: String
) : Parcelable

@Parcelize
data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
) : Parcelable