package com.example.xuedan_zou_myrun2

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

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
        my_title.text = ExerciseEntryList.get(position).id.toString()

        // need to modify our way to show the distance according to the saved_unit
        val pref: SharedPreferences = context.getSharedPreferences(
            "unit_saved",
            Context.MODE_PRIVATE
        )
        val unit = pref.getInt("unit", 0)
        when(unit){
            2131296542 ->{
                my_subtitle.text = ExerciseEntryList.get(position).distance.toString()+" Kilometers, "+
                        ExerciseEntryList.get(position).duration.toString()+"secs"

            }
                else ->{
                    var transfer = ExerciseEntryList.get(position).distance * 0.621371F
                    my_subtitle.text = transfer.toString()+" Miles, "+
                            ExerciseEntryList.get(position).duration.toString()+"secs"
                }
        }
        return view
    }

    fun replace(newExerciseEntryList: List<ExerciseEntry>){
        ExerciseEntryList = newExerciseEntryList
    }
}