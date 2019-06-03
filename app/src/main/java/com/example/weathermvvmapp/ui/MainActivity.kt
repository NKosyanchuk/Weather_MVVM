package com.example.weathermvvmapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weathermvvmapp.R
import com.example.weathermvvmapp.extensions.loadFragment
import com.example.weathermvvmapp.extensions.showToast
import com.example.weathermvvmapp.ui.current_weather.CurrentWeatherFragment
import com.example.weathermvvmapp.ui.future_weather.FeatureWeatherFragment
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
        val featureWeatherFragment = FeatureWeatherFragment.newInstance()
        loadFragment(featureWeatherFragment)
    }

    override fun showLocationMapFragment() {
        showToast("Not implemented yet")
    }
}
