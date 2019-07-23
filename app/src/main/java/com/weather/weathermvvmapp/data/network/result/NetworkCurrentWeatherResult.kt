package com.weather.weathermvvmapp.data.network.result

import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel

sealed class NetworkCurrentWeatherResult {
    data class Success(val currentWeatherModel: CurrentWeatherModel?) : NetworkCurrentWeatherResult()
    data class CommunicationError(val cause: Throwable?) : NetworkCurrentWeatherResult()
}