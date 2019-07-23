package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.createApiInterface
import com.weather.weathermvvmapp.data.network.result.NetworkFutureWeatherResult
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel
import com.weather.weathermvvmapp.weather.ViewObject
import kotlinx.coroutines.launch

class FutureWeatherViewModel(
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
