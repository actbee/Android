package com.example.xuedan_zou_myrun2

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class FragmentSetting: PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
    }

    /*
    override fun onCreatePreference(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }
    */
}
