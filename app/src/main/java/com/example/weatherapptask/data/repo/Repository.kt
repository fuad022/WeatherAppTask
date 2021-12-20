package com.example.weatherapptask.data.repo

import com.example.weatherapptask.data.remote.api.RetrofitInstance
import com.example.weatherapptask.data.remote.model.LocationModel
import retrofit2.Response

class Repository {
    suspend fun getCurrentForecast(
        city: String, units: String, apiKey: String
    ): Response<LocationModel> = RetrofitInstance.api.getCurrentForecast(city, units, apiKey)
}