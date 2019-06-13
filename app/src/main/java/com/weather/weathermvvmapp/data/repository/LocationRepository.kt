package com.weather.weathermvvmapp.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import io.reactivex.Observable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


interface LocationProviderInterface {
    fun getUserLastLocation(): Observable<Location>?
}

class LocationProvider(
    private val context: Context
) : LocationProviderInterface {

    @SuppressLint("MissingPermission")
    override fun getUserLastLocation(): Observable<Location>? {
        return if (hasLocationPermission()) {
            val locationProvider = ReactiveLocationProvider(context)
            locationProvider.lastKnownLocation
        } else {
            null
        }
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
}
