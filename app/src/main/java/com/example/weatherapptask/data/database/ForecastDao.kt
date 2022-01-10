package com.example.weatherapptask.data.database

import androidx.room.*
import com.example.weatherapptask.data.remote.model.LocationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationForecast(locationModel: LocationModel)

//    @Query("SELECT * FROM location_forecast_table ORDER BY id ASC")
    @Query("SELECT * FROM location_forecast_table ORDER BY dateTime DESC")
    fun readLocationForecast(): Flow<LocationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecastEntity: HourlyForecastEntity)

    @Query("SELECT * FROM hourly_forecast_table ORDER BY id ASC")
    fun readHourlyForecast(): Flow<List<HourlyForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteForecast(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_forecast_table ORDER BY id ASC")
    fun readFavoriteForecasts(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteForecast(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_forecast_table")
    suspend fun deleteAllFavoriteForecasts()
}