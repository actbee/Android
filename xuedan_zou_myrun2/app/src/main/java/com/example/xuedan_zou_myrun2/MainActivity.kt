package com.example.xuedan_zou_myrun2

import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import java.util.*
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {

    lateinit var my_spinner:Spinner

    private val START="START"
    private val HISTORY="HISTORY"
    private val SETTING="SETTINGS"

    private lateinit var frag_start: FragmentStart
    private lateinit var frag_history: FragmentHistory
    private lateinit var frag_setting: FragmentSetting
    private lateinit var viewpager2: ViewPager2   //ViewPager2 here is used to switch between fragments
    private lateinit var tab_layout: TabLayout
    private lateinit var fragment_state_adapter:FragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tab_strategy:TabConfigurationStrategy
    private lateinit var tab_mediator:TabLayoutMediator
    private lateinit var selected:String

    private val tab_name=arrayOf(START, HISTORY, SETTING)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager2 = findViewById(R.id.viewpager)
        tab_layout = findViewById(R.id.tab)

        frag_start = FragmentStart()
        frag_history = FragmentHistory()
        frag_setting = FragmentSetting()

        fragments = ArrayList()
        fragments.add(frag_start)
        fragments.add(frag_history)
        fragments.add(frag_setting)

        fragment_state_adapter = FragmentStateAdapter(fragments,this)
        viewpager2.adapter = fragment_state_adapter

        // tab_strategy set the title of each tab
        tab_strategy = TabConfigurationStrategy { tab: TabLayout.Tab, pos: Int ->
            tab.text = tab_name[pos]
        }

        // tab_mediator is used to link tab_layout and viewpager2 so they synchronize together
        tab_mediator = TabLayoutMediator(tab_layout, viewpager2, tab_strategy)
        tab_mediator.attach()

        /*   to get the spinner's value part but something wrong
        my_spinner=findViewById(R.id.spinner)
        val my_adapter=ArrayAdapter.createFromResource(
            this, R.array.input_entries,android.R.layout.simple_spinner_item
        )
        my_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        my_spinner.adapter=my_adapter
         */
    }

    override fun onDestroy(){
        super.onDestroy()
        tab_mediator.detach()
    }

    fun StartClicked(view:View?){
        /*
        if(selected=="Manual Entry"){
            print("gettt!")
        }*/
        val intent=Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    fun SYNCClicked(view:View?){
        val intent=Intent(this, ManualEntryActivity::class.java)
        startActivity(intent)
    }

}