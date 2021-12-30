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
//    ,
//    @SerializedName("feels_like")
//    val feelsLike: Double,
//    @SerializedName("pressure")
//    val pressure: Int,
//    @SerializedName("temp_max")
//    val tempMax: Double,
//    @SerializedName("temp_min")
//    val temp_min: Double
) : Parcelable
