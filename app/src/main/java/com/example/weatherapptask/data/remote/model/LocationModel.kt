package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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