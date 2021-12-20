package com.example.weatherapptask.data.di

import com.example.weatherapptask.data.repo.Repository
import com.example.weatherapptask.ui.mylocation.viewmodel.LocationForecastVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }

    viewModel { LocationForecastVM(get()) }
}