package com.example.fundacion.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.fundacion.BaseActivity
import com.example.fundacion.R

class Ueligirnivel : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ueligirnivel)
    }

    fun facil(view: View){
        val intent = Intent(this, inicio::class.java)
        startActivity(intent)
    }
}