package com.weather.weathermvvmapp.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.weathermvvmapp.extensions.asDeferred


interface LocationProviderInterface {
    suspend fun getUserLastLocationAsync(): Location?
}

class LocationProvider(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProviderInterface {

    @SuppressLint("MissingPermission")
    override suspend fun getUserLastLocationAsync(): Location? {
        return if (hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation.asDeferred().await()
        } else {
            null
        }
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
}
