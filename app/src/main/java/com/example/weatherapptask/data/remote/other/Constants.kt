package com.example.weatherapptask.data.remote.other

class Constants {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "545e119b278e9ddd371278175b707bcb"
        const val UNITS = "metric"
        const val EXCLUDE = "daily"

        //ROOM Database
        const val DATABASE_NAME = "forecast_database"
        const val LOCATION_FORECAST_TABLE = "location_forecast_table"
        const val HOURLY_FORECAST_TABLE = "hourly_forecast_table"
        const val FAVORITE_FORECAST_TABLE = "favorite_forecast_table"

        const val PERMISSION_ID = 52
        const val GPS_CODE = 1002
    }
}