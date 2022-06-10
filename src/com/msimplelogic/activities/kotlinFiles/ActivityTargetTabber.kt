package com.msimplelogic.activities.kotlinFiles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.activity_target.*
import kotlinx.android.synthetic.main.content_tabbertarget.*

class ActivityTargetTabber : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        val adapter = ViewPagerAdapter(supportFragmentManager)
//        adapter.addFragment(MindOrksFragment(), "MindOrks")
//        adapter.addFragment(GetMeAnAppFragment(), "GetMeAnApp")
//        adapter.addFragment(BestContentAppFragment(), "BestContentApp")
//        viewPager.adapter = adapter
//        tabs.setupWithViewPager(viewPager)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }


}