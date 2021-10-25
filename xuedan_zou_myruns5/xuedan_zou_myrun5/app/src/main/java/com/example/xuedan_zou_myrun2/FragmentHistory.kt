package com.example.xuedan_zou_myrun2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FragmentHistory:Fragment(){

    private lateinit var history_listview: ListView
    private lateinit var database: ExerciseEntryDatabase
    private lateinit var repository: ExerciseEntryRepository
    private lateinit var databaseDao: ExerciseEntryDatabaseDao
    private lateinit var viewModelFactory: ExerciseEntryViewModelFactory
    private lateinit var exerciseentryViewModel: ExerciseEntryViewModel

    private lateinit var arrayList: ArrayList<ExerciseEntry>
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_history, container, false)
        history_listview = view.findViewById(R.id.History_ListView)

        // database related behaviors
        database = ExerciseEntryDatabase.getInstance(requireActivity())
        databaseDao = database.exerciseEntryDatabaseDao
        repository = ExerciseEntryRepository(databaseDao)
        viewModelFactory = ExerciseEntryViewModelFactory(repository)
        exerciseentryViewModel = ViewModelProvider(this,
            viewModelFactory).get(ExerciseEntryViewModel::class.java)

       // bind the costom adapter to the listview
       arrayList = ArrayList()
       historyAdapter = HistoryAdapter(requireActivity(), arrayList)
       history_listview.adapter = historyAdapter

        /*
       exerciseentryViewModel.allExerciseEntryLiveData.observe(requireActivity(), Observer { it ->
           historyAdapter.replace(it)
           historyAdapter.notifyDataSetChanged()
       })
       */
        history_listview.setOnItemClickListener(){ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            // get the ExerciseEntry we get
            val item:ExerciseEntry = history_listview.getItemAtPosition(position) as ExerciseEntry
           //  Toast.makeText(requireActivity(),item.toString(), Toast.LENGTH_SHORT).show()
            when(item.inputType){
                0 ->{
                    val intent = Intent(requireActivity(), DisplayEntryActivity::class.java)
                    intent.putExtra("id", item.id)
                    intent.putExtra("input_type", item.inputType)
                    intent.putExtra("activity_type", item.activityType)
                    intent.putExtra("date_and_time", item.dateTime)
                    intent.putExtra("duration", item.duration)
                    intent.putExtra("distance", item.distance)
                    intent.putExtra("calorie", item.calorie)
                    intent.putExtra("heart_rate", item.heartRate)
                    startActivity(intent)
                }
                else ->{
                    val intent = Intent(requireActivity(), MapActivity::class.java)
                    var bundle: Bundle = Bundle()
                    bundle.putString("location", item.locationlist)
                    bundle.putFloat("avgspeed",item.avgSpeed)
                    bundle.putFloat("distance", item.distance)
                    bundle.putFloat("calorie", item.calorie)
                    bundle.putFloat("altitude",item.climb)
                    bundle.putLong("map_id", item.id)
                    intent.putExtras(bundle)

                    intent.putExtra("activity_type", item.activityType)
                    when(item.inputType){
                        1 -> intent.putExtra("input_type", "GPS")
                        2 -> intent.putExtra("input_type", "Automatic")
                    }
                    intent.putExtra("id", item.id)
                    startActivity(intent)
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        exerciseentryViewModel.allExerciseEntryLiveData.observe(requireActivity(), Observer { it ->
            historyAdapter.replace(it)
            historyAdapter.notifyDataSetChanged()
        })
    }

}

