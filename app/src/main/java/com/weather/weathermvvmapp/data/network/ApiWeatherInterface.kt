package com.weather.weathermvvmapp.data.network


import com.weather.weathermvvmapp.data.database.current_db.CurrentWeather
import com.weather.weathermvvmapp.data.database.future_db.FutureWeather
import io.reactivex.Observable
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
        @Query("cnt") cnt: Int
    ): Observable<FutureWeather>
}