package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // displaying data to its textviews
        nameContent.text = arguments!!.getString("name")
        daysContent.text = arguments!!.getInt("days").toString()
        dateContent.text = arguments!!.getString("date")
        venueContent.text = arguments!!.getString("venue")

    }

}