package com.example.xuedan_zou_myrun2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class FragmentHistory:Fragment(){

    private lateinit var history_listview: ListView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_history, container, false)
        history_listview = view.findViewById(R.id.History_ListView)

        return view
    }

}

