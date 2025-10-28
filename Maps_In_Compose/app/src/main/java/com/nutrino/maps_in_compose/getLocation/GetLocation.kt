package com.nutrino.maps_in_compose.getLocation

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.getValue

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getLocation(comtext: Context){
     val fusedLocation by lazy {
        LocationServices.getFusedLocationProviderClient(comtext)
    }
    val cancellationToken = CancellationTokenSource()
    fusedLocation.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        cancellationToken.token

    ).addOnSuccessListener {
        if(it==null){
            Toast.makeText(comtext, "Location not avaliable !!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(comtext, "${it.latitude}", Toast.LENGTH_SHORT).show()
            Toast.makeText(comtext, "${it.latitude}", Toast.LENGTH_SHORT).show()
        }
    }
}