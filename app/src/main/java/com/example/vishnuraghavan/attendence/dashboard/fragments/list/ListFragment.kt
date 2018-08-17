package com.example.vishnuraghavan.attendence.dashboard.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.list_fragment.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doAsync {

            val reqBody = FormBody.Builder()
                    .add("event_id", arguments!!.getString("eventID"))
                    .build()

            val req = Request.Builder().url("https://test3.htycoons.in/api/list_participants")
                    .header("Authorization", "Bearer ${arguments!!.getString("token")}")
                    .post(reqBody).build()

            val client = OkHttpClient()

            val res = client.newCall(req).execute()

            if (res.body() != null) {
                val resString = res.body().toString()
                val json = JSONObject(resString)
                val resJson = json.getJSONArray("members")

                val memberArray = ArrayList<Member>()

                for (i in 0..resJson.length()-1) {
                    val result = Member(
                            resJson.getJSONObject(i).getString("name"),
                            resJson.getJSONObject(i).getString("phone_number"),
                            resJson.getJSONObject(i).getString("email_address"),
                            resJson.getJSONObject(i).getString("organization"),
                            resJson.getJSONObject(i).getString("reg_id"))

                    memberArray.add(result)
                }

                uiThread {

                    memberRecycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    val adapter = MemberAdapter(memberArray)
                    memberRecycle.adapter = adapter

                    when (res.code()) {

                        200 -> {

                        }

                        400 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("Bad response")
                                    .setNeutralButton("OK") { dialog, which ->
                                        dialog.dismiss()
                                    }.show()

                        }

                        404 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("No Internet Connection")
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