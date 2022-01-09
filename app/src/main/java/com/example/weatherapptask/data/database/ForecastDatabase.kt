package com.example.weatherapptask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapptask.data.remote.model.LocationModel

@Database(
    entities = [LocationModel::class, HourlyForecastEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CoordinateTypeConverter::class,
    ForecastTypeConverter::class,
    TemperatureTypeConverter::class,
    WeatherListTypeConverter::class,
    WindTypeConverter::class,
    HourlyForecastTypeConverter::class
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract val forecastDao: ForecastDao
}