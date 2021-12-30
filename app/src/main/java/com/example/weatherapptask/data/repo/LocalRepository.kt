package com.example.weatherapptask.data.repo

import com.example.weatherapptask.data.database.ForecastDao
import com.example.weatherapptask.data.database.HourlyForecastEntity
import com.example.weatherapptask.data.remote.model.LocationModel
import kotlinx.coroutines.flow.Flow

class LocalRepository(private val forecastDao: ForecastDao) {

    suspend fun insertLocationForecast(locationModel: LocationModel) {
        forecastDao.insertLocationForecast(locationModel)
    }

//    fun readLocationForecast(): Flow<List<LocationModel>> {
//        return forecastDao.readLocationForecast()
//    }

    fun readLocationForecast(): Flow<LocationModel> {
        return forecastDao.readLocationForecast()
    }

    suspend fun insertHourlyForecast(hourlyForecastEntity: HourlyForecastEntity) {
        forecastDao.insertHourlyForecast(hourlyForecastEntity)
    }

    fun readHourlyForecast(): Flow<List<HourlyForecastEntity>> {
        return forecastDao.readHourlyForecast()
    }
}