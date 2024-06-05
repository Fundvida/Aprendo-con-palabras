package com.example.fundacion.admin

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.fundacion.BaseActivity
import com.example.fundacion.R

class Atorneo : BaseActivity() {

    private lateinit var newLinear : LinearLayout
    private lateinit var antLinear : LinearLayout
    private lateinit var actLinear : LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atorneo)

        newLinear = findViewById(R.id.view_new)
        actLinear = findViewById(R.id.view_list)
        antLinear = findViewById(R.id.view_ant)


    }

    fun btn_new(view: View){
        newLinear.visibility = View.VISIBLE
        actLinear.visibility = View.GONE
    }

    fun cancel_new(view: View){
        newLinear.visibility = View.GONE
        actLinear.visibility = View.VISIBLE
    }

}
