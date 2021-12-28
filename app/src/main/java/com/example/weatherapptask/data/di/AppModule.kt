package com.example.weatherapptask.data.di

import com.example.weatherapptask.data.repo.Repository
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.ui.forecastreport.viewmodel.DailyForecastVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }

    viewModel { LocationForecastVM(get(), androidApplication()) }
    viewModel { HourlyForecastVM(get(), androidApplication()) }
    viewModel { DailyForecastVM(get(), androidApplication()) }
}