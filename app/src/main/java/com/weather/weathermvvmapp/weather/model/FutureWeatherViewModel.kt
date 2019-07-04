package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherModel
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel
import io.reactivex.Observable

class FutureWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<FutureWeatherModel>(
    weatherRepositoryProvider.getWorkerScheduler(),
    weatherRepositoryProvider.getResultScheduler()
) {

    init {
        fetchData()
    }

    override fun createDataObservable(): Observable<FutureWeatherModel>? =
        weatherRepositoryProvider.getFutureWeather(locationProvider)

    companion object {
        fun getInstance(fragment: Fragment): FutureWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val weatherRepositoryProvider = WeatherRepositoryProvider(
                        createApiInterface(),
                        WeatherDatabase.invoke(fragment.requireContext()),
                        fragment.requireContext()
                    )

                    val locationProvider = LocationProvider(fragment.requireContext().applicationContext)
                    return FutureWeatherViewModel(weatherRepositoryProvider, locationProvider) as T
                }
            })[FutureWeatherViewModel::class.java]
        }
    }
}
