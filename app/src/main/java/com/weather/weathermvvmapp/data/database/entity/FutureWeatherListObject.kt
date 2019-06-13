package com.weather.weathermvvmapp.data.database.entity


import com.google.gson.annotations.SerializedName

data class FutureWeatherListObject(
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("snow")
    val snow: Double,
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("temp")
    val temp: Temp,
    @SerializedName("weather")
    val weather: List<Weather>
)