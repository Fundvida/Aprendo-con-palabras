package com.example.fundacion

import android.content.Intent
import android.os.Bundle
import android.os.Handler

class splash : BaseActivity() {

    private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        Handler().postDelayed({
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)

    }
}