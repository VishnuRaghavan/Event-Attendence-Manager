package com.example.vishnuraghavan.attendence.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.support.v7.app.AppCompatActivity
import com.example.vishnuraghavan.attendence.R
import com.example.vishnuraghavan.attendence.dashboard.fragments.AddMemberFragment
import com.example.vishnuraghavan.attendence.dashboard.fragments.CameraFragment
import com.example.vishnuraghavan.attendence.dashboard.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_dashboard.*


class dashboardActivity : AppCompatActivity() {

    val bundle = Bundle()



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.homeItem -> {
                val homeFragment = HomeFragment()
                homeFragment.arguments = bundle
                addFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.addPersonItem -> {
                val addMemberFragment = AddMemberFragment()
                addMemberFragment.arguments = bundle
                addFragment(addMemberFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.scanItem -> {
                val qrFragment = CameraFragment()
                qrFragment.arguments = bundle
                addFragment(qrFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.listItem -> {
                val listFragment = ListFragment()
                listFragment.arguments = bundle
                addFragment(listFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val pref = getSharedPreferences("eventToken",0)
        val token = pref.getString("access_token", "")

        bundle.putString("eventID", intent.getStringExtra("event_id"))
        bundle.putString("name", intent.getStringExtra("name"))
        bundle.putString("days", intent.getStringExtra("days"))
        bundle.putString("date", intent.getStringExtra("date"))
        bundle.putString("venue", intent.getStringExtra("venue"))
        bundle.putString("token", token.toString())


        val homeFragment = HomeFragment()
        homeFragment.arguments = bundle
        addFragment(homeFragment)
    }


    fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.base_layout, fragment).commit()
    }
}
