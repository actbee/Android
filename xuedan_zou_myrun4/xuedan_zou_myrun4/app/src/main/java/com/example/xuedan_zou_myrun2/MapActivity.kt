package com.example.xuedan_zou_myrun2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{

    private lateinit var mMap: GoogleMap
    private lateinit var location_manager: LocationManager
    private var mapCentered = false
    private  var PERMISSION_REQUEST_CODE = 0
    private lateinit var polylines_options: PolylineOptions
    private lateinit var polylines: ArrayList<Polyline>
    private lateinit var marker_options: MarkerOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
     //  val frag = supportFragmentManager.findFragmentById(R.id.fragment_map) as MapFragment
    //   frag.getMapAsync(this)
    }


    override fun onStart() {
        super.onStart()
        /*
        val frag = supportFragmentManager.findFragmentById(R.id.fragment_map)
        if (frag != null) {
            val mapFragment: SupportMapFragment = frag as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
         */
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        polylines_options = PolylineOptions()
        polylines_options.color(Color.BLACK)
        polylines = ArrayList()
        init_LocationManager()
        /*
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
         */

        checkPermisiion()
    }

    fun checkPermisiion(){
        if(Build.VERSION.SDK_INT < 23){
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        else
            init_LocationManager()
    }

    fun init_LocationManager() {
        try {
            location_manager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE

            val provider = location_manager.getBestProvider(criteria, true)
            val location = location_manager.getLastKnownLocation(provider!!)

            if(location != null)
                onLocationChanged(location)
            location_manager.requestLocationUpdates(provider, 0, 0f, this)
        } catch (e: SecurityException) {
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        val position_latlng = LatLng(lat, lng)
        if (!mapCentered) {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position_latlng, 17f)
            mMap.animateCamera(cameraUpdate)
            marker_options.position(position_latlng)
            mMap.addMarker(marker_options)
            polylines_options.add(position_latlng)
            mapCentered = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (location_manager != null)
            location_manager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) init_LocationManager()
        }
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}

}