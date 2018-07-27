package com.example.vishnuraghavan.attendence.Event

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

            val pref = getSharedPreferences("eventToken", 0)
            val token = pref.getString("access_token", "")

            val req = Request.Builder().url("https://test3.htycoons.in/api/list_events")
                    .header("Authorization", "Bearer $token").post(FormBody.Builder().build()).build()

            val client = OkHttpClient()
            val res = client.newCall(req).execute()

            if (res.body() != null) {
                val resString = res.body()!!.string()
                val json = JSONObject(resString)
                val eventsJson =json.getJSONArray("events")
                val eventsArray = ArrayList<Event>()

                for(i in 0..eventsJson.length() - 1){
                  val event = Event(eventsJson.getJSONObject(i).getString("_id"),
                          eventsJson.getJSONObject(i).getString("event_name"),
                          eventsJson.getJSONObject(i).getString("date").split("T")[0],
                          eventsJson.getJSONObject(i).getString("venue"),
                          eventsJson.getJSONObject(i).getInt("days"),
                          eventsJson.getJSONObject(i).getInt("no_of_participants"))

                    eventsArray.add(event)
                }

                uiThread {
                    recycle.layoutManager = LinearLayoutManager(this@eventsActivity,LinearLayoutManager.VERTICAL,false)

                    val adapter = EventAdapter(eventsArray)

                    recycle.adapter = adapter
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

                startActivity(intentFor<MainActivity>())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
