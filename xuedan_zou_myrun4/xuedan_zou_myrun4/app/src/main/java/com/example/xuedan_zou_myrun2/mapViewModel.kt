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

class mapViewModel: ViewModel(), ServiceConnection {
    private var my_MessageHandler: MyMessageHandler = MyMessageHandler(Looper.getMainLooper())

    // MutableLiveData means you can change its value
     private val positions = MutableLiveData<String>()
     val position: LiveData<String>
        get() = positions


    override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
        val temp_binder = iBinder as MapService.My_Binder
        temp_binder.set_msg_handler(my_MessageHandler)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        println("disconnected")
    }


    inner class MyMessageHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(message: Message) {
            if (message.what == 0) {
                val bundle = message.data
                positions.value = bundle.getString("new_location")

            }
        }
    }
}

