package com.example.weathermvvmapp.database

import androidx.room.TypeConverter
import com.example.weathermvvmapp.database.entity.FutureWeatherList
import com.example.weathermvvmapp.database.entity.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val DATABASE_NAME = "weatherDatabase.db"

const val CURRENT_WEATHER_DATABASE_NAME = "current_weather"
const val FUTURE_WEATHER_DATABASE_NAME = "future_weather"

const val CURRENT_WEATHER_ID = 0
const val FUTURE_WEATHER_ID = 10

class WeatherConverter {

    @TypeConverter
    fun fromWeatherList(value: List<Weather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(value, type)
    }
}

class FutureWeatherConverter {
    @TypeConverter
    fun fromFeatureWeatherList(value: List<FutureWeatherList>): String {
        val gson = Gson()
        val type = object : TypeToken<List<FutureWeatherList>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFeatureWeatherList(value: String): List<FutureWeatherList> {
        val gson = Gson()
        val type = object : TypeToken<List<FutureWeatherList>>() {}.type
        return gson.fromJson(value, type)
    }
}