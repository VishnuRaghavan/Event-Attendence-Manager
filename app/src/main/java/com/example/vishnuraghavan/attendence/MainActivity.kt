package com.example.vishnuraghavan.attendence

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.vishnuraghavan.attendence.Event.eventsActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = getSharedPreferences("eventToken", 0)

        if (pref.getString("access_token", "") != "") {
            startActivity(intentFor<eventsActivity>())
            finish()
        }

    }

    fun login(v: View) {
        if (username.text.toString().isEmpty() || pass.text.toString().isEmpty())
            toast("Invalid username or password")
        else {
            userLogin()
        }
    }

    fun userLogin() {

        progressBar.visibility = View.VISIBLE

        doAsync {
            val body = FormBody.Builder()
                    .add("username", username.text.toString())
                    .add("password", pass.text.toString()).build()

            val req = Request.Builder().url("https://test3.htycoons.in/api/login").post(body).build()

            val client = OkHttpClient()
            val res = client.newCall(req).execute()

            uiThread {
                when (res.code()) {
                    200 -> {
                        if (res.body() != null) {
                            val jsonRes = JSONObject(res.body()!!.string())
                            val token = jsonRes.getString("access_token")

                            val pref = getSharedPreferences("eventToken", 0)

                            pref.edit().putString("access_token", token).apply()

                            startActivity(intentFor<eventsActivity>())
                            finish()
                        }
                    }

                    400 -> {
                        AlertDialog.Builder(this@MainActivity)
                                .setTitle("Error")
                                .setMessage("An error is occured!")
                                .setNeutralButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                }.show()
                    }

                    404 -> {

                    }
                }
            }
        }
    }
}
