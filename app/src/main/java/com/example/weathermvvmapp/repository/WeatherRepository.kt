package com.example.weathermvvmapp.repository

import com.example.weathermvvmapp.database.WeatherDatabase
import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.database.future_db.FutureWeather
import com.example.weathermvvmapp.network.ApiWeatherInterface
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.HttpException


private const val RETRY_COUNT = 3L

interface WeatherRepository {
    fun getCurrentWeather(location: LocationProvider): Flowable<CurrentWeather>
    fun getFutureWeather(location: LocationProvider): Flowable<FutureWeather>
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

    private fun getCurrentWeatherFromApi(location: LocationProvider): Flowable<CurrentWeather>? {
        return apiWeatherInterface
            .getCurrentWeather(location.latitude, location.longitude)
            .doAfterSuccess { currentWeather: CurrentWeather ->
                weatherDatabase.currentWeatherDao().insert(currentWeather)
            }
            .subscribeOn(getWorkerScheduler())
            .toFlowable()
    }

    private fun getCurrentWeatherFromDataBase(): Flowable<CurrentWeather>? {
        return weatherDatabase.currentWeatherDao().getCurrentWeather()
    }

    override fun getCurrentWeather(location: LocationProvider): Flowable<CurrentWeather> {
        return Flowable.mergeDelayError(
            getCurrentWeatherFromApi(location),
            getCurrentWeatherFromDataBase()
        )
            .retry(RETRY_COUNT, ::retryPredicate)
    }

    override fun getFutureWeather(location: LocationProvider): Flowable<FutureWeather> {
        return apiWeatherInterface
            .getFutureWeather(location.latitude, location.longitude)
            .toFlowable()
            .retry(RETRY_COUNT, ::retryPredicate)
    }

    private fun retryPredicate(t: Throwable): Boolean {
        return t is HttpException
    }
}