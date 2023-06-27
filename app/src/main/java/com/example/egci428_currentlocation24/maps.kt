package com.example.egci428_currentlocation24

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.egci428_currentlocation24.databinding.ActivityMapsBinding

class maps : AppCompatActivity(), OnMapReadyCallback {

    private var k = 0
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var currentLatLng: LatLng? = null

    private var gpsBtn: Button? = null
    private var mapBtn: Button? = null
    private var textView: TextView? = null

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapBtn = findViewById(R.id.mnapBtn)
        mapBtn!!.setOnClickListener {
            mMap.addMarker(
                MarkerOptions().position(currentLatLng!!).title(currentLatLng.toString())
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 8F))
            println("MAP clicked")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object: LocationListener{
            override fun onLocationChanged(location: Location){
                textView = findViewById(R.id.textView)
                textView?.text = location.latitude.toString() + "," + location.longitude.toString()
                currentLatLng = LatLng(location.latitude,location.longitude)
            }
            override fun onProviderDisabled(provider: String){
                super.onProviderDisabled(provider)
                val intent = Intent(ContactsContract.Settings.ACTION_SET_DEFAULT_ACCOUNT)
                startActivity(intent)
            }
        }
        Thread.sleep(1000)
        request_location()

    }

    private fun request_location() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), 10)
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 10)
            }
            return
        }

        gpsBtn = findViewById(R.id.gpsBtn)
        gpsBtn!!.setOnClickListener {
            locationManager!!.requestLocationUpdates("gps", 5000, 0F, locationListener!!)
        }

    }

}