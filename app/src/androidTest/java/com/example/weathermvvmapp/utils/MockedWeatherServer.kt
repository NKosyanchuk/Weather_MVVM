package com.example.weathermvvmapp.utils

import com.weather.weathermvvmapp.data.network.response.*

const val BASE = "base"
const val LAT = 10.7
const val LON = 15.5
const val DT = 0.1
const val TEMP_NOW = 20.3
const val TEMP_MAX = 30.4
const val TEMP_MIN = -7.3
const val NAME = "name"
const val VISIBILITY = 165

const val DESCRIPTION = "Sunny day"
const val ICON = "01d@2x"
const val ID = 10
const val MAIN = "main"

const val DEG = 10.8
const val WIND_SPEED = 6.3

class MockedWeatherServer {

    fun getCurrentWeatherMocked(): CurrentWeather {
        return CurrentWeather(
            BASE,
            Coord(LAT, LON),
            DT,
            Main(TEMP_NOW, TEMP_MAX, TEMP_MIN),
            NAME,
            VISIBILITY,
            createWeather(),
            createWind()
        )
    }

    private fun createWind(): Wind {
        return Wind(DEG, WIND_SPEED)
    }

    private fun createWeather(): ArrayList<Weather> {
        val weatherArray = arrayListOf<Weather>()
        val weather = Weather(DESCRIPTION, ICON, ID, MAIN)
        weatherArray.add(weather)
        return weatherArray
    }
}