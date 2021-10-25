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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.security.KeyStore
import java.util.concurrent.ArrayBlockingQueue
import kotlin.collections.ArrayList

/*
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instance
import weka.core.Instances
import weka.core.converters.ArffSaver
import weka.core.converters.ConverterUtils.DataSource
*/

class MapService : Service(),LocationListener, SensorEventListener {
    private lateinit var notification_manager: NotificationManager
    val NOTIFICATION_ID = 777
    private lateinit var myBinder: My_Binder
    private val CHANNEL_ID = "notification channel"
    private lateinit var location_manager: LocationManager
    private var message_handler: Handler? = null

    //private var counter = 0
    private lateinit var my_task: MyTask
    private lateinit var timer: Timer

    // used to calculate the distance
    private lateinit var first_location: Location
    private lateinit var location_now_L: Location
    private lateinit var locationl_now: String
    private var starttime: Long = 0L
    private var startaltitude: Double = 0.toDouble()
    private var distance: Float = 0F
    private var speed: Float = 0F
    private var calorie: Float = 0F
    private var input_type: String = " "

    private lateinit var sensorManager: SensorManager
    private var x: Double = 0.0
    private var y: Double = 0.0
    private var z: Double = 0.0

    private lateinit var acc_buffer: ArrayBlockingQueue<Double>
    public val ACCELEROMETER_BUFFER_CAPACITY: Int = 2048
    public val ACCELEROMETER_BLOCK_CAPACITY: Int = 64
    // var latenint AsyncTask: OnSensorChangedTask

    private var counter_stand: Int = 0
    private var counter_walk: Int = 0
    private var counter_run: Int = 0


    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        // tracking the sensor data
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        acc_buffer = ArrayBlockingQueue<Double>(ACCELEROMETER_BUFFER_CAPACITY)

        locationl_now = "0,0"
        my_task = MyTask()
        // control the update rate
        timer = Timer()
        timer.scheduleAtFixedRate(my_task, 0, 1000L)
        // put the icon on the bar
        showNotification()
        myBinder = My_Binder()
        starttime = System.nanoTime()
        message_handler = null
        init_LocationManager()
    }

    // calls everytime startService() is called
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        input_type = intent!!.getStringExtra("input_type")!!
        // the container for attributes
        //var all_attr:ArrayList<Attribute> =
        /*
        if(input_type == "Automatic"){
            AsyncTask = OnSensorChangedTask()
            AsyncTask.execute()
        }
         */
        return START_NOT_STICKY
    }

    inner class My_Binder : Binder() {
        fun set_msg_handler(message_handler: Handler) {
            this@MapService.message_handler = message_handler
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        message_handler = null
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
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

    fun cleanupTasks() {
        notification_manager.cancel(NOTIFICATION_ID)
        /*
        if (timer != null)
            timer.cancel()
        //counter = 0
         */
    }

    inner class MyTask : TimerTask() {
        override fun run() {
            try {

                val temphandler = message_handler
                if (temphandler != null) {
                    val bundle = Bundle()
                    speed = location_now_L.getSpeed() * 5F

                    val altitude = ((location_now_L.getAltitude() - startaltitude) * 0.01).toFloat()
                    val time = (System.nanoTime() - starttime) * 0.000000001F
                    distance = distance + speed * 0.01F
                    val avgspeed = distance / (time * 0.01F)
                    calorie = calorie + 0.01F * speed

                    /*
                    val map_entry = MapEntry()
                    map_entry.altitude = altitude
                    map_entry.avgspeed = avgspeed
                    map_entry.calorie = calorie
                    map_entry.distance = distance
                    map_entry.location = locationl_now
                    map_entry.speed = speed
                    map_entry.time = time

                     */

                    bundle.putString("location", locationl_now)
                    bundle.putFloat("speed", speed)
                    bundle.putFloat("avgspeed", avgspeed)
                    bundle.putFloat("time", time)
                    bundle.putFloat("distance", distance)
                    bundle.putFloat("calorie", calorie)
                    bundle.putFloat("altitude", altitude)

                    // see if we need to read data from sensor
                    if (input_type == "Automatic") {
                        var myfft: FFT = FFT(ACCELEROMETER_BLOCK_CAPACITY)
                        val accBlock = DoubleArray(ACCELEROMETER_BLOCK_CAPACITY)
                        val re: DoubleArray = accBlock
                        val im: DoubleArray = DoubleArray(ACCELEROMETER_BLOCK_CAPACITY)
                        var featvect: DoubleArray = DoubleArray(ACCELEROMETER_BLOCK_CAPACITY+1)
                        var blocksize: Int = 0
                        // get a deep copy of the acc_buffer(we will modify this buffer)
                        //val buffer = acc_buffer.toDoubleArray()

                        while(blocksize < ACCELEROMETER_BLOCK_CAPACITY) {
                            var i: Int = blocksize
                            accBlock[i] = acc_buffer.take()
                            blocksize++
                        }

                        var max_acc:Double = 0.0
                        for(value:Double in accBlock){
                            if(max_acc < value){
                                max_acc = value
                            }
                        }

                        myfft.fft(re, im)

                        for (i in 0..ACCELEROMETER_BLOCK_CAPACITY-1) {
                            val mag: Double = Math.sqrt(re[i] * re[i] + im[i] * im[i])
                            featvect[i] = mag
                            im[i] = .0 // Clear the field
                        }
                        //featvect[ACCELEROMETER_BLOCK_CAPACITY] = accBlock.toList().maxOrNull()!!
                        featvect[ACCELEROMETER_BLOCK_CAPACITY] = max_acc
                        // transfer the type of our data and pass it into the classifier
                        val in_object:Array<Double?> = arrayOfNulls(featvect.size)
                        for(i in featvect.indices){
                            in_object[i] = featvect[i]
                        }
                        var weka_classifier:Double = WekaClassifier.classify(in_object)

                        when(weka_classifier){
                            0.toDouble() -> {
                                counter_stand++
                            }
                            1.toDouble() -> {
                                counter_walk++
                            }
                            2.toDouble() -> {
                                counter_run++
                            }
                        }
                        var max=0
                        var activity_type:Int = 0
                        if(max<counter_stand){
                            activity_type = 0
                            max = counter_stand
                        }
                        if(max<counter_walk){
                            activity_type = 1
                            max = counter_walk
                        }
                        if(max<counter_run){
                            activity_type = 2
                            max = counter_run
                        }
                        bundle.putInt("activity_type", activity_type)
                    }


                    //bundle.putParcelable("new datas", map_entry)
                    //bundle.put

                    val message: Message = temphandler.obtainMessage()
                    message.data = bundle
                    message.what = 0
                    temphandler.sendMessage(message)

                }
            } catch (t: Throwable) {
            }
        }
    }

    fun init_LocationManager() {
        try {
            location_manager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider = location_manager.getBestProvider(criteria, true)
            // get the last time's location
            val location = location_manager.getLastKnownLocation(provider!!)
            if (location != null) {
                onLocationChanged(location)
            }
            first_location = location!!
            startaltitude = first_location.getAltitude()
            // update the location and call onLocationChanged
            location_manager.requestLocationUpdates(provider, 0, 0f, this)

        } catch (e: SecurityException) {
            println("init location manager problem!!!")
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        val pos = lat.toString() + "," + lng.toString()
        location_now_L = location
        locationl_now = pos
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            /*
             x = (event.values[0] / SensorManager.GRAVITY_EARTH).toDouble()
             y = (event.values[1] / SensorManager.GRAVITY_EARTH).toDouble()
             z = (event.values[2] / SensorManager.GRAVITY_EARTH).toDouble()
             */
            var magnitude = Math.sqrt(
                (event.values[0] * event.values[0]
                        + event.values[1] * event.values[1]
                        + event.values[2] * event.values[2]).toDouble()
            )
            try {
                acc_buffer.add(magnitude)
            } catch (e: IllegalStateException) {
                println("error, out of the bound of acc_buffer!!")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
