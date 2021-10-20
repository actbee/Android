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


class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{

    private lateinit var my_map: GoogleMap
    private val PERMISSION_REQUEST_CODE = 0
    private lateinit var location_manager: LocationManager
    private var map_centered = false
    private lateinit var  marker_options: MarkerOptions
    private lateinit var  polylines_options: PolylineOptions
    private lateinit var  polylines: ArrayList<Polyline>
    private lateinit var  now_marker: Marker
    private lateinit var  type: String
    private lateinit var app_context: Context
    private lateinit var service_intent:Intent
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

        val intent: Intent = getIntent()
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

        var id: Int = intent.getIntExtra("map_id", 0)
        // decide if it needs to update the input or read the history from the database
        when(id){
            0 ->{
                app_context = this.applicationContext
                // open the service
                service_intent = Intent(this, MapService::class.java)
                startService(service_intent)
           //     app_context.bindService(service_intent, map_ViewModel, Context.BIND_AUTO_CREATE)
              //  map_ViewModel = ViewModelProvider(this).get(mapViewModel::class.java)

                var map_fragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
                map_fragment.getMapAsync(this)
            }
            else ->{
                database = ExerciseEntryDatabase.getInstance(this)
                databaseDao = database.exerciseEntryDatabaseDao
                repository = ExerciseEntryRepository(databaseDao)
                viewModelFactory = ExerciseEntryViewModelFactory(repository)
                exerciseentryViewModel = ViewModelProvider(this,
                    viewModelFactory).get(ExerciseEntryViewModel::class.java)

                delete = true
            }
        }

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
            location_manager.requestLocationUpdates(provider, 10, 0f, this)

        } catch (e: SecurityException) {
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        val pos = LatLng(lat, lng)
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
        message.setText(
            "Type: " + type + "\n" +
            lat.toString()+" , "+lng.toString())
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
    //  app_context.unbindService()
        stopService(service_intent)

        if (location_manager != null) {
            location_manager.removeUpdates(this)
        }
    }

    // add the delte bottom to the actionbar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      if(delete == true) {
          getMenuInflater().inflate(R.menu.delete, menu)
      }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id:Int = item.getItemId()
        if (id == R.id.history_delete_button){

        }
        return super.onOptionsItemSelected(item)
    }



}