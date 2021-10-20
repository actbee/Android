package com.example.xuedan_zou_myrun2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class FragmentStateAdapter(var list: ArrayList<Fragment>, activity: FragmentActivity)
    : FragmentStateAdapter(activity){

      override fun createFragment(pos: Int): Fragment{
          return list[pos]
      }

      override fun getItemCount():Int{
          return list.size
      }

    }