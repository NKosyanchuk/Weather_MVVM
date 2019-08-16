package com.weather.weathermvvmapp.di

import com.weather.weathermvvmapp.WeatherApplication
import com.weather.weathermvvmapp.di.modules.AppModule
import com.weather.weathermvvmapp.di.modules.FragmentBindingModule
import com.weather.weathermvvmapp.di.modules.NetworkModule
import com.weather.weathermvvmapp.di.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] is the module from Dagger.Android that helps with the
 * generation and location of subcomponents.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FragmentBindingModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<WeatherApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<WeatherApplication>()
}