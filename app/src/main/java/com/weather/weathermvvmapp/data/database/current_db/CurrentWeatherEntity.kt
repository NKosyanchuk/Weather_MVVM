package com.weather.weathermvvmapp.data.database.current_db


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_ID
import com.weather.weathermvvmapp.data.network.response.CurrentWeather

@Entity(tableName = CURRENT_WEATHER_DATABASE_NAME)
data class CurrentWeatherModel(
    var base: String,
    val dt: Double,
    val temp: Double,
    val tempMax: Double,
    val tempMin: Double,
    val name: String,
    val visibility: Int,
    val description: String,
    val icon: String,
    val main: String,
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}

fun CurrentWeather.fromApiDataToModelWeather(): CurrentWeatherModel {
    return CurrentWeatherModel(
        this.base,
        this.dt,
        this.main.temp,
        this.main.tempMax,
        this.main.tempMin,
        this.name,
        this.visibility,
        this.weather[0].description,
        this.weather[0].icon,
        this.weather[0].main,
        this.wind.speed
    )
}