package com.example.xuedan_zou_myrun2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapService : Service(){
    private lateinit var notification_manager: NotificationManager
    val NOTIFICATION_ID = 777
    private lateinit var  myBinder: MyBinder
    private val CHANNEL_ID = "notification channel"

    private var message_handler: Handler? = null
    companion object{
        val INT_KEY = "int key"
        val MSG_INT_VALUE = 0
    }
    //private var counter = 0
    private lateinit var my_task: MyTask
    private lateinit var timer: Timer

    override fun onCreate() {
        super.onCreate()
        my_task = MyTask()
        //timer = Timer()
        //timer.scheduleAtFixedRate(my_task, 0, 1000L)
        // put the icon on the bar
        showNotification()
        //ymyBinder = MyBinder()
        message_handler = null
    }

    // calls everytime startService() is called
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun set_msg_handler(message_handler: Handler) {
            this@MapService.message_handler = message_handler
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        message_handler = null
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        println("debug: Service onDestroy")
        cleanupTasks()
    }

    private fun showNotification() {
        // set the notification
        val intent = Intent(this, MapActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, CHANNEL_ID
        )
        notificationBuilder.setSmallIcon(R.drawable.dartmouth)
        notificationBuilder.setContentTitle("MyRuns")
        notificationBuilder.setContentText("Recording your path now")
        notificationBuilder.setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()

        notification_manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, "channel name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notification_manager.createNotificationChannel(notificationChannel)
        }
        notification_manager.notify(NOTIFICATION_ID, notification)
    }

    // when app removed from the application list
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        cleanupTasks()
        stopSelf()
    }

    fun cleanupTasks(){
        notification_manager.cancel(NOTIFICATION_ID)
        /*
        if (timer != null)
            timer.cancel()
        //counter = 0
         */
    }

    inner class MyTask : TimerTask() {
        override fun run() {


           /* try {

                counter += 1
                println("debug: counter: $counter")

                val tempHamdler = message_handler //see the reason why we need a local val in this situation https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha

                if (tempHamdler != null) {
                    val bundle = Bundle()
                    bundle.putInt(INT_KEY, counter)
                    val message: Message = tempHamdler.obtainMessage()
                    message.data = bundle
                    message.what = MSG_INT_VALUE
                    tempHamdler.sendMessage(message)


                }
            } catch (t: Throwable) { // you should always ultimately catch all // exceptions in timer tasks.
                println("debug: Timer Tick Failed. $t")
            }*/
        }
    }
}


