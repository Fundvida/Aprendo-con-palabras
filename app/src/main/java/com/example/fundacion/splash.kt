package com.example.fundacion

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView

class splash : BaseActivity() {

    private lateinit var loadingTextView: TextView
    private var loadingProgress = 0
    private val handler = Handler(Looper.getMainLooper())


    private lateinit var networkListener: NetworkListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        networkListener = NetworkListener(this)
        networkListener.startListening()


        loadingTextView = findViewById(R.id.loadingTextView)

        Thread {
            while (loadingProgress < 8) {
                loadingProgress++
                handler.post {
                    updateLoadingText()
                }
                Thread.sleep(500)
            }
            handler.post {
                loadingTextView.textSize = 30f
                loadingTextView.text = ""

                 val intent = Intent(this, login::class.java)
                 startActivity(intent)
                 finish()
            }
        }.start()
    }

    private fun updateLoadingText() {
        val loadingText = "".plus(".".repeat(loadingProgress % 4 + 1))
        loadingTextView.text = loadingText
    }

    override fun onDestroy() {
        super.onDestroy()
        networkListener.stopListening()
    }
}