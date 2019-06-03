package com.example.weathermvvmapp.database.future_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathermvvmapp.database.FUTURE_WEATHER_DATABASE_NAME
import com.example.weathermvvmapp.database.FUTURE_WEATHER_ID

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<FutureWeather>)

    @Query("select * from $FUTURE_WEATHER_DATABASE_NAME where id = $FUTURE_WEATHER_ID")
    fun getFutureWeather(): LiveData<FutureWeather>
}