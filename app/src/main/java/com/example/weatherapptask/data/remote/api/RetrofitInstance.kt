package com.example.weatherapptask.data.remote.api

import com.example.weatherapptask.data.remote.other.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LocationApi by lazy {
        retrofit.create(LocationApi::class.java)
    }
}