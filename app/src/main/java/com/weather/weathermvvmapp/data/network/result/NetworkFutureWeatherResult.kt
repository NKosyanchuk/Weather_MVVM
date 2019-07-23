package com.weather.weathermvvmapp.data.network.result

import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel

sealed class NetworkFutureWeatherResult {
    data class Success(val futureWeatherList: List<FutureWeatherListObjectModel>?) : NetworkFutureWeatherResult()
    data class CommunicationError(val cause: Throwable?) : NetworkFutureWeatherResult()
}