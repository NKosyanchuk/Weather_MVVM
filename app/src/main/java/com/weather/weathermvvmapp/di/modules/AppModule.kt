package com.weather.weathermvvmapp.di.modules

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.WeatherApplication
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideContext(application: WeatherApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesWeatherDatabase(context: Context): WeatherDatabase = WeatherDatabase.buildDatabase(context)

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient = FusedLocationProviderClient(context)
}