package com.example.xuedanzoustressmeter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.xuedanzoustressmeter.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class ImageActivity: AppCompatActivity() {
    private  var image_id: Int = 0
    private  var image_value: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagepage_layout)

        val intent: Intent = getIntent()
        image_id = intent.getIntExtra("image_id", 0)
        image_value = intent.getIntExtra("value", -1)
        if(image_id!=0) {
            val my_imageview:ImageView? = findViewById<ImageView>(R.id.imageclicked)
            my_imageview!!.setImageResource(image_id)
        }
    }

    fun onImageSubmitClicked(view: View?){
        Toast.makeText(this,image_value.toString(), Toast.LENGTH_SHORT).show()
        val pref: SharedPreferences = this.getSharedPreferences("stress_saved",
            Context.MODE_PRIVATE)
        pref.edit().putInt("saved_state", (1..3).random()).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onImageCancelClicked(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}