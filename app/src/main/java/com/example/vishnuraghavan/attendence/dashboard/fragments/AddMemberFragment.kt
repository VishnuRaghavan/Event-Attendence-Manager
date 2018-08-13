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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddMemberFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_member_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button2.setOnClickListener(){

            val token = arguments!!.getString("token")
//            Log.d("token", "token  is : $token")

            doAsync {

                val reqBody = FormBody.Builder()
                        .add("reg_id", reg.text.toString())
                        .add("name", name.text.toString())
                        .add("phone_number", phone.text.toString())
                        .add("email_address", email.text.toString())
                        .add("organization", organization.text.toString()).build()


                val req = Request.Builder().url("https://test3.htycoons.in/api/add_participant")
                        .header("Authorization", "Bearer $token")
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
                                        .setNeutralButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                        }.show()
                            }
                        }

                        400 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("Error")
                                    .setMessage("An error has occured!")
                                    .setNeutralButton("OK") { dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                        }

                        404 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("Server Error")
                                    .setMessage("Internal server error!")
                                    .setNeutralButton("OK") { dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                        }
                    }
                }


            }
        }

    }

}