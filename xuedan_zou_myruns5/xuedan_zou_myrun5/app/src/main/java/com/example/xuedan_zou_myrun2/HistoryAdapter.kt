package com.example.xuedan_zou_myrun2

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

class HistoryAdapter(val context: Context, var ExerciseEntryList: List<ExerciseEntry> ): BaseAdapter() {
    override fun getItem(position: Int): Any{
        return ExerciseEntryList.get(position)
    }
    override fun getItemId(position: Int): Long{
        return position.toLong()
    }
    override fun getCount(): Int{
        return ExerciseEntryList.size
    }
    override fun getView(position: Int, convert_view: View?, parent: ViewGroup?): View{
        val view: View = View.inflate(context, R.layout.layout_adapter, null)
        val my_title = view.findViewById<TextView>(R.id.Title)
        val my_subtitle = view.findViewById<TextView>(R.id.subTitle)

        //my_title.text = ExerciseEntryList.get(position).id.toString()
        val entry_input:Int = ExerciseEntryList.get(position).inputType
        var input_type:String = ""
        when(entry_input){
            0 -> input_type = "Manual Entry: "
            1 -> input_type = "GPS Entry: "
            else -> input_type = "Automatic Entry: "
        }
        val entry_activity:Int = ExerciseEntryList.get(position).activityType
        var activity_type:String = ""
        when(entry_activity){
            0 -> activity_type = "Running, "
            1 -> activity_type = "Walking, "
            2 -> activity_type = "Standing, "
            3 -> activity_type = "Cycling, "
            4 -> activity_type = "Hiking, "
            5 -> activity_type = "Downhill skiing, "
            6 -> activity_type = "Cross-country skiing, "
            7 -> activity_type = "Snowboarding, "
            8 -> activity_type = "Skating, "
            9 -> activity_type = "Swimming, "
            10 -> activity_type = "Mountain biking, "
            11 -> activity_type = "Wheelchair, "
            12 -> activity_type = "Elliptical, "
            13 -> activity_type = "Other, "
        }
        my_title.text = input_type + activity_type + ExerciseEntryList.get(position).dateTime

        // need to modify our way to show the distance according to the saved_unit
        val pref: SharedPreferences = context.getSharedPreferences(
            "unit_saved",
            Context.MODE_PRIVATE
        )
        val unit = pref.getInt("unit", 0)
        // get the seconds
        val duration = ExerciseEntryList.get(position).duration
        val sec = duration % 60
        val min =( duration / 60.0 ).toInt()
        var last_time: String = " "
        if(entry_input == 0){
            last_time = duration.toString() + "mins 0secs"
        }
        else {
            if (min == 0) {
                last_time = sec.toString() + "secs"
            } else {
                last_time = min.toString() + "mins " + sec.toString() + "secs"
            }
        }
        when(unit){
            R.id.metric ->{
                my_subtitle.text = ExerciseEntryList.get(position).distance.toString()+" Kilometers, "+ last_time
            }
                else ->{
                    var transfer = ExerciseEntryList.get(position).distance * 0.621371F
                    my_subtitle.text = transfer.toString()+" Miles, "+ last_time
                }
        }
        return view
    }

    fun replace(newExerciseEntryList: List<ExerciseEntry>){
        ExerciseEntryList = newExerciseEntryList
    }
}