package com.example.weatherapptask.data.remote.other

class Constants {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "545e119b278e9ddd371278175b707bcb"
        const val BAKU_CITY = "Baku"
        const val UNITS = "metric"
        const val LAT = "40.3777"
        const val LON = "49.892"
        const val EXCLUDE = "daily"
        const val EXCLUDE_CURRENT = "current"

        //ROOM Database
        const val DATABASE_NAME = "forecast_database"
        const val LOCATION_FORECAST_TABLE = "location_forecast_table"
        const val HOURLY_FORECAST_TABLE = "hourly_forecast_table"
    }
}