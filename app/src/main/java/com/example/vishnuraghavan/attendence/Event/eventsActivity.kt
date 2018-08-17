package com.example.vishnuraghavan.attendence.Event

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.vishnuraghavan.attendence.MainActivity
import com.example.vishnuraghavan.attendence.R

import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_events.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class eventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        doAsync {
            // accessing sharedPreference for getting the stored token
            val pref = getSharedPreferences("eventToken", 0)
            val token = pref.getString("access_token", "")
            // building the req object
            val req = Request.Builder().url("https://test3.htycoons.in/api/list_events")
                    .header("Authorization", "Bearer $token").post(FormBody.Builder().build()).build()
            // setting up client
            val client = OkHttpClient()
            // making request to server using req object and storing it to res variable
            val res = client.newCall(req).execute()
            uiThread {
                when (res.code()) {
                    200 -> {
                        if (res.body() != null) {
                            val resString = res.body()!!.string()
                            val json = JSONObject(resString)
                            val eventsJson = json.getJSONArray("events")
                            val eventsArray = ArrayList<Event>()

                            for (i in 0 until eventsJson.length()) {
                                val event = Event(eventsJson.getJSONObject(i).getString("_id"),
                                        eventsJson.getJSONObject(i).getString("event_name"),
                                        eventsJson.getJSONObject(i).getString("date").split("T")[0],
                                        eventsJson.getJSONObject(i).getString("venue"),
                                        eventsJson.getJSONObject(i).getInt("days"),
                                        eventsJson.getJSONObject(i).getInt("no_of_participants"))

                                eventsArray.add(event)
                            }
                            // setting up adapter
                            recycle.layoutManager = LinearLayoutManager(this@eventsActivity, LinearLayoutManager.VERTICAL, false)
                            val adapter = EventAdapter(eventsArray)
                            recycle.adapter = adapter
                        }

                    }

                    400 -> {
                        // if 400 manage using alert box
                        AlertDialog.Builder(this@eventsActivity)
                                .setTitle("Bad response")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }

                    404 -> {
                        // if 404 manage using alert box
                        AlertDialog.Builder(this@eventsActivity)
                                .setTitle("No Internet Connection")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mymenu = MenuInflater(this).inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout -> {
                getSharedPreferences("eventToken", 0)
                        .edit().putString("access_token", "").apply()
                // Intents
                startActivity(intentFor<MainActivity>())
                finish() // killing the previous activity
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
