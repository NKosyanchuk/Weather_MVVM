package com.weather.weathermvvmapp.data.repository

import android.content.Context
import android.location.Location
import com.weather.weathermvvmapp.data.database.WeatherDatabase
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.database.current_db.fromApiDataToModelWeather
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherModel
import com.weather.weathermvvmapp.data.database.future_db.fromApiDataToFutureWeatherModel
import com.weather.weathermvvmapp.data.network.ApiWeatherInterface
import com.weather.weathermvvmapp.data.network.isDeviceOnline
import com.weather.weathermvvmapp.data.network.response.CurrentWeather
import com.weather.weathermvvmapp.data.network.response.FutureWeather
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


private const val RETRY_COUNT = 3L
private const val DAYS = 10
private const val TIMEOUT_INTERVAL = 15L

interface WeatherRepository {
    fun getCurrentWeather(location: LocationProvider): Observable<CurrentWeatherModel>?
    fun getFutureWeather(location: LocationProvider): Observable<FutureWeatherModel>?
}

interface SchedulersRepository {
    fun getWorkerScheduler(): Scheduler
    fun getResultScheduler(): Scheduler
}

class WeatherRepositoryProvider(
    private val apiWeatherInterface: ApiWeatherInterface,
    private val weatherDatabase: WeatherDatabase,
    private val context: Context
) : WeatherRepository, SchedulersRepository {

    override fun getWorkerScheduler() = Schedulers.io()

    override fun getResultScheduler(): Scheduler = AndroidSchedulers.mainThread()

    private fun getCurrentWeatherFromApi(location: Location): Observable<CurrentWeatherModel> =
        apiWeatherInterface.getCurrentWeather(location.latitude, location.longitude)
            .doOnNext { t: CurrentWeather? ->
                t?.let { weatherDatabase.currentWeatherDao().insert(it.fromApiDataToModelWeather()) }
            }
            .flatMap { t: CurrentWeather ->
                Observable.just(t.fromApiDataToModelWeather())
            }
            .subscribeOn(getWorkerScheduler())

    private fun getCurrentWeatherFromDataBase(): Observable<CurrentWeatherModel> =
        weatherDatabase.currentWeatherDao().getCurrentWeather()
            .takeUntil(Observable.timer(TIMEOUT_INTERVAL, java.util.concurrent.TimeUnit.SECONDS))
            .subscribeOn(getWorkerScheduler())

    override fun getCurrentWeather(location: LocationProvider): Observable<CurrentWeatherModel>? {
        return if (location.hasLocationPermission() && isDeviceOnline(context)) {
            Observable.mergeDelayError(
                location.getUserLastLocation()
                    ?.flatMap { userLocation: Location ->
                        getCurrentWeatherFromApi(userLocation)
                    }?.subscribeOn(getWorkerScheduler()),
                getCurrentWeatherFromDataBase().retry(RETRY_COUNT, ::retryPredicate)
            )
        } else {
            return getCurrentWeatherFromDataBase()
        }
    }

    private fun retryPredicate(t: Throwable) = t is HttpException

    override fun getFutureWeather(location: LocationProvider): Observable<FutureWeatherModel>? {
        return if (location.hasLocationPermission() && isDeviceOnline(context)) {
            Observable.mergeDelayError(location.getUserLastLocation()
                ?.flatMap { userLocation: Location ->
                    getFutureWeatherFromApi(userLocation)
                }
                ?.subscribeOn(getWorkerScheduler()), getFutureWeatherFromDatabase())
                .retry(RETRY_COUNT, ::retryPredicate)
        } else {
            return getFutureWeatherFromDatabase()
        }
    }

    private fun getFutureWeatherFromApi(location: Location): Observable<FutureWeatherModel>? =
        apiWeatherInterface.getFutureWeather(location.latitude, location.longitude, DAYS)
            .doOnNext { t: FutureWeather? ->
                t?.let {
                    weatherDatabase.futureWeatherDao().insert(it.fromApiDataToFutureWeatherModel())
                }
            }
            .flatMap { t: FutureWeather ->
                Observable.just(t.fromApiDataToFutureWeatherModel())
            }
            .subscribeOn(getWorkerScheduler())

    private fun getFutureWeatherFromDatabase() = weatherDatabase.futureWeatherDao().getFutureWeather()
        .subscribeOn(getWorkerScheduler())
}