package com.weather.weathermvvmapp.di.modules

import androidx.lifecycle.ViewModelProvider
import com.weather.weathermvvmapp.di.utils.WeatherViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: WeatherViewModelFactory):
            ViewModelProvider.Factory
}