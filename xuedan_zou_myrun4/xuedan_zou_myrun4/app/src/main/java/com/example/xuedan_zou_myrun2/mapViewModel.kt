package com.example.xuedan_zou_myrun2

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class mapViewModel: ViewModel(), ServiceConnection {
    private lateinit var my_MessageHandler: MyMessageHandler
    init {
        my_MessageHandler = MyMessageHandler(Looper.getMainLooper())
    }
    // MutableLiveData means you can change its value
    private val position = MutableLiveData<Int>()
    //val counter: LiveData<Int>
        get() = position


    override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
        val temp_binder = iBinder as MapService.MyBinder
        temp_binder.set_msg_handler(my_MessageHandler)
    }


    inner class MyMessageHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(message: Message) {
            if (message.what == MapService.MSG_INT_VALUE) {
                val bundle = message.data
                position.value = bundle.getInt(MapService.INT_KEY)
            }
        }
    }
}

