package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("humidity")
    val humidity: Int
) : Parcelable
