package com.example.xuedan_zou_myrun2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import java.time.Duration

class DisplayEntryActivity : AppCompatActivity(){

    private var get_id: Long = -1L
    private var get_input: Int = 0
    private var get_activity: Int = 0
    private var get_date : String? = " "
    private var get_duration : Int = 0
    private var get_distance : Float = 0F
    private var get_calorie : Float = 0F
    private var get_heartrate: Int = 0

    private lateinit var input_view: TextView
    private lateinit var activity_view: TextView
    private lateinit var date_view: TextView
    private lateinit var duration_view: TextView
    private lateinit var distance_view: TextView
    private lateinit var calorie_view: TextView
    private lateinit var heart_view: TextView

    private lateinit var database: ExerciseEntryDatabase
    private lateinit var repository: ExerciseEntryRepository
    private lateinit var databaseDao: ExerciseEntryDatabaseDao
    private lateinit var viewModelFactory: ExerciseEntryViewModelFactory
    private lateinit var exerciseentryViewModel: ExerciseEntryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_entry_layout)

        input_view = findViewById(R.id.input_type)
        activity_view = findViewById(R.id.input_activity)
        date_view = findViewById(R.id.date_time)
        duration_view = findViewById(R.id.duration)
        distance_view = findViewById(R.id.distance)
        calorie_view = findViewById(R.id.calories)
        heart_view = findViewById(R.id.heart_rate)

        val intent: Intent = getIntent()
        get_id = intent.getLongExtra("id", -1L)
        get_activity = intent.getIntExtra("activity_type", 0)
        get_input = intent.getIntExtra("input_type", 0)
        get_date = intent.getStringExtra("date_and_time")
        get_duration = intent.getIntExtra("duration", 0)
        get_distance = intent.getFloatExtra("distance", 0F)
        get_calorie = intent.getFloatExtra("calorie", 0F)
        get_heartrate = intent.getIntExtra("heart_rate", 0)

        when(get_input){
            0 -> input_view.setText("Manual Entry")
            1 -> input_view.setText("GPS Entry")
            2 -> input_view.setText("Automatic Entry")
        }
        when(get_activity){
            0 -> activity_view.setText("Running")
            1 -> activity_view.setText("Walking")
            2 -> activity_view.setText("Standing")
            3 -> activity_view.setText("Cycling")
            4 -> activity_view.setText("Hiking")
            5 -> activity_view.setText("Downhill skiing")
            6 -> activity_view.setText("Cross-country skiing")
            7 -> activity_view.setText("Snowboarding")
            8 -> activity_view.setText("Skating")
            9 -> activity_view.setText("Swimming")
            10 -> activity_view.setText("Mountain biking")
            11 -> activity_view.setText("Wheelchair")
            12 -> activity_view.setText("Elliptical")
            13 -> activity_view.setText("Other")
        }
        date_view.setText(get_date)
        duration_view.setText(get_duration.toString()+"secs")
        calorie_view.setText(get_calorie.toString()+" cals")
        heart_view.setText(get_heartrate.toString()+" bpm")

        // convert the unit when caculating the distance! we save the distance as kilo in our database
        val pref: SharedPreferences = this.getSharedPreferences("unit", Context.MODE_PRIVATE)
        val unit_choice = pref.getInt("unit", -1)
        when(unit_choice){
            0 -> distance_view.setText(get_distance.toString()+" Kilometers")
            1 -> {
                val convert:Float = get_distance/0.621371F
                distance_view.setText(convert.toString()+" Miles")
            }
        }
        distance_view.setText(get_distance.toString()+" Miles")

        //  Toast.makeText(this,"get!"+ get_id.toString(), Toast.LENGTH_SHORT).show()
        // read the database according to the given id
        database = ExerciseEntryDatabase.getInstance(this)
        databaseDao = database.exerciseEntryDatabaseDao
        repository = ExerciseEntryRepository(databaseDao)
        viewModelFactory = ExerciseEntryViewModelFactory(repository)
        exerciseentryViewModel = ViewModelProvider(this,
            viewModelFactory).get(ExerciseEntryViewModel::class.java)


    }


    // add the delte bottom to the actionbar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.delete, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id:Int = item.getItemId()
        if (id == R.id.history_delete_button){

        }
        return super.onOptionsItemSelected(item)
    }

}