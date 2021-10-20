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
/*
class MapService: Service() {
    inner class MyBinder : Binder() {
        fun setmsgHandler(msgHandler: Handler) {
            this@MapService.handler = msgHandler
        }
    }
    private lateinit var notification_manager: NotificationManager
    val NOTIFICATION_ID = 777
    private lateinit var  myBinder: MyBinder
    private val CHANNEL_ID = "notification channel"
    private var handler: Handler? = null
    companion object{
        val INT_KEY = "int key"
        val MSG_INT_VALUE = 0
    }
    private var counter = 0
    private lateinit var myTask: MyTask
    private lateinit var timer: Timer
    inner class MyTask : TimerTask() {
        override fun run() {
            try {
                counter += 1
                println("debug: counter: $counter")

                val tempHamdler = handler //see the reason why we need a local val in this situation https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha

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
            }
        }
    }
}

 */

