package com.example.xuedan_zou_myrun2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MapActivity : AppCompatActivity() {
    override fun onCreate(bundle:Bundle?){
        super.onCreate(bundle)
        setContentView(R.layout.map_layout)
    }


    fun MapButtonClicked(view:View?){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}