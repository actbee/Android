package com.example.xuedan_zou_myrun2
// almost used the demo code of this part

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.time.Duration

class ManualEntryActivity : AppCompatActivity(){
    private val INPUT_PROPERTY = arrayOf(
        "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"
    )
    private lateinit var myListView: ListView
    private lateinit var database: ExerciseEntryDatabase
    private lateinit var repository: ExerciseEntryRepository
    private lateinit var databaseDao: ExerciseEntryDatabaseDao
    private lateinit var viewModelFactory: ExerciseEntryViewModelFactory
    private lateinit var exerciseentryViewModel: ExerciseEntryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_entry_layout)

        myListView = findViewById(R.id.ManualEntry_ListView)

        // database related behaviors
        database = ExerciseEntryDatabase.getInstance(this)
        databaseDao = database.exerciseEntryDatabaseDao
        repository = ExerciseEntryRepository(databaseDao)
        viewModelFactory = ExerciseEntryViewModelFactory(repository)
        exerciseentryViewModel = ViewModelProvider(this,
            viewModelFactory).get(ExerciseEntryViewModel::class.java)


        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, INPUT_PROPERTY)
        myListView.adapter = arrayAdapter
        myListView.setOnItemClickListener(){ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            show(position)
        }

    }

    fun show(position:Int){
        val fm =supportFragmentManager
        when(position){
            0 -> Date_Dialogs().show(fm, "date")
            1 -> MyTimeDialog().show(fm, "time")
            2 -> Duration_Dialogs().show(fm, "duration")
            3 -> Distance_Dialogs().show(fm, "distance")
            4 ->Calories_Dialogs().show(fm, "calories")
            5 ->HeartRate_Dialogs().show(fm, "heart_rate")
            6 ->Manual_Comment().show(fm, "manual_comment")
        }
    }

    fun ManualSavedClicked(view:View?){
        // save all the data to the ExerciseEntry data class and be inserted to the database
        val pref : SharedPreferences =this.getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val exercise_entry = ExerciseEntry()
        exercise_entry.activityType = pref.getInt("saved_activitytype", 0)
        exercise_entry.inputType = pref.getInt("saved_inputtype", 0)
        exercise_entry.dateTime = pref.getString("saved_time", "0") + pref.getString("saved_date","0")
        exercise_entry.duration = pref.getInt("saved_duration", 0)
        exercise_entry.distance = pref.getFloat("saved_distance", 0F)
        exercise_entry.calorie = pref.getFloat("saved_calorie", 0F)
        exercise_entry.heartRate = pref.getInt("saved_heartrate", 0)

        exerciseentryViewModel.insert(exercise_entry)
        // then clean out the sharedpreference
        pref.edit().clear().commit()

        Toast.makeText(this," Saved!", Toast.LENGTH_SHORT).show()
     //   val intent= Intent(this, MainActivity::class.java)
     //   startActivity(intent)
        finish()
    }

    fun ManualCancelClicked(view:View?){
        //  also clean out the sharedpreference
        val pref : SharedPreferences =this.getSharedPreferences("start",
            Context.MODE_PRIVATE)
        pref.edit().clear()
        pref.edit().commit()

        Toast.makeText(this,"Entry discared!", Toast.LENGTH_SHORT).show()
     //   val intent= Intent(this, MainActivity::class.java)
     //   startActivity(intent)
        finish()
    }
}