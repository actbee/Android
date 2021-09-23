package com.example.xuedan_zou_myrun1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object Util {
    fun ask_permissions(activity: Activity?) {

        if (Build.VERSION.SDK_INT < 23){  //too early edition
            return
        }

        if ((ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 0)
        }           //  ask for the permission
    }


    fun get_bitmap(context: Context, img_uri: Uri): Bitmap {    //to transfer an image to something easily saved
        var bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(img_uri))
        val matrix = Matrix()
        matrix.setRotate(90f)
        var result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return result
    }

    fun store_image(saved_image:File, bitmap:Bitmap){
        val stream:OutputStream=FileOutputStream(saved_image)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        stream.flush()
        stream.close()

    }

}