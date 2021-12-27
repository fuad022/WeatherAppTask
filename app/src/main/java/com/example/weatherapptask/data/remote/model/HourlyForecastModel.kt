package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class HourlyForecastModel(
    @SerializedName("hourly")
    val hourly: List<Hourly>
) : Parcelable