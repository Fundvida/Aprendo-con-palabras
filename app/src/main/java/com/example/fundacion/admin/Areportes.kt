package com.example.fundacion.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.Adapter_admin_torneoReporte
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson


private lateinit var rv: RecyclerView
private val estadistica: MutableList<Estadistica> = mutableListOf()

class Areportes : BaseActivity(), Refreshtorneo {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areportes)

        rv = findViewById(R.id.list)
        rv.layoutManager = LinearLayoutManager(this)


        cargar_datos()

    }


    private fun cargar_datos(){


        Fuel.get("${config.url}admin/estadistica-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userList = Gson().fromJson(data, Array<Estadistica>::class.java).toList()
                    estadistica.clear()
                    estadistica.addAll(userList)

                    runOnUiThread{
                        val aadapter = Adapter_admin_torneoReporte(this, estadistica, this@Areportes)
                        rv?.adapter = aadapter
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }

    override fun refresha() {
        TODO("Not yet implemented")
    }

    override fun verlist(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    override fun retro() {
        TODO("Not yet implemented")
    }


}