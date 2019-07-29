package com.weather.weathermvvmapp.modules

import androidx.room.Room
import com.weather.weathermvvmapp.data.database.DATABASE_NAME
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import org.koin.dsl.module

val roomDataBaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            WeatherDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}