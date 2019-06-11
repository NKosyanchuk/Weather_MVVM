package com.example.weathermvvmapp.database.future_db


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weathermvvmapp.database.FUTURE_WEATHER_DATABASE_NAME
import com.example.weathermvvmapp.database.FUTURE_WEATHER_ID
import com.example.weathermvvmapp.database.FutureWeatherConverter
import com.example.weathermvvmapp.database.entity.FutureWeatherList
import com.google.gson.annotations.SerializedName

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