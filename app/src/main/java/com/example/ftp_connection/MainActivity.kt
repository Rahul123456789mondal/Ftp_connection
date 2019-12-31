package com.example.ftp_connection

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() , View.OnClickListener {

    private val TAG = "MainActivity"

    private lateinit var cntx: Context
    private lateinit var btnLoginFtp: Button
    private lateinit var btnDownloadFtp : Button
    private lateinit var btnDisconnectFtp: Button
    private lateinit var btnExit: Button

    private lateinit var edtHostName: EditText
    private lateinit var edtUserName: EditText
    private lateinit var edtPassword: EditText

    private var ftpclient: MyFTPClientFunctions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cntx = this.baseContext
        edtHostName = findViewById(R.id.edtHostName)
        edtUserName = findViewById(R.id.edtUserName)
        edtPassword = findViewById(R.id.edtPassword)

        btnLoginFtp = findViewById(R.id.btnLoginFtp)
        btnDownloadFtp = findViewById(R.id.btnDownloadFtp)
        btnDisconnectFtp = findViewById(R.id.btnDisconnectFtp)
        btnExit = findViewById(R.id.btnExit)

        btnLoginFtp.setOnClickListener(this)
        btnDownloadFtp.setOnClickListener(this)
        btnDisconnectFtp.setOnClickListener(this)
        btnExit.setOnClickListener(this)
        ftpclient = MyFTPClientFunctions()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLoginFtp -> if (isOnline(this@MainActivity)) {
                Toast.makeText(this@MainActivity, "Internet Is Connected", Toast.LENGTH_SHORT).show()
                connectToFTPAddress()
            } else {
                Toast.makeText(this@MainActivity, "Please check your internet connection!", Toast.LENGTH_LONG).show()
            }
            R.id.btnDownloadFtp -> {
                Thread(Runnable {
                    run{
                      var  check = ftpclient!!.ftpDownload( "/", Environment.getExternalStorageState()+"/Download")
                        if (check == true){
                            Log.d(TAG, "Download Success")
                            Toast.makeText(this@MainActivity, "Download Successfully", Toast.LENGTH_LONG).show()
                        }else{
                            Log.d(TAG, "Download Failed")
                            Toast.makeText(this@MainActivity, "Download Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }).start()
            }
            R.id.btnDisconnectFtp -> {
                Thread(Runnable {
                    ftpclient?.ftpDisconnect()
                }).start()
            }
            R.id.btnExit -> finish()
        }
    }

    private fun connectToFTPAddress() {
        val host = edtHostName.text.toString().trim()
        val username = edtUserName.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (host.length < 1 ){
            Toast.makeText(this@MainActivity, "Please Enter Host Address!", Toast.LENGTH_LONG).show()
        }else if (username.length < 1){
            Toast.makeText(this@MainActivity, "Please Enter User Name!", Toast.LENGTH_LONG).show()
        }else if (password.length < 1){
            Toast.makeText(this@MainActivity, "Please Enter Password!", Toast.LENGTH_LONG).show()
        }else{
            Thread(Runnable {
                var status : Boolean
                status = ftpclient!!.ftpConnect(host, username, password, 21)
                if (status == true) {
                    Log.d(TAG, "Connection Success")
                        //handler.sendEmptyMessage(0)
                } else {
                    Log.d(TAG, "Connection failed")
                        //handler.sendEmptyMessage(-1)
                }
            }).start()
        }

    }

    private fun isOnline(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo
        if ( info!= null && info.isConnected){
            return true
        }
        return false
    }


}