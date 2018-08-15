package com.example.vishnuraghavan.attendence.dashboard.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.list_fragment.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = context!!.getSharedPreferences("reg",0)
        val regID = pref.getString("reg_id","")

        doAsync {

            val reqBody = FormBody.Builder()
                    .add("event_id", arguments!!.getString("eventID"))
                    .add("reg_id", regID)
                    .build()

            val req = Request.Builder().url("https://test3.htycoons.in/api/get_attendence")
                    .header("Authorization", "Bearer ${arguments!!.getString("token")}")
                    .post(reqBody).build()

            val client = OkHttpClient()

            val res = client.newCall(req).execute()

            if (res.body() != null) {
                val resString = res.body().toString()
                val json = JSONObject(resString)
                val resJson = json.getJSONArray("attended_dates")

                Log.d("jsonRes","$resJson")

                val attendArray = ArrayList<AttendDates>()

                for (i in 0 until resJson.length()) {
                    val result = AttendDates(resJson.getJSONObject(i).getString("date"))
                    attendArray.add(result)
                }

                uiThread {

                    when (res.code()) {

                        200 -> {
                            attendRecycle.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
                            val adapter = AttendenceAdapter(attendArray)
                            attendRecycle.adapter = adapter
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