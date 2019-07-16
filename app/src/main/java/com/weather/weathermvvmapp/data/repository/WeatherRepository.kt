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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val DAYS = 10

interface WeatherRepository {
    fun getCurrentWeatherModel(locationProvider: LocationProvider): LiveData<CurrentWeatherModel>?
    fun getFutureWeatherModel(locationProvider: LocationProvider): LiveData<List<FutureWeatherListObjectModel>>?
    fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherListObjectModel>?
}

class WeatherRepositoryProvider(
    private val apiWeatherInterface: ApiWeatherInterface,
    private val weatherDatabase: WeatherDatabase,
    private val networkProvider: NetworkProvider
) : WeatherRepository {

    private val currentWeatherMutableLiveData = MutableLiveData<CurrentWeatherModel>()

    override fun getCurrentWeatherModel(locationProvider: LocationProvider): LiveData<CurrentWeatherModel>? {
        return currentWeatherMutableLiveData
    }

    fun getCurrentWeather(location: LocationProvider) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userLastLocationAsync = userLocation(location)
                if (userLastLocationAsync != null && networkProvider.isDeviceOnline()) {
                    val fromApiDataToModelWeather = apiWeatherInterface.getCurrentWeatherAsync(
                        userLastLocationAsync.latitude,
                        userLastLocationAsync.longitude
                    ).await().fromApiDataToModelWeather()
                    GlobalScope.launch(Dispatchers.IO) {
                        weatherDatabase.currentWeatherDao().insert(fromApiDataToModelWeather)
                        currentWeatherMutableLiveData.postValue(fromApiDataToModelWeather)
                    }.join()
                } else {
                    currentWeatherMutableLiveData.postValue(getCurrentWeatherModelFromDB())
                }
            } catch (e: Exception) {
                currentWeatherMutableLiveData.postValue(null)
            }
        }
    }

    private suspend fun userLocation(location: LocationProvider): Location? = location.getUserLastLocationAsync()

    private suspend fun getCurrentWeatherModelFromDB(): CurrentWeatherModel {
        return withContext(Dispatchers.IO) {
            return@withContext weatherDatabase.currentWeatherDao().getCurrentWeather()
        }
    }

    private val futureWeatherMutableLiveData = MutableLiveData<List<FutureWeatherListObjectModel>>()

    override fun getFutureWeatherModel(locationProvider: LocationProvider): LiveData<List<FutureWeatherListObjectModel>>? {
        return futureWeatherMutableLiveData
    }

    fun getFutureWeather(locationProvider: LocationProvider) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userLastLocationAsync = userLocation(locationProvider)
                if (userLastLocationAsync != null && networkProvider.isDeviceOnline()) {
                    val fromApiDataToFutureModelWeatherList = createArrayFromApiWeatherList(
                        apiWeatherInterface.getFutureWeatherAsync(
                            userLastLocationAsync.latitude,
                            userLastLocationAsync.longitude,
                            DAYS
                        ).await().listWeather
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        weatherDatabase.futureWeatherDao().insertFutureListAll(fromApiDataToFutureModelWeatherList)
                        futureWeatherMutableLiveData.postValue(fromApiDataToFutureModelWeatherList)
                    }.join()
                } else {
                    futureWeatherMutableLiveData.postValue(getFutureWeatherModelFromDB())
                }
            } catch (e: Exception) {
                currentWeatherMutableLiveData.postValue(null)
            }
        }
    }

    private suspend fun getFutureWeatherModelFromDB(): List<FutureWeatherListObjectModel>? {
        return withContext(Dispatchers.IO) {
            return@withContext weatherDatabase.futureWeatherDao().getFutureList()
        }
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