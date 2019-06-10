package com.example.weathermvvmapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.database.current_db.CurrentWeatherDao
import com.example.weathermvvmapp.database.future_db.FutureWeather
import com.example.weathermvvmapp.database.future_db.FutureWeatherDao

@Database(
    entities = [CurrentWeather::class, FutureWeather::class],
    version = 1, exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        @Volatile
        var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java, DATABASE_NAME
            )
                .build()
    }
}