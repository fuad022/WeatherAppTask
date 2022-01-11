package com.example.weatherapptask.data.remote.api

import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.data.remote.model.DailyForecastModel
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.example.weatherapptask.data.remote.model.LocationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    //    @GET("weather")
//    suspend fun getCurrentForecast(
//        @Query("q") city: String,
//        @Query("units") units: String,
//        @Query("appid") apiKey: String
//    ): Response<LocationModel>
    @GET("weather")
    suspend fun getCurrentForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<FavoritesEntity>

    @GET("weather")
    suspend fun getCurrentForecastByCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<LocationModel>

    @GET("weather")
    suspend fun getSearchCurrentForecastByCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<FavoritesEntity>

    @GET("onecall")
    suspend fun getHourlyForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): Response<HourlyForecastModel>

    @GET("onecall")
    suspend fun getDailyForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): Response<DailyForecastModel>
}