package com.weather.weathermvvmapp.modules

import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import org.koin.dsl.module

val repositoryKoinModule = module {

    single {
        WeatherRepositoryProvider(get(), get<WeatherDatabase>(), get<NetworkProvider>())
    }
}