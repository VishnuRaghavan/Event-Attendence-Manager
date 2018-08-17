package com.example.vishnuraghavan.attendence

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        name.text = intent.getStringExtra("name")
        email.text = intent.getStringExtra("email")
        phone.text = intent.getStringExtra("phone")
        org.text = intent.getStringExtra("org")
        reg.text = intent.getStringExtra("regId")

    }
}
