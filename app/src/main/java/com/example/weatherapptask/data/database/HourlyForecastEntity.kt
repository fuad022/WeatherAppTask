package com.example.weatherapptask.data.database

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.example.weatherapptask.data.remote.other.Constants.Companion.HOURLY_FORECAST_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = HOURLY_FORECAST_TABLE)
class HourlyForecastEntity(
    @TypeConverters(HourlyForecastTypeConverter::class)
    var hourlyForecastModel: HourlyForecastModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}

//@Keep
//@Parcelize
//@Entity(tableName = HOURLY_FORECAST_TABLE)
//data class HourlyForecastEntity(
//    @PrimaryKey(autoGenerate = false)
//    var id: Int = 0,
//    @TypeConverters(HourlyForecastTypeConverter::class)
//    var hourlyForecastModel: HourlyForecastModel
//) : Parcelable