package com.example.weatherapptask.data.di

import android.app.Application
import androidx.room.Room
import com.example.weatherapptask.data.database.ForecastDao
import com.example.weatherapptask.data.database.ForecastDatabase
import com.example.weatherapptask.data.repo.Repository
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.Constants.Companion.DATABASE_NAME
import com.example.weatherapptask.data.repo.LocalRepository
import com.example.weatherapptask.data.repo.MainRepository
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

val databaseModule = module {
    fun provideDatabase(application: Application): ForecastDatabase {
        return Room.databaseBuilder(application, ForecastDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(forecastDatabase: ForecastDatabase): ForecastDao {
        return forecastDatabase.forecastDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { LocalRepository(get()) }

    single { MainRepository(get(), get()) }
}