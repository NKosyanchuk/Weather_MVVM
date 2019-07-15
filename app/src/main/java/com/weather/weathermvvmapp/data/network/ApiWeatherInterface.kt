package com.weather.weathermvvmapp.data.network


import com.weather.weathermvvmapp.data.network.response.CurrentWeather
import com.weather.weathermvvmapp.data.network.response.FutureWeather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiWeatherInterface {

    @GET("weather")
    fun getCurrentWeatherAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<CurrentWeather>

    @GET("forecast/daily")
    fun getFutureWeatherAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int
    ): Deferred<FutureWeather>
}