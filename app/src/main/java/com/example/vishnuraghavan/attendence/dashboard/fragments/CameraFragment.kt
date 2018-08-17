package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
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
import kotlinx.android.synthetic.main.add_member_fragment.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class CameraFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner // defining the codescanner variable as CodeScanner type !

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view) // finding the scanner id
        val activity = requireActivity() // getting activity

        // dexter thirdparty library

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CAMERA) // setting up camera dynamic permission
                .withListener(object : PermissionListener {   // permission object having its methods
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {  // method called if permission granted
                        codeScanner = CodeScanner(activity, scannerView)
                        codeScanner.decodeCallback = DecodeCallback { // decoding the QR pattern
                            activity.runOnUiThread {  // firing up ui thread for displaying it in ui
                                val regID = it.text.toString()
//                                context!!.toast("msg: $regID")
                                if (regID != "") { sentRequestToServer(regID) }
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

    override fun onResume() {  // method called  on resume
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {  // method called on pause
        codeScanner.releaseResources()
        super.onPause()
    }

    fun sentRequestToServer(regID: String) {  // custom function written to organise  server codes
        doAsync {

            val reqBody = FormBody.Builder() // building the request body
                    .add("event_id", arguments!!.getString("eventID"))
                    .add("reg_id", regID)
                    .add("date", arguments!!.getString("date")).build()

            val req = Request.Builder().url("https://test3.htycoons.in/api/daily_register") // building request object using request body
                    .header("Authorization", "Bearer ${arguments!!.getString("token")}")
                    .post(reqBody).build()

            val client = OkHttpClient() // initialising the client

            val res = client.newCall(req).execute() // setting up the response

            uiThread {
                when (res.code()) {
                    // checking the res code
                    200 -> {  // if 200 display success
                        AlertDialog.Builder(context!!)
                                .setTitle("Registered Successfully")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()

                    }

                    400 -> { // if 400 display error response
                        AlertDialog.Builder(context!!)
                                .setTitle("ERROR - Bad server response")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }

                    404 -> { // if 404 display 404 error
                        AlertDialog.Builder(context!!)
                                .setTitle("ERROR 404")
                                .setNeutralButton("OK") { dialog, which -> dialog.dismiss() }.show()
                    }
                }
            }
        }
    }
}