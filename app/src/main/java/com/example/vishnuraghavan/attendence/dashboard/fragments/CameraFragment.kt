package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.vishnuraghavan.attendence.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class CameraFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    var token = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         token = arguments!!.getString("token")
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        codeScanner = CodeScanner(activity, scannerView)
                        codeScanner.decodeCallback = DecodeCallback {
                            activity.runOnUiThread {
                                val regID = it.text.toString()
                                context!!.toast("msg: $regID")
                                if(regID != ""){
                                    sentRequestToServer(regID)
                                }

                            }
                        }

                        scannerView.setOnClickListener {
                            codeScanner.startPreview()
                        }
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {

                    }

                })
                .check()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

     fun sentRequestToServer(regID: String) {
            doAsync {

                val reqBody = FormBody.Builder()
                        .add("event_id", arguments!!.getString("eventID"))
                        .add("reg_id", regID)
                        .add("date", arguments!!.getString("date")).build()

                val req = Request.Builder().url("https://test3.htycoons.in/api/daily_register")
                        .header("Authorization", "Bearer $token")
                        .post(reqBody).build()

                val client = OkHttpClient()

                val res = client.newCall(req).execute()

                Log.d("token","token : $token")
                uiThread {
                    when (res.code()) {
                        200 -> {

                            AlertDialog.Builder(context!!)
                                    .setTitle("Registered Successfully")
                                    .setNeutralButton("OK"){ dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                        }

                        400 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("ERROR - Bad server response")
                                    .setNeutralButton("OK"){ dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                        }

                        404 -> {
                            AlertDialog.Builder(context!!)
                                    .setTitle("ERROR 404")
                                    .setNeutralButton("OK"){ dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                        }
                    }
                }
            }
    }
}