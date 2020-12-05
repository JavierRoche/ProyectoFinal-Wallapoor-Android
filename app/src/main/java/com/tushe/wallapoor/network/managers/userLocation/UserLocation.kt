package com.tushe.wallapoor.network.managers.userLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.showSnackbar

class UserLocation {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * STATIC INIT
     */

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }


    /**
     * PUBLIC FUNCTIONS
     */

    fun checkPermissions(activity: Activity): Boolean {
        // Comprobamos el estado de los permisos y devolvemos true solo cuando esten concedidos
        val permissionState = ActivityCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(activity: Activity) {
        // Booleano que indica si se debe mostrar un razonamiento al usuario para que otorgue permisos
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (shouldProvideRationale) showSnackbar(activity, R.string.permission_rationale, android.R.string.ok) {
            // Despues de una primera negacion de permisos se muestra el rationale
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)

        } else {
            // Primera solicitud y resto tras el rationale
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity, success: (Location) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            success(it)
        }
    }


    /**
     * PRIVATE FUNCTIONS
     */
}