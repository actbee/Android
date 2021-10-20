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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import java.util.ArrayList


class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{

    private lateinit var my_map: GoogleMap
    private val PERMISSION_REQUEST_CODE = 0
    private lateinit var location_manager: LocationManager
    private var map_centered = false
    private lateinit var  marker_options: MarkerOptions
    private lateinit var  polylines_options: PolylineOptions
    private lateinit var  polylines: ArrayList<Polyline>
    private lateinit var  now_marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        val map_fragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        map_fragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        my_map = googleMap
        my_map.mapType = GoogleMap.MAP_TYPE_NORMAL
        polylines_options = PolylineOptions()
        polylines_options.color(Color.BLACK)
        polylines = ArrayList()
        marker_options = MarkerOptions()

        check_permission()
    }

    fun init_LocationManager() {
        try {
            location_manager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider = location_manager.getBestProvider(criteria, true)
            // get the last time's location
            val location = location_manager.getLastKnownLocation(provider!!)
            if(location != null) {
                onLocationChanged(location)
            }
            // update the location and call onLocationChanged every 1 second
            location_manager.requestLocationUpdates(provider, 15, 0f, this)

        } catch (e: SecurityException) {
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        val pos = LatLng(lat, lng)
        // update our map
        if (!map_centered) {
            val change_camera = CameraUpdateFactory.newLatLngZoom(pos, 10f)
            my_map.animateCamera(change_camera)
            map_centered = true

            marker_options.position(pos)
            // mark our first point
            var first_marker_options = MarkerOptions()
            first_marker_options.position(pos)
            first_marker_options.icon(BitmapDescriptorFactory.defaultMarker
                (BitmapDescriptorFactory.HUE_CYAN))
            my_map.addMarker(first_marker_options)
            now_marker = my_map.addMarker(marker_options)
            polylines_options.add(pos)
        }

        marker_options.position(pos)
        now_marker.remove()
        now_marker = my_map.addMarker(marker_options)
        polylines_options.add(pos)
        polylines.add(my_map.addPolyline(polylines_options))
        val message = findViewById<EditText>(R.id.type_stats)
        message.setText(lat.toString()+" , "+lng.toString())
    }

    fun check_permission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        else {
            init_LocationManager()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init_LocationManager()
            }
        }
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}

    fun MapSaveClicked(view:View?){

    }

    fun MapCancelClicked(view: View?){
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (location_manager != null)
            location_manager.removeUpdates(this)
    }
}