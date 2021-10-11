package com.example.xuedan_zou_myrun2

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference


class FragmentSetting: PreferenceFragmentCompat(){


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)

        val unit_pref =findPreference<Preference>("unit_preference")!!
        unit_pref.setOnPreferenceClickListener { _ ->
            Unit_Preference_dialogs().show(childFragmentManager, "unit")
            true
        }

        val comment_pref =findPreference<Preference>("comments")!!
        comment_pref.setOnPreferenceClickListener { _ ->
            Comments_Dialogs().show(childFragmentManager, "comment_dialog")
            true
        }
    }
}
