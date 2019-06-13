package com.weather.weathermvvmapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weather.weathermvvmapp.data.database.FutureWeatherConverter.GsonProvider.listFromJson
import com.weather.weathermvvmapp.data.database.FutureWeatherConverter.GsonProvider.listToJson
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherListObject
import com.weather.weathermvvmapp.data.database.entity.Weather
import java.lang.reflect.Type

const val DATABASE_NAME = "weatherDatabase.db"

const val CURRENT_WEATHER_DATABASE_NAME = "current_weather"
const val FUTURE_WEATHER_DATABASE_NAME = "future_weather"

const val CURRENT_WEATHER_ID = 0
const val FUTURE_WEATHER_ID = 10

class WeatherConverter {

    @TypeConverter
    fun fromWeatherList(value: List<Weather>): String {
        val type = object : TypeToken<List<Weather>>() {}.type
        return listToJson(value, type)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return listFromJson(value, type)
    }
}

class FutureWeatherConverter {
    @TypeConverter
    fun fromFeatureWeatherList(value: List<FutureWeatherListObject>): String {
        val gson = Gson()
        val type = object : TypeToken<List<FutureWeatherListObject>>() {}.type
        return listToJson(value, type)
    }

    @TypeConverter
    fun toFeatureWeatherList(value: String): List<FutureWeatherListObject> {
        val gson = Gson()
        val type = object : TypeToken<List<FutureWeatherListObject>>() {}.type
        return listFromJson(value, type)
    }

    object GsonProvider {
        private var gson: Gson? = null

        private fun getGson(): Gson {
            if (gson == null) {
                gson = Gson()
                return gson as Gson
            }
            return gson as Gson
        }

        fun <E> listFromJson(o: String, listType: Type): List<E> {
            return getGson().fromJson(o, listType)
        }

        fun <T> listToJson(list: List<T>, listType: Type): String {
            return getGson().toJson(list, listType)
        }
    }
}