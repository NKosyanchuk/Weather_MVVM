package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.createApiInterface
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
