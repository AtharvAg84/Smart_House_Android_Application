package com.example.smarthome

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smarthome.databinding.ActivitySosSectionBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class SosSection : AppCompatActivity() {
    var locationManager: LocationManager? = null
    var binding: ActivitySosSectionBinding? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosSectionBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Runtime permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )
        }

        // Set up the button to get the location
        binding.getLocationBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                this.location
            }
        })
    }

    // Handle the result of permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @get:SuppressLint("MissingPermission")
    private val location: Unit
        get() {
            if (isLocationEnabled) {
                fusedLocationClient!!.lastLocation
                    .addOnSuccessListener(
                        this
                    ) { location ->
                        if (location != null) {
                            // Use the location object here
                            val latitude = location.latitude
                            val longitude = location.longitude

                            Toast.makeText(
                                this@SosSection,
                                """
                                Latitude: ${location.latitude}
                                Longitude: ${location.longitude}
                                """.trimIndent(),
                                Toast.LENGTH_LONG
                            ).show()
                            getAddressFromLocation(latitude, longitude)
                        } else {
                            Toast.makeText(
                                this@SosSection,
                                "Location not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@SosSection, "GPS not available", Toast.LENGTH_SHORT).show()
            }
        }

    private val isLocationEnabled: Boolean
        get() {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && !addresses.isEmpty()) {
                val address = addresses[0]
                val addressString = address.getAddressLine(0) // Get full address
                binding.locationTxt.setText("Address: $addressString")
            } else {
                binding.locationTxt.setText("No address found for the location.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Unable to get address.", Toast.LENGTH_SHORT).show()
        }
    }
}