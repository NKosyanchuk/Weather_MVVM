package com.weather.weathermvvmapp.data.repository

import android.location.Location
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeather
import com.weather.weathermvvmapp.data.database.future_db.FutureWeather
import com.weather.weathermvvmapp.data.network.ApiWeatherInterface
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


private const val RETRY_COUNT = 3L
private const val DAYS = 10

interface WeatherRepository {
    fun getCurrentWeather(location: LocationProvider): Observable<CurrentWeather>?
    fun getFutureWeather(location: LocationProvider): Observable<FutureWeather>?
}

interface SchedulersRepository {
    fun getWorkerScheduler(): Scheduler
    fun getResultScheduler(): Scheduler
}

class WeatherRepositoryProvider(
    private val apiWeatherInterface: ApiWeatherInterface,
    private val weatherDatabase: WeatherDatabase
) : WeatherRepository, SchedulersRepository {

    override fun getWorkerScheduler() = Schedulers.io()

    override fun getResultScheduler(): Scheduler = AndroidSchedulers.mainThread()

    private fun getCurrentWeatherFromApi(location: Location): Observable<CurrentWeather> {
        return apiWeatherInterface.getCurrentWeather(location.latitude, location.longitude)
            .doOnNext { t: CurrentWeather? ->
                t?.let { weatherDatabase.currentWeatherDao().insert(it) }
            }
            .subscribeOn(getWorkerScheduler())
    }

    private fun getCurrentWeatherFromDataBase(): Observable<CurrentWeather>? {
        return weatherDatabase.currentWeatherDao().getCurrentWeather()
            .subscribeOn(getWorkerScheduler())
    }

    override fun getCurrentWeather(location: LocationProvider): Observable<CurrentWeather>? {
        return Observable.mergeDelayError(
            location.getUserLastLocation()
                ?.flatMap { userLocation: Location ->
                    getCurrentWeatherFromApi(userLocation)
                }?.subscribeOn(getWorkerScheduler()), getCurrentWeatherFromDataBase()
                ?.retry(RETRY_COUNT, ::retryPredicate)
        )
    }

    private fun retryPredicate(t: Throwable): Boolean {
        return t is HttpException
    }

    override fun getFutureWeather(location: LocationProvider): Observable<FutureWeather>? {
        return Observable.mergeDelayError(location.getUserLastLocation()?.flatMap { userLocation: Location ->
            getFutureWeatherFromApi(userLocation)
        }?.subscribeOn(getWorkerScheduler()), getFutureWeatherFromDatabase()).retry(RETRY_COUNT, ::retryPredicate)
    }

    private fun getFutureWeatherFromApi(location: Location): Observable<FutureWeather>? {
        return apiWeatherInterface.getFutureWeather(location.latitude, location.longitude, DAYS)
            .doOnNext { t: FutureWeather? -> t?.let { weatherDatabase.futureWeatherDao().insert(it) } }
            .subscribeOn(getWorkerScheduler())
    }

    private fun getFutureWeatherFromDatabase(): Observable<FutureWeather> {
        return weatherDatabase.futureWeatherDao().getFutureWeather()
            .subscribeOn(getWorkerScheduler())
    }
}