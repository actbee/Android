package com.example.xuedan_zou_myrun2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import java.util.ArrayList
import java.util.Observer

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var my_map: GoogleMap
    private val PERMISSION_REQUEST_CODE = 0
    private var map_centered = false
    private lateinit var marker_options: MarkerOptions
    private lateinit var polylines_options: PolylineOptions
    private lateinit var polylines: ArrayList<Polyline>
    private lateinit var now_marker: Marker
    private lateinit var type: String
   // private lateinit var app_context: Context
    private lateinit var service_intent: Intent
    private var delete: Boolean = false
    private lateinit var exerciseentryViewModel: ExerciseEntryViewModel
    private lateinit var viewModelFactory: ExerciseEntryViewModelFactory
    private lateinit var map_ViewModel: mapViewModel
    private lateinit var database: ExerciseEntryDatabase
    private lateinit var repository: ExerciseEntryRepository
    private lateinit var databaseDao: ExerciseEntryDatabaseDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        val intent: Intent = intent
        val activity_type = intent.getStringExtra("activity_type")
        val input_type = intent.getStringExtra("input_type")
        // to set the input activity type
        when(input_type){
            "GPS" -> {
                type = activity_type!!
            }
            "Automatic" -> {
                type = "Unknown"
            }
        }
        val map_fragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        map_fragment.getMapAsync(this)
        service_intent = Intent(this, MapService::class.java)
        startService(service_intent)
        map_ViewModel = ViewModelProvider(this).get(mapViewModel::class.java)
        applicationContext.bindService(service_intent, map_ViewModel, Context.BIND_AUTO_CREATE)
        map_ViewModel.position.observe(this, { it ->
           // val updated_data:MapEntry = it
           // val posget:String = updated_data.location
            val posget:String = it
            val data = posget.split(",")
            val lat = data[0].toDouble()
            val lng = data[1].toDouble()
            val pos = LatLng(lat, lng)
            Location_Changed(pos)
        } )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        check_permission()
        my_map = googleMap
        my_map.mapType = GoogleMap.MAP_TYPE_NORMAL
        my_map
        polylines_options = PolylineOptions()
        polylines_options.color(Color.BLACK)
        polylines = ArrayList()
        marker_options = MarkerOptions()
    }

    fun Location_Changed(inpos:LatLng) {
        val pos = inpos
        val lat = inpos.latitude
        val lng = inpos.longitude
        // update our map
        if (!map_centered) {
            val change_camera = CameraUpdateFactory.newLatLngZoom(pos, 15f)
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
        val s = "Type: $type\n$lat , $lng"
        message.setText(s)
    }

    fun check_permission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    fun MapSaveClicked(view:View?){

    }

    fun MapCancelClicked(view: View?){
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unbindService(map_ViewModel)
        stopService(service_intent)
    }

    // add the delte bottom to the actionbar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      if(delete == true) {
          getMenuInflater().inflate(R.menu.delete, menu)
      }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int = item.getItemId()
        if (id == R.id.history_delete_button){

        }
        return super.onOptionsItemSelected(item)
    }
}