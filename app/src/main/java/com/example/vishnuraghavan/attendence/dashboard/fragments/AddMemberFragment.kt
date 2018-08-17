package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import com.example.vishnuraghavan.attendence.dashboard.dashboardActivity
import kotlinx.android.synthetic.main.add_member_fragment.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import org.jetbrains.anko.toast
import okhttp3.Request
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddMemberFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_member_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button2.setOnClickListener() {

            if (name.text.isNotEmpty()
                    || phone.text.isNotEmpty()
                    || email.text.isNotEmpty()
                    || organization.text.isNotEmpty()
                    || reg.text.isNotEmpty()) {

                sentRequestToServer()

            } else {
                AlertDialog.Builder(context!!)
                        .setTitle("Incomplete Form")
                        .setMessage("Fill the form first and try again !!")
                        .setNeutralButton("ok") { dialog, which ->
                            dialog.cancel()
                        }
                        .show()
            }

        }
    }

    fun sentRequestToServer() {
        doAsync {

            val reqBody = FormBody.Builder()
                    .add("event_id", arguments!!.getString("eventID"))
                    .add("name", name.text.toString())
                    .add("phone_number", phone.text.toString())
                    .add("email_address", email.text.toString())
                    .add("organization", organization.text.toString())
                    .add("reg_id", reg.text.toString()).build()


            val req = Request.Builder().url("https://test3.htycoons.in/api/add_participant")
                    .header("Authorization", "Bearer ${arguments!!.getString("token")}")
                    .post(reqBody).build()

            val client = OkHttpClient()

            val res = client.newCall(req).execute()

            uiThread {

                when (res.code()) {
                    200 -> {
                        if (res.body() != null) {
                            AlertDialog.Builder(context!!)
                                    .setTitle("Success!")
                                    .setMessage("Details added successfully!")
                                    .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                        }
                    }

                    400 -> {
                        AlertDialog.Builder(context!!)
                                .setTitle("Error")
                                .setMessage("An error has occured!")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }

                    404 -> {
                        AlertDialog.Builder(context!!)
                                .setTitle("Server Error")
                                .setMessage("Internal server error!")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }
                } // when clause
            }  // ui thread

        } // do async
    }

}