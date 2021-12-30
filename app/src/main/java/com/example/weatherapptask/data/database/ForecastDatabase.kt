package com.example.weatherapptask.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapptask.data.remote.model.LocationModel
import com.example.weatherapptask.data.remote.other.Constants.Companion.DATABASE_NAME

@Database(
    entities = [LocationModel::class, HourlyForecastEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CoordinateTypeConverter::class,
    ForecastTypeConverter::class,
    TemperatureTypeConverter::class,
    WeatherListTypeConverter::class,
    WindTypeConverter::class,
    HourlyForecastTypeConverter::class
)
abstract class ForecastDatabase : RoomDatabase() {

    //    abstract fun locationForecastDao(): LocationForecastDao
    abstract val forecastDao: ForecastDao

//    companion object {
//        @Volatile
//        private var INSTANCE: ForecastDatabase? = null
//
//        fun getInstance(context: Context): ForecastDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        ForecastDatabase::class.java,
//                        DATABASE_NAME
//                    ).fallbackToDestructiveMigration().build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }

}