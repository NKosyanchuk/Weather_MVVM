package com.example.weathermvvmapp.utils

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.http.Fault
import com.weather.weathermvvmapp.data.network.BaseUrlChangingInterceptor
import com.weather.weathermvvmapp.data.network.WEATHER_URL
import com.weather.weathermvvmapp.data.network.response.*
import java.io.File


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

    // https://stackoverflow.com/questions/55635918/androidespressowiremock-java-lang-noclassdeffounderror-failed-resolution-of
    companion object {
        fun stubCurrentWeatherResponseWithError(errorCode: Int) {
            val url = "weather"
            BaseUrlChangingInterceptor.get().setInterceptor(WEATHER_URL + url)
            stubFor(
                get(urlPathMatching(url))
                    .willReturn(
                        aResponse()
                            .withFixedDelay(2000)
                            .withStatus(errorCode)
                            .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                    )
            )
        }

        fun stubFeatureWeatherResponseWithError(errorCode: Int) {
            val url = "forecast/daily"
            BaseUrlChangingInterceptor.get().setInterceptor(WEATHER_URL + url)
            stubFor(
                get(urlPathMatching(url))
                    .willReturn(
                        aResponse()
                            .withFixedDelay(2000)
                            .withStatus(errorCode)
                    )
            )
        }

        fun stubCurrentWeatherResponse() {
            val url = "weather"
            BaseUrlChangingInterceptor.get().setInterceptor(WEATHER_URL + url)
            val jsonBody = getJson("json/current_weather.json")
            stubFor(
                get(urlPathMatching(url))
                    .willReturn(
                        aResponse()
                            .withFixedDelay(2000)
                            .withStatus(200)
                            .withBody(jsonBody)
                    )
            )
        }

        private fun getJson(path: String): String {
            // Load the JSON response
            val uri = this.javaClass.classLoader.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        }
    }


}