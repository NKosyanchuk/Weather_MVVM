package com.weather.weathermvvmapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherDao
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherDao
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel


@Database(
    entities = [CurrentWeatherModel::class, FutureWeatherListObjectModel::class],
    version = 3, exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}