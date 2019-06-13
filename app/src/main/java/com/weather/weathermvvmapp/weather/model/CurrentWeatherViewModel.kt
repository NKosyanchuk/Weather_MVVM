package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeather
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel

import io.reactivex.Observable

class CurrentWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<CurrentWeather>(
    weatherRepositoryProvider.getWorkerScheduler(),
    weatherRepositoryProvider.getResultScheduler()
) {

    init {
        fetchData()
    }

    override fun createDataObservable(): Observable<CurrentWeather>? =
        weatherRepositoryProvider.getCurrentWeather(locationProvider)

    companion object {
        fun getInstance(fragment: Fragment): CurrentWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val weatherRepositoryProvider = WeatherRepositoryProvider(
                        createApiInterface(),
                        WeatherDatabase.invoke(fragment.requireContext())
                    )

                    val locationProvider = LocationProvider(fragment.requireContext().applicationContext)
                    return CurrentWeatherViewModel(weatherRepositoryProvider, locationProvider) as T
                }
            })[CurrentWeatherViewModel::class.java]
        }
    }
}
