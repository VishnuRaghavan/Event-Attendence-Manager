package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
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

            // creating request body
            val reqBody = FormBody.Builder()
                    .add("event_id", arguments!!.getString("eventID"))
                    .build()
            // creating request using the above request body
            val req = Request.Builder().url("https://test3.htycoons.in/api/list_participants")
                    .header("Authorization", "Bearer ${arguments!!.getString("token")}")
                    .post(reqBody).build()
            // preparing the client
            val client = OkHttpClient()
            //using client to sent the request
            val res = client.newCall(req).execute()

            uiThread {
                // checking the response codes and dealing with the responses
                when (res.code()) {
                // if 200 read the response and add to adapter
                    200 -> {
                        if (res.body() != null) {
                            val resString = res.body()!!.string() // taking response body and setting to string
                            val json = JSONObject(resString)  // making JSON object
                            val resJson = json.getJSONArray("events") // fetching JSON array

                            val memberArray = ArrayList<Member>()

                            for (i in 0 until resJson.length()) {
                                val result = Member(
                                        resJson.getJSONObject(i).getString("name"),
                                        resJson.getJSONObject(i).getString("phone_number"),
                                        resJson.getJSONObject(i).getString("email_address"),
                                        resJson.getJSONObject(i).getString("organization"),
                                        resJson.getJSONObject(i).getString("reg_id"))

                                memberArray.add(result)
                            } // looping through the JSON array and adding to sample array

                            memberRecycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            val adapter = MemberAdapter(memberArray)
                            memberRecycle.adapter = adapter  // setting up adapter for recycler view
                        }

                    }

                    400 -> {
                        // if 400 manage using alert box
                        AlertDialog.Builder(context!!)
                                .setTitle("Bad response")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()

                    }

                    404 -> {
                        // if 404 manage using alert box
                        AlertDialog.Builder(context!!)
                                .setTitle("No Internet Connection")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }

                }


            }


        }


    }


}