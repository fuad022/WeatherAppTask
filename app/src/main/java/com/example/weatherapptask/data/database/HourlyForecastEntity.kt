package com.example.weatherapptask.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.example.weatherapptask.data.remote.other.Constants.Companion.HOURLY_FORECAST_TABLE

@Entity(tableName = HOURLY_FORECAST_TABLE)
class HourlyForecastEntity(
    @TypeConverters(HourlyForecastTypeConverter::class)
    var hourlyForecastModel: HourlyForecastModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 1
}