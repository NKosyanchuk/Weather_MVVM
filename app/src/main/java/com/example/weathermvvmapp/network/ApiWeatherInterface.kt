package com.example.weathermvvmapp.network

import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.database.future_db.FutureWeather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiWeatherInterface {

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Observable<CurrentWeather>

    @GET("forecast/daily")
    fun getFutureWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: String = "10"
    ): Single<FutureWeather>
}