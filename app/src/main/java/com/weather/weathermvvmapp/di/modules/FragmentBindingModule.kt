package com.weather.weathermvvmapp.di.modules

import com.weather.weathermvvmapp.di.scopes.ActivityScoped
import com.weather.weathermvvmapp.weather.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class FragmentBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            // fragments
            CurrentWeatherModule::class,
            DetailedWeatherModule::class,
            FutureWeatherModule::class
            // other
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}