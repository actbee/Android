package com.example.myruns1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File
import android.view.View


class MainActivity : AppCompatActivity() {

    private val temp_img_name= "my_run.jpg"
    private lateinit var  temp_img_uri: Uri
    private lateinit var  image_view: ImageView
    private lateinit var  view_model: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        Util.askPermissions(this)

        image_view = findViewById(R.id.image)

        view_model = ViewModelProvider(this).get(ViewModel::class.java)
        view_model.user_img.observe(this, {
                it -> image_view.setImageBitmap(it)
        })

        val temp_image = File(getExternalFilesDir(null), temp_img_name)
        temp_img_uri = FileProvider.getUriForFile(this, "com.example.myruns1", temp_image)
        println("debug: here?")
        if(temp_image.exists()) {
            val bitmap = Util.get_bitmap(this, temp_img_uri)
            image_view.setImageBitmap(bitmap)
        }
    }

    val cameraResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val bitmap = Util.get_bitmap(this, temp_img_uri)
            view_model.user_img.value = bitmap
        }
    }


    fun onClicked_changephoto(view: View){
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,temp_img_uri)
        cameraResult.launch(intent)
    }

    override fun onSaveInstanceState(out_state:Bundle){
        super.onSaveInstanceState(out_state)
    }

}