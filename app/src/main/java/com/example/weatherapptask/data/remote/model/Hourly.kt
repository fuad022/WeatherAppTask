package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hourly(
    val dt: Int,
    val temp: Double,
    val weather: List<Weather>
) : Parcelable