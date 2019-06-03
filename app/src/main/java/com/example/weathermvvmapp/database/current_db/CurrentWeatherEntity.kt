package com.example.weathermvvmapp.database.current_db


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathermvvmapp.database.CURRENT_WEATHER_DATABASE_NAME
import com.example.weathermvvmapp.database.CURRENT_WEATHER_ID
import com.example.weathermvvmapp.database.entity.Coord
import com.example.weathermvvmapp.database.entity.Main
import com.example.weathermvvmapp.database.entity.Weather
import com.example.weathermvvmapp.database.entity.Wind
import com.google.gson.annotations.SerializedName

@Entity(tableName = CURRENT_WEATHER_DATABASE_NAME)
data class CurrentWeather(
    @SerializedName("base")
    val base: String,
    @Embedded(prefix = "coord_")
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Double,
    @Embedded(prefix = "main_")
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    val wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}