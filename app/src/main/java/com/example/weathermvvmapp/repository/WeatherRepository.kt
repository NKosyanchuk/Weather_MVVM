package com.example.weathermvvmapp.repository

import android.location.Location
import com.example.weathermvvmapp.database.WeatherDatabase
import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.database.future_db.FutureWeather
import com.example.weathermvvmapp.network.ApiWeatherInterface
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


private const val RETRY_COUNT = 3L

interface WeatherRepository {
    fun getCurrentWeather(location: LocationProvider): Observable<CurrentWeather>?
    fun getFutureWeather(location: LocationProvider): Flowable<FutureWeather>?
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

    override fun getFutureWeather(location: LocationProvider): Flowable<FutureWeather>? {
        return null
    }
}