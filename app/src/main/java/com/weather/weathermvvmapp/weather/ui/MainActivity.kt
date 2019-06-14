package com.weather.weathermvvmapp.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.extensions.loadFragment
import com.weather.weathermvvmapp.extensions.showToast
import com.weather.weathermvvmapp.weather.LifecycleBoundLocationManager
import com.weather.weathermvvmapp.weather.ui.current_weather.CurrentWeatherFragment
import com.weather.weathermvvmapp.weather.ui.future_weather.FutureWeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

interface MainMenuNavigator {
    fun showCurrentWeatherFragment()
    fun showFeatureWeatherFragment()
    fun showLocationMapFragment()
}

class MainActivity : AppCompatActivity(), MainMenuNavigator {

    private lateinit var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initBottomNavigation()

        fusedLocationProviderClient = FusedLocationProviderClient(this)

        requestLocationPermission()

        if (hasLocationPermission()) {
            bindLocationManager()
        } else {
            requestLocationPermission()
        }

        if (savedInstanceState == null) {
            showCurrentWeatherFragment()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else {
                showToast("Please allow the location permission")
            }
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
