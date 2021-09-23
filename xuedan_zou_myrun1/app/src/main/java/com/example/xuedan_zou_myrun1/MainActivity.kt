package com.example.xuedan_zou_myrun1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RadioButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.OutputStream
import java.io.FileOutputStream
import android.view.View
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import android.graphics.Bitmap




class MainActivity : AppCompatActivity() {

    private lateinit var  temp_img_uri: Uri
    private lateinit var  saved_img_uri: Uri
    private lateinit var  view_model: ViewModel
    private lateinit var  image_view: ImageView
    private lateinit var  name_view: EditText
    private lateinit var  email_view: EditText
    private lateinit var  phone_view: EditText
    private lateinit var  class_view: EditText
    private lateinit var  major_view: EditText
    private lateinit var  gender_view: RadioGroup
    private lateinit var  saved_image: File
    private lateinit var  temp_image: File

    private val NAME="name"
    private val EMAIL="email"
    private val PHONE="phone"
    private val CLASS="class"
    private val MAJOR="major"
    private val GENDER="gender"
    private val temp_img_name= "my_run.jpg"
    private val saved_img_name="my_photo.jpg"

    private val sharedPrefFile="kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)     //build the basic project

        Util.ask_permissions(this)    //check permissions

        image_view = findViewById(R.id.Image)     //get widgets through id
        name_view=findViewById(R.id.Name)
        email_view=findViewById(R.id.Email)
        phone_view=findViewById(R.id.Phone)
        class_view=findViewById(R.id.Class)
        major_view=findViewById(R.id.Major)
        gender_view=findViewById(R.id.Gender)

        val SharedPreference :SharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)  //to save data

       view_model = ViewModelProvider(this).get(ViewModel::class.java)
        view_model.user_img.observe(this, {        //in the ViewModel to observe any change to the image
                it -> image_view.setImageBitmap(it)
        })


        temp_image = File(getExternalFilesDir(null), temp_img_name)     //to get the image stored in the phone by this app
        saved_image=File(getExternalFilesDir(null),saved_img_name)
        temp_img_uri = FileProvider.getUriForFile(this, "com.example.xuedan_zou_myrun1", temp_image)
        saved_img_uri=FileProvider.getUriForFile(this, "com.example.xuedan_zou_myrun1", saved_image)
      //  println("debug: address"+temp_img_uri)

        loadProfile()
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

    fun onSaveClicked(view :View){
        saveProfile()
        Toast.makeText(this@MainActivity,"SAVED!",Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onCancleClicked(view: View){
        finish()
    }


    override fun onSaveInstanceState(out_state:Bundle){
        println("debug:save!")
        super.onSaveInstanceState(out_state)
    }

    fun saveProfile() {
        val SharedPreference: SharedPreferences =getSharedPreferences(sharedPrefFile,MODE_PRIVATE)

        val getname: String = name_view.getText().toString()
        val getemail: String = email_view.getText().toString()
        val getphone: String = phone_view.getText().toString()
        val getclass: String = class_view.getText().toString()
        val getmajor: String = major_view.getText().toString()
        val getgender: Int=gender_view.checkedRadioButtonId

        val editor: SharedPreferences.Editor = SharedPreference.edit()
        editor.clear()
        editor.putString(NAME, getname)
        editor.putString(EMAIL, getemail)
        editor.putString(PHONE, getphone)
        editor.putString(MAJOR, getmajor)
        editor.putString(CLASS, getclass)
        editor.putInt(GENDER,getgender)
        /*
        if(getclass.isNullOrEmpty()==false){
            editor.putInt(CLASS, getclass.toInt() )
        }
        */
        editor.commit()
        println("debug: store image")
        val bitmap = Util.get_bitmap(this, temp_img_uri)
        Util.store_image(saved_image,bitmap)

    }

    fun loadProfile(){
        val SharedPreference=this.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        name_view.setText(SharedPreference.getString(NAME,""))
        email_view.setText(SharedPreference.getString(EMAIL,""))
        phone_view.setText(SharedPreference.getString(PHONE,""))
        major_view.setText(SharedPreference.getString(MAJOR,""))
        class_view.setText(SharedPreference.getString(CLASS,""))

        val gender:Int=SharedPreference.getInt(GENDER,-1)
        println("debug: gender is"+gender)
        val clicked_button:RadioButton?=findViewById(gender)
        if(clicked_button!=null){
            clicked_button.isChecked=true
        }

        if(saved_image.exists()) {
            println("debug:it exists!")
            val bitmap = Util.get_bitmap(this, saved_img_uri)
            image_view.setImageBitmap(bitmap)
        }

        //  class_view.setText(SharedPreference.getInt(CLASS,-1))
    }

}