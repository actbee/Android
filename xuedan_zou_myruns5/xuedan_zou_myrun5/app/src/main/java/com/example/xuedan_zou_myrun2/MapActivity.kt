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
    private val PERMISSION_REQUEST_CODE = 0
    private var activity_type: Int = 0
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

        val map_fragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        map_fragment.getMapAsync(this)

    }
    override fun onResume() {
           super.onResume()

    }

    override fun onMapReady(googleMap: GoogleMap) {

        my_map = googleMap
        my_map.mapType = GoogleMap.MAP_TYPE_NORMAL
        polylines_options = PolylineOptions()
        polylines_options.color(Color.BLACK)
        polylines = ArrayList()
        marker_options = MarkerOptions()
        check_permission()

        val intent: Intent = intent
        //val activity_type = intent.getStringExtra("activity_type")
        activity_type = intent.getIntExtra("activity_type", 0)
        input_type = intent.getStringExtra("input_type")!!
        // to set the input activity type
        when (input_type) {
            "GPS" -> {
                when (activity_type) {
                    0 -> type = "Running"
                    1 -> type = "Walking"
                    2 -> type = "Standing"
                    3 -> type = "Cycling"
                    4 -> type = "Hiking"
                    5 -> type = "Downhill skiing"
                    6 -> type = "Cross-country skiing"
                    7 -> type = "Snowboarding"
                    8 -> type = "Skating"
                    9 -> type = "Swimming"
                    10 -> type = "Mountain biking"
                    11 -> type = "Wheelchair"
                    12 -> type = "Elliptical"
                    13 -> type = "Other"
                }
            }
            "Automatic" -> {
                type = "Unknown"
            }
        }

        map_ViewModel = ViewModelProvider(this).get(mapViewModel::class.java)

        val bundle = intent.getExtras()!!
        get_id = intent.getLongExtra("map_id", -1L)
        // get_id =bundle.getLong("id", -1L)

        when (get_id) {
            -1L -> {
                service_intent = Intent(this, MapService::class.java)
                service_intent.putExtra("input_type", input_type)
                startService(service_intent)
                applicationContext.bindService(
                    service_intent,
                    map_ViewModel,
                    Context.BIND_AUTO_CREATE
                )
                map_ViewModel.data.observe(this, { it ->
                    updated_data = it
                    val posget: String = updated_data.location
                    // val posget:String = it
                    locationlist = locationlist + posget + ","
                    val data = posget.split(",")
                    val lat = data[0].toDouble()
                    val lng = data[1].toDouble()
                    val pos = LatLng(lat, lng)
                    Location_Changed(pos)

                    val message = findViewById<EditText>(R.id.type_stats)
                    //val s = "Type: $type\n$lat , $lng"

                    if(input_type == "Automatic"){
                        // show the current activity
                        var type_id:Int = updated_data.current_activity
                        when(type_id){
                            0 -> type = "Standing"
                            1 -> type = "Walking"
                            2 -> type = "Running"
                        }
                        // save the general activity
                        when(updated_data.activity_type){
                            // 0 is standing so maps to 2
                            0 -> activity_type = 2
                            // 1 is walking so maps to 1
                            1 -> activity_type = 1
                            // 2 is running so maps to 0
                            2 -> activity_type = 0
                        }
                    }

                    val s ="Type: $type\n" +
                            "Avg speed: "+ String.format("%.2f",updated_data.avgspeed).toString() + "km/h\n"+
                            "Cur speed: "+ String.format("%.2f",updated_data.speed).toString() + "km/h\n" +
                            "Climb: " + String.format("%.2f",updated_data.altitude).toString() +"Kilometers\n" +
                            "Calorie: ${updated_data.calorie.toInt()} \n" +
                            "Distance: " + String.format("%.2f", updated_data.distance).toString() + "Kilometers"

                    /*
                    val s ="Type: $type\n" +
                            "Avg speed: ${updated_data.avgspeed} m/h\n" +
                            "Cur speed: ${updated_data.speed} m/h\n" +
                            "Climb: ${updated_data.altitude} Miles\n" +
                            "Calorie: ${updated_data.calorie}\n" +
                            "Distance: ${updated_data.distance} Miles"

                     */
                    message.setText(s)

                })
            }
            // we need to read our datas from the database
            else -> {
                locationlist = bundle.getString("location")!!
                val avgspeed = bundle.getFloat("avgspeed", 0F)
                val distance = bundle.getFloat("distance", 0F)
                val calorie = bundle.getFloat("calorie", 0F)
                val altitude = bundle.getFloat("altitude", 0F)
                // seperate our locationlist
                val locations = locationlist.split(",")
                val end:Int = locations.size - 2

                if(input_type == "Automatic"){
                    when(activity_type){
                        0 -> type = "Running"
                        1 -> type = "Walking"
                        2 -> type = "Standing"
                        3 -> type = "Cycling"
                        4 -> type = "Hiking"
                        5 -> type = "Downhill skiing"
                        6 -> type = "Cross-country skiing"
                        7 -> type = "Snowboarding"
                        8 -> type = "Skating"
                        9 -> type = "Swimming"
                        10 -> type = "Mountain biking"
                        11 -> type = "Wheelchair"
                        12 -> type = "Elliptical"
                        13 -> type = "Other"
                    }
                }

                for (i: Int in 0..end step 2) {
                    var lat = locations[i].toDouble()
                    var lng = locations[i + 1].toDouble()
                    var pos = LatLng(lat, lng)
                    Location_Changed(pos)
                }
                val message = findViewById<EditText>(R.id.type_stats)
                //val s = "Type: $type\n$lat , $lng"
                // to transfer the unit
                val pref: SharedPreferences = this.getSharedPreferences(
                    "unit_saved",
                    Context.MODE_PRIVATE
                )
                val unit = pref.getInt("unit", 0)
                var s:String = " "
                when(unit){
                    R.id.metric ->{
                        s ="Type: $type\n" +
                                "Avg speed: "+ String.format("%.2f",avgspeed).toString() + "km/h\n"+
                                "Cur speed: n/a\n" +
                                "Climb: " + String.format("%.2f",altitude).toString() +"Kilometers\n" +
                                "Calorie: ${calorie.toInt()} \n" +
                                "Distance: " + String.format("%.2f",distance) + "Kilometers\n"
                    }
                    else ->{
                        s ="Type: $type\n" +
                                "Avg speed: "+ String.format("%.2f",avgspeed * 0.621371F).toString() + "m/h\n"+
                                "Cur speed: n/a\n" +
                                "Climb: " + String.format("%.2f",altitude * 0.621371F).toString() +"Miles\n" +
                                "Calorie: ${calorie.toInt()} \n" +
                                "Distance: " + String.format("%.2f",distance * 0.621371F) + "Miles\n"
                    }
                }

                message.setText(s)


                val buttonsaved = findViewById<Button>(R.id.map_save_button)
                val buttoncancel = findViewById<Button>(R.id.map_cancel_button)
                buttonsaved.setVisibility(View.GONE)
                buttoncancel.setVisibility(View.GONE)
                delete = true

            }
        }
    }

    fun Location_Changed(inpos:LatLng) {
        val pos = inpos
        val lat = inpos.latitude
        val lng = inpos.longitude
        // update our map
        if (!map_centered) {
            val change_camera = CameraUpdateFactory.newLatLngZoom(pos, 18f)
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
        exercise_entry.activityType = activity_type
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
        // only when we open the serivice, that is ,when we can not delete
        if(delete == false) {
            applicationContext.unbindService(map_ViewModel)
            stopService(service_intent)
        }
    }

    fun check_permission(): Boolean {
        if (Build.VERSION.SDK_INT < 23){
            return false
        }
       while(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
           PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE)
        }
        return true
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