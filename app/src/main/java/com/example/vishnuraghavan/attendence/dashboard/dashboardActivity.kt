package com.example.vishnuraghavan.attendence.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.vishnuraghavan.attendence.R
import com.example.vishnuraghavan.attendence.dashboard.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_dashboard.*


class dashboardActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.homeItem -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.addPersonItem -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.scanItem -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.listItem -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val homeFragment = HomeFragment()

        addFragment(homeFragment)
    }


    fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.base_layout, fragment).commit()
    }
}
