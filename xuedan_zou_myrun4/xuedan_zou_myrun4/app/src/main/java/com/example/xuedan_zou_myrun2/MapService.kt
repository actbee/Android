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

class MapService : Service(){
    private lateinit var notificationManager: NotificationManager
    val NOTIFICATION_ID = 777
    private lateinit var  myBinder: MyBinder
    private val CHANNEL_ID = "notification channel"

    private var msgHandler: Handler? = null
    companion object{
        val INT_KEY = "int key"
        val MSG_INT_VALUE = 0
    }
    private var counter = 0
    private lateinit var myTask: MyTask
    private lateinit var timer: Timer

    override fun onCreate() {
        super.onCreate()
        Log.d("xdyang", "Service onCreate() called")
        myTask = MyTask()
        timer = Timer()
        timer.scheduleAtFixedRate(myTask, 0, 1000L)
        showNotification()
        myBinder = MyBinder()
        msgHandler = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("debug: Service onStartCommand() called everytime startService() is called; startId: $startId flags: $flags")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("debug: Service onBind() called")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun setmsgHandler(msgHandler: Handler) {
            this@MapService.msgHandler = msgHandler
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("debug: Service onUnBind() called~~~")
        msgHandler = null
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        println("debug: Service onDestroy")
        cleanupTasks()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        println("debug: app removed from the application list")
        cleanupTasks()
        stopSelf()
    }

    fun cleanupTasks(){
        notificationManager.cancel(NOTIFICATION_ID)
        if (timer != null)
            timer.cancel()
        counter = 0
    }

    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        ) //XD: see book p1019 why we do not use Notification.Builder
        notificationBuilder.setSmallIcon(R.drawable.dartmouth)
        notificationBuilder.setContentTitle("Service has started")
        notificationBuilder.setContentText("Tap me to go back")
        notificationBuilder.setContentIntent(pendingIntent)
        val notification = notificationBuilder.build()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "channel name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    inner class MyTask : TimerTask() {
        override fun run() {
            try {
                counter += 1
                println("debug: counter: $counter")

                val tempHamdler = msgHandler //see the reason why we need a local val in this situation https://stackoverflow.com/questions/44595529/smart-cast-to-type-is-impossible-because-variable-is-a-mutable-property-tha

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


