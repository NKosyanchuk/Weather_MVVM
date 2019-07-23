package com.weather.weathermvvmapp.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.database.current_db.fromApiDataToModelWeather
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.database.future_db.createArrayFromApiWeatherList
import com.weather.weathermvvmapp.data.network.ApiWeatherInterface
import com.weather.weathermvvmapp.data.network.NetworkProvider
import com.weather.weathermvvmapp.data.network.result.NetworkCurrentWeatherResult
import com.weather.weathermvvmapp.data.network.result.NetworkFutureWeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val DAYS = 10

interface WeatherRepository {
    fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherListObjectModel>?
}

class WeatherRepositoryProvider(
    private val apiWeatherInterface: ApiWeatherInterface,
    private val weatherDatabase: WeatherDatabase,
    private val networkProvider: NetworkProvider
) : WeatherRepository {

    suspend fun getCurrentWeather(locationProvider: LocationProvider): NetworkCurrentWeatherResult {
        return try {
            val userLastLocationAsync = userLocation(locationProvider)
            if (userLastLocationAsync != null && networkProvider.isDeviceOnline()) {
                val fromApiDataToModelWeather = apiWeatherInterface.getCurrentWeatherAsync(
                    userLastLocationAsync.latitude,
                    userLastLocationAsync.longitude
                ).await()
                weatherDatabase.currentWeatherDao().insert(fromApiDataToModelWeather.fromApiDataToModelWeather())
                NetworkCurrentWeatherResult.Success(fromApiDataToModelWeather.fromApiDataToModelWeather())
            } else {
                NetworkCurrentWeatherResult.Success(getCurrentWeatherModelFromDB())
            }
        } catch (e: Exception) {
            NetworkCurrentWeatherResult.CommunicationError(e)
        }
    }

    private suspend fun userLocation(location: LocationProvider): Location? = location.getUserLastLocationAsync()

    private suspend fun getCurrentWeatherModelFromDB(): CurrentWeatherModel? {
        return weatherDatabase.currentWeatherDao().getCurrentWeather()
    }

    suspend fun getFutureWeather(locationProvider: LocationProvider): NetworkFutureWeatherResult {
        return try {
            val userLastLocationAsync = userLocation(locationProvider)
            if (userLastLocationAsync != null && networkProvider.isDeviceOnline()) {
                val fromApiDataToFutureModelWeatherList = createArrayFromApiWeatherList(
                    apiWeatherInterface.getFutureWeatherAsync(
                        userLastLocationAsync.latitude,
                        userLastLocationAsync.longitude,
                        DAYS
                    ).await().listWeather
                )
                weatherDatabase.futureWeatherDao().insertFutureListAll(fromApiDataToFutureModelWeatherList)
                NetworkFutureWeatherResult.Success(fromApiDataToFutureModelWeatherList)
            } else {
                NetworkFutureWeatherResult.Success(getFutureWeatherModelFromDB())
            }
        } catch (e: Exception) {
            NetworkFutureWeatherResult.CommunicationError(e)
        }
    }

    private suspend fun getFutureWeatherModelFromDB(): List<FutureWeatherListObjectModel>? {
        return weatherDatabase.futureWeatherDao().getFutureList()
    }

    private val specificWeatherMutableLiveData = MutableLiveData<FutureWeatherListObjectModel>()

    override fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherListObjectModel>? {
        return specificWeatherMutableLiveData
    }

    fun getSpecificFutureWeatherModelObject(dt: Long?) {
        GlobalScope.launch(Dispatchers.IO) {
            specificWeatherMutableLiveData.postValue(dt?.let {
                weatherDatabase.futureWeatherDao().getSpecificFutureObject(it)
            })
        }
    }
}