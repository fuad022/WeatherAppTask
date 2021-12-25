package com.example.weatherapptask.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Temp(
    val day: Double
) : Parcelable