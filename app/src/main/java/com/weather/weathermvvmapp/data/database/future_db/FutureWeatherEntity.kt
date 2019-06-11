package com.weather.weathermvvmapp.data.database.future_db


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_ID
import com.weather.weathermvvmapp.data.database.FutureWeatherConverter
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherList

@Entity(tableName = FUTURE_WEATHER_DATABASE_NAME)
data class FutureWeather(
    @SerializedName("cod")
    val cod: String,

    @SerializedName("list")
    @Embedded
    @TypeConverters(FutureWeatherConverter::class)
    val list: ArrayList<FutureWeatherList>?,
    @SerializedName("message")
    val message: Int
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = FUTURE_WEATHER_ID
}