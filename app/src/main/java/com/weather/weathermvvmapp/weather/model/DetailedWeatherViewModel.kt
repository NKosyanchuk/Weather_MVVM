package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel

class DetailedWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val dayInMils: Long?
) : BaseWeatherViewModel<FutureWeatherListObjectModel>() {

    override fun refreshData() {
        weatherRepositoryProvider.getSpecificFutureWeatherModelObject(dayInMils)
    }

    init {
        fetchData()
        weatherRepositoryProvider.getSpecificFutureWeatherModelObject(dayInMils)
    }

    override fun createLiveData(): LiveData<FutureWeatherListObjectModel>? = weatherRepositoryProvider.getSpecificWeatherModel(dayInMils)

    companion object {
        fun getInstance(
            fragment: Fragment,
            dayInMils: Long?
        ): DetailedWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val weatherRepositoryProvider = WeatherRepositoryProvider(
                        createApiInterface(),
                        WeatherDatabase.invoke(fragment.requireContext()),
                        NetworkProvider(fragment.requireContext())
                    )
                    return DetailedWeatherViewModel(weatherRepositoryProvider, dayInMils) as T
                }
            })[DetailedWeatherViewModel::class.java]
        }
    }
}
