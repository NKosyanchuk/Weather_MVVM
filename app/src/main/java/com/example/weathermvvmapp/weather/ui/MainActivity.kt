package com.example.weathermvvmapp.weather.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weathermvvmapp.R
import com.example.weathermvvmapp.extensions.loadFragment
import com.example.weathermvvmapp.extensions.showToast
import com.example.weathermvvmapp.weather.ui.current_weather.CurrentWeatherFragment
import com.example.weathermvvmapp.weather.ui.future_weather.FutureWeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


interface MainMenuNavigator {
    fun showCurrentWeatherFragment()
    fun showFeatureWeatherFragment()
    fun showLocationMapFragment()
}

class MainActivity : AppCompatActivity(), MainMenuNavigator {

    private lateinit var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initBottomNavigation()

        if (savedInstanceState == null) {
            showCurrentWeatherFragment()
        }
    }

    private fun initBottomNavigation() {
        setupBottomNavigationListener()
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    private fun setupBottomNavigationListener() {
        navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.currentWeatherFragment -> {
                    showCurrentWeatherFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.futureWeatherListFragment -> {
                    showFeatureWeatherFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.mapFragment -> {
                    showLocationMapFragment()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun showCurrentWeatherFragment() {
        val currentWeatherFragment = CurrentWeatherFragment.newInstance()
        loadFragment(currentWeatherFragment)
    }

    override fun showFeatureWeatherFragment() {
        val featureWeatherFragment = FutureWeatherFragment.newInstance()
        loadFragment(featureWeatherFragment)
    }

    override fun showLocationMapFragment() {
        showToast("Not implemented yet")
    }
}
