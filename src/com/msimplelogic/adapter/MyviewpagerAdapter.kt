package com.msimplelogic.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.msimplelogic.activities.ViewPagerFragments.Checkout1
import com.msimplelogic.activities.ViewPagerFragments.Checkout2
import com.msimplelogic.activities.ViewPagerFragments.Checkout3

@SuppressLint("WrongConstant")
class MyviewpagerAdapter (private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                Checkout1()
            }
            1 -> {
                Checkout2()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                Checkout3()
            }
            else -> null!!
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}