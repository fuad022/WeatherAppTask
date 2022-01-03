package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Weather(
    @SerializedName("main")
    val currentWeather: String,
    @SerializedName("icon")
    val icon: String
) : Parcelable