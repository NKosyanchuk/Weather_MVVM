package com.weather.weathermvvmapp.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.network.result.NetworkCurrentWeatherResult
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel
import com.weather.weathermvvmapp.weather.ViewObject
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<CurrentWeatherModel>() {
    override fun refreshData() {
        getCurrentWeatherResult()
    }

    private val currentWeatherMutableLiveData = MutableLiveData<CurrentWeatherModel>()

    private fun getCurrentWeatherModel(): LiveData<CurrentWeatherModel>? {
        return currentWeatherMutableLiveData
    }

    init {
        fetchData()
        getCurrentWeatherResult()
    }

    private fun getCurrentWeatherResult() {
        scope.launch {
            when (val currentWeatherResult: NetworkCurrentWeatherResult =
                weatherRepositoryProvider.getCurrentWeather(locationProvider)) {
                is NetworkCurrentWeatherResult.Success -> {
                    currentWeatherMutableLiveData.postValue(currentWeatherResult.currentWeatherModel)
                }
                is NetworkCurrentWeatherResult.CommunicationError -> {
                    mutableLiveData.postValue(
                        ViewObject(
                            data = null,
                            progress = false,
                            error = true,
                            throwable = currentWeatherResult.cause
                        )
                    )
                }
            }
        }
    }

    override fun createLiveData(): LiveData<CurrentWeatherModel>? =
        getCurrentWeatherModel()
}
