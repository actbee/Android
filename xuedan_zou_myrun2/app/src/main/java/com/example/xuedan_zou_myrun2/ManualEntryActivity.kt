package com.example.xuedan_zou_myrun2
// almost used the demo code of this part

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ManualEntryActivity : AppCompatActivity(){
    private val INPUT_PROPERTY = arrayOf(
        "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"
    )
    private lateinit var myListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_entry_layout)

        myListView = findViewById(R.id.ManualEntry_ListView)

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, INPUT_PROPERTY)
        myListView.adapter = arrayAdapter
        myListView.setOnItemClickListener(){ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            println("debug: parent: $parent | view: $view | position: $position | id: $id")
        }

    }

    fun ManualSavedClicked(view:View?){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ManualCancelClicked(view:View?){
        Toast.makeText(this,"Entry discared!", Toast.LENGTH_SHORT).show()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}