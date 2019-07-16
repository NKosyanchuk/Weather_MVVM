package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel

class CurrentWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<CurrentWeatherModel>() {
     override fun refreshData() {
        weatherRepositoryProvider.getCurrentWeather(locationProvider)
    }

    init {
        fetchData()
        weatherRepositoryProvider.getCurrentWeather(locationProvider)
    }

    override fun createLiveData(): LiveData<CurrentWeatherModel>? =
        weatherRepositoryProvider.getCurrentWeatherModel(locationProvider)

    companion object {
        fun getInstance(fragment: Fragment): CurrentWeatherViewModel {
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
                    return CurrentWeatherViewModel(weatherRepositoryProvider, locationProvider) as T
                }
            })[CurrentWeatherViewModel::class.java]
        }
    }
}
