package com.example.weathermvvmapp.current_weather

import androidx.lifecycle.Observer
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.example.weathermvvmapp.BaseUITest
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeatherModel
import com.weather.weathermvvmapp.data.repository.LocationProvider
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.ViewObject
import com.weather.weathermvvmapp.weather.model.CurrentWeatherViewModel
import com.weather.weathermvvmapp.weather.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify




@RunWith(AndroidJUnit4::class)
class CurrentWeatherTest : BaseUITest() {


    private lateinit var viewModel: CurrentWeatherViewModel

    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var runtimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @Mock
    lateinit var currentWeatherObserver: Observer<in ViewObject<CurrentWeatherModel>>

    @Mock
    lateinit var locationProvider: LocationProvider

    @Mock
    lateinit var weatherRepositoryProvider: WeatherRepositoryProvider

    @Before
    fun init() {
        viewModel = CurrentWeatherViewModel(weatherRepositoryProvider, locationProvider)
    }

    @Test
    fun testCurrentWeather() {
        sleepShort()
        viewModel.liveData().observeForever(currentWeatherObserver)
        sleepShort()
        verify(currentWeatherObserver).onChanged(getDefaultCurrentWeather())
    }

    private fun getDefaultCurrentWeather(): ViewObject<CurrentWeatherModel>? {
        return ViewObject(
            data = null,
            progress = false,
            error = false,
            throwable = null

        )
    }
}