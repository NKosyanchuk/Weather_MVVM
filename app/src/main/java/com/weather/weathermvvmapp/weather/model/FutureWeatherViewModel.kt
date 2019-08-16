package com.weather.weathermvvmapp.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.result.NetworkFutureWeatherResult
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel
import com.weather.weathermvvmapp.weather.ViewObject
import kotlinx.coroutines.launch
import javax.inject.Inject

class FutureWeatherViewModel @Inject constructor(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
    private val locationProvider: LocationProvider
) : BaseWeatherViewModel<List<FutureWeatherListObjectModel>>() {

    private val futureWeatherMutableLiveData = MutableLiveData<List<FutureWeatherListObjectModel>>()

    private fun getFutureWeatherModel(): LiveData<List<FutureWeatherListObjectModel>>? {
        return futureWeatherMutableLiveData
    }

    override fun refreshData() {
        getFutureWeatherResult()
    }

    init {
        fetchData()
        getFutureWeatherResult()
    }

    private fun getFutureWeatherResult() {
        scope.launch {
            when (val futureWeatherResult: NetworkFutureWeatherResult =
                weatherRepositoryProvider.getFutureWeather(locationProvider)) {
                is NetworkFutureWeatherResult.Success -> {
                    futureWeatherMutableLiveData.postValue(futureWeatherResult.futureWeatherList)
                }
                is NetworkFutureWeatherResult.CommunicationError -> {
                    mutableLiveData.postValue(
                        ViewObject(
                            data = null,
                            progress = false,
                            error = true,
                            throwable = futureWeatherResult.cause
                        )
                    )
                }
            }
        }
    }

    override fun createLiveData(): LiveData<List<FutureWeatherListObjectModel>>? =
        getFutureWeatherModel()
}
