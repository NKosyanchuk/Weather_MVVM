package com.weather.weathermvvmapp.modules

import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.model.CurrentWeatherViewModel
import com.weather.weathermvvmapp.weather.model.DetailedWeatherViewModel
import com.weather.weathermvvmapp.weather.model.FutureWeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewsModelModule = module {
    viewModel { CurrentWeatherViewModel(get<WeatherRepositoryProvider>(), get<LocationProvider>()) }

    viewModel { (dayInMils: Long) -> DetailedWeatherViewModel(get<WeatherRepositoryProvider>(), dayInMils) }

    viewModel { FutureWeatherViewModel(get<WeatherRepositoryProvider>(), get<LocationProvider>()) }
}