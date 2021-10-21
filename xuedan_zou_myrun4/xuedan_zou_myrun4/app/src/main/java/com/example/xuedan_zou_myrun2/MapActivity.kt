package com.example.xuedan_zou_myrun2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import java.util.*

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
    private lateinit var updated_data:MapEntry
    private var activity_id: Int = 0
    private var locationlist: String = ""
    private var input_type: String = ""
    private var get_id: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)

        database = ExerciseEntryDatabase.getInstance(this)
        databaseDao = database.exerciseEntryDatabaseDao
        repository = ExerciseEntryRepository(databaseDao)
        viewModelFactory = ExerciseEntryViewModelFactory(repository)
        exerciseentryViewModel = ViewModelProvider(this,
            viewModelFactory).get(ExerciseEntryViewModel::class.java)

        val intent: Intent = intent
        val activity_type = intent.getStringExtra("activity_type")
        activity_id = intent.getIntExtra("activity_id", 0)
        input_type = intent.getStringExtra("input_type")!!
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

        get_id = intent.getLongExtra("map_id", -1L)
        when(get_id){
            -1L ->{
                service_intent = Intent(this, MapService::class.java)
                startService(service_intent)
                map_ViewModel = ViewModelProvider(this).get(mapViewModel::class.java)
                applicationContext.bindService(service_intent, map_ViewModel, Context.BIND_AUTO_CREATE)
                map_ViewModel.data.observe(this, { it ->
                    updated_data = it
                    val posget:String = updated_data.location
                    // val posget:String = it
                    locationlist = locationlist + posget + ","
                    val data = posget.split(",")
                    val lat = data[0].toDouble()
                    val lng = data[1].toDouble()
                    val pos = LatLng(lat, lng)
                    Location_Changed(pos)
                } )
            }
            else ->{

                val buttonsaved = findViewById<Button>(R.id.map_save_button)
                val buttoncancel = findViewById<Button>(R.id.map_cancel_button)
                buttonsaved.setVisibility(View.GONE)
                buttoncancel.setVisibility(View.GONE)
                delete = true

            }
        }
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
        //val s = "Type: $type\n$lat , $lng"

        val s ="Type: $type\n" +
                "Avg speed: "+ String.format("%.2f",updated_data.avgspeed).toString() + "km/h\n"+
                "Cur speed: "+ String.format("%.2f",updated_data.speed).toString() + "km/h\n" +
                "Climb: " + String.format("%.2f",updated_data.altitude).toString() +"Kilometers\n" +
                "Calorie: ${updated_data.calorie.toInt()} \n" +
                "Distance: " + String.format("%.2f", updated_data.distance).toString() + "Kilometers\n" +
                "Time: ${updated_data.time}"

        /*
        val s ="Type: $type\n" +
                "Avg speed: ${updated_data.avgspeed} m/h\n" +
                "Cur speed: ${updated_data.speed} m/h\n" +
                "Climb: ${updated_data.altitude} Miles\n" +
                "Calorie: ${updated_data.calorie}\n" +
                "Distance: ${updated_data.distance} Miles"

         */
        message.setText(s)


    }

    fun check_permission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    fun MapSaveClicked(view:View?){
        val exercise_entry = ExerciseEntry()
        when(input_type){
            "GPS" -> {
                exercise_entry.inputType = 1
            }
            "Automatic" -> {
                exercise_entry.inputType = 2
            }
        }
        exercise_entry.activityType = activity_id
        exercise_entry.duration = updated_data.time.toInt()
        exercise_entry.distance = updated_data.distance
        exercise_entry.calorie = updated_data.calorie
        exercise_entry.avgSpeed = updated_data.avgspeed
        exercise_entry.climb = updated_data.altitude
        exercise_entry.locationlist = locationlist

        var calendar = Calendar.getInstance()

        var date = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "M " +
                    String.format("%02d", calendar.get(Calendar.MONTH)) + " " +
                    String.format("%d", calendar.get(Calendar.YEAR))
        var time = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" +
                    String.format("%02d", calendar.get(Calendar.SECOND))

        exercise_entry.dateTime = time+" "+date
        exerciseentryViewModel.insert(exercise_entry)
        Toast.makeText(this,"saved!", Toast.LENGTH_SHORT).show()
        finish()
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
            exerciseentryViewModel.delete(get_id)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}