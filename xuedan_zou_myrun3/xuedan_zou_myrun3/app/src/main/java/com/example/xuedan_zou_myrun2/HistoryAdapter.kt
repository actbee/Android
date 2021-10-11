package com.example.xuedan_zou_myrun2

import android.app.Activity
import android.content.Context
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
        my_subtitle.text = ExerciseEntryList.get(position).distance.toString()+"Miles, "+
                ExerciseEntryList.get(position).duration.toString()+"secs"

        return view
    }

    fun replace(newExerciseEntryList: List<ExerciseEntry>){
        ExerciseEntryList = newExerciseEntryList
    }
}