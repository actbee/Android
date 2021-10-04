package com.example.xuedanzoustressmeter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
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

class ImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagepage_layout)
    }

    fun onImageSubmitClicked(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun onImageCancelClicked(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
    }

}