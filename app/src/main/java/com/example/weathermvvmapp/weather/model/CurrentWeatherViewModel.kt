package com.example.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.weathermvvmapp.database.WeatherDatabase
import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.network.createApiInterface
import com.example.weathermvvmapp.repository.LocationProvider
import com.example.weathermvvmapp.repository.WeatherRepositoryProvider
import io.reactivex.Flowable

class CurrentWeatherViewModel(
    private val locationProvider: LocationProvider,
    private val weatherRepositoryProvider: WeatherRepositoryProvider
) : BaseWeatherViewModel<CurrentWeather>(
    weatherRepositoryProvider.getWorkerScheduler(),
    weatherRepositoryProvider.getResultScheduler()
) {

    init {
        fetchData()
    }

    override fun createDataObservable(): Flowable<CurrentWeather> {
        return weatherRepositoryProvider.getCurrentWeather(locationProvider)
    }

    companion object {
        fun getInstance(fragment: Fragment, locationProvider: LocationProvider): CurrentWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val weatherRepositoryProvider = WeatherRepositoryProvider(createApiInterface(), WeatherDatabase.invoke(fragment.requireContext()))
                    return CurrentWeatherViewModel(locationProvider, weatherRepositoryProvider) as T
                }
            })[CurrentWeatherViewModel::class.java]
        }
    }
}
