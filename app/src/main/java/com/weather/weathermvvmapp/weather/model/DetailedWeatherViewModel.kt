package com.weather.weathermvvmapp.weather.model

import androidx.lifecycle.LiveData
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
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

    override fun createLiveData(): LiveData<FutureWeatherListObjectModel>? =
        weatherRepositoryProvider.getSpecificWeatherModel(dayInMils)
}
