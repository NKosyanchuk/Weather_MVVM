package com.example.weathermvvmapp.database.current_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathermvvmapp.database.CURRENT_WEATHER_DATABASE_NAME
import com.example.weathermvvmapp.database.CURRENT_WEATHER_ID

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeather)

    @Query("select * from $CURRENT_WEATHER_DATABASE_NAME where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeather>
}