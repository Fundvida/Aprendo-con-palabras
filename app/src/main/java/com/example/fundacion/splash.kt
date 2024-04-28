package com.example.fundacion

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.util.Log

class splash : BaseActivity() {

    private val splashTimeOut: Long = 3000
    private lateinit var networkListener: NetworkListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //para no estar compilando cada vez la ip de mi maquina sobre la IP
        networkListener = NetworkListener(this)
        networkListener.startListening()

        //esta parte solo es del splash screen
        Handler().postDelayed({
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)

    }

    override fun onDestroy() {
        super.onDestroy()
        networkListener.stopListening()
    }

}