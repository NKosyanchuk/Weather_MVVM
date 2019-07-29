package com.weather.weathermvvmapp

import android.app.Application
import com.weather.weathermvvmapp.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            } else {
                EmptyLogger()
            }
            androidContext(this@WeatherApplication)
            modules(
                listOf(
                    roomDataBaseModule, networkKoinModule, repositoryKoinModule, viewsModelModule, locationKoinModule
                )
            )
        }
    }
}
