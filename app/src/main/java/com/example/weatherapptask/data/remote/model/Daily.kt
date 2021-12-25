package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Daily(
    val dt: Int,
    val temp: Temp,
    val weather: List<Weather>
) : Parcelable