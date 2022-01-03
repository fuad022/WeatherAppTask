package com.example.weatherapptask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapptask.data.remote.model.LocationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationForecast(locationModel: LocationModel)

    @Query("SELECT * FROM location_forecast_table ORDER BY id ASC")
    fun readLocationForecast(): Flow<LocationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecastEntity: HourlyForecastEntity)

    @Query("SELECT * FROM hourly_forecast_table ORDER BY id ASC")
    fun readHourlyForecast(): Flow<List<HourlyForecastEntity>>
}