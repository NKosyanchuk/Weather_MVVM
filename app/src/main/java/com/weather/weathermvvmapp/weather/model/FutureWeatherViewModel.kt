package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel

class FutureWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<List<FutureWeatherListObjectModel>>() {
    override fun refreshData() {
        weatherRepositoryProvider.getFutureWeather(locationProvider)
    }

    init {
        fetchData()
        weatherRepositoryProvider.getFutureWeather(locationProvider)
    }

    override fun createLiveData(): LiveData<List<FutureWeatherListObjectModel>>? =
        weatherRepositoryProvider.getFutureWeatherModel(locationProvider)

    companion object {
        fun getInstance(fragment: Fragment): FutureWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val weatherRepositoryProvider = WeatherRepositoryProvider(
                        createApiInterface(),
                        WeatherDatabase.invoke(fragment.requireContext()),
                        NetworkProvider(fragment.requireContext())
                    )

                    val locationProvider = LocationProvider(
                        fragment.requireContext().applicationContext,
                        fusedLocationProviderClient = FusedLocationProviderClient(fragment.requireContext().applicationContext)
                    )
                    return FutureWeatherViewModel(weatherRepositoryProvider, locationProvider) as T
                }
            })[FutureWeatherViewModel::class.java]
        }
    }
}
