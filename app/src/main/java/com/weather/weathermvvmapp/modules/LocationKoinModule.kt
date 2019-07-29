package com.weather.weathermvvmapp.modules

import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.data.repository.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationKoinModule = module {

    single {
        FusedLocationProviderClient(androidContext())
    }

    single {
        LocationProvider(androidContext(), get<FusedLocationProviderClient>())
    }
}