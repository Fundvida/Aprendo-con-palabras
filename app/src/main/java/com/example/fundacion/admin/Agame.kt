package com.example.fundacion.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.AdapterGames
import com.example.fundacion.admin.adapter.AdapterUsuario

import com.example.fundacion.admin.fragment.prueba
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

private lateinit var rvGame: RecyclerView
private val games: MutableList<lgames> = mutableListOf()

class Agame : BaseActivity() {

    private lateinit var list: LinearLayout
    private lateinit var new: LinearLayout
    private lateinit var ant: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agame)

        rvGame = findViewById(R.id.listusuarios)
        rvGame.layoutManager = LinearLayoutManager(this)
        cargar_datos()


        list = findViewById(R.id.view_list)
        new = findViewById(R.id.view_new)
        ant = findViewById(R.id.view_ant)

        /*val fragment = prueba()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmen_prueba, fragment)
            .commit()
*/
    }

    private fun cargar_datos(){


        Fuel.get("${config.url}admin/game-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userList = Gson().fromJson(data, Array<lgames>::class.java).toList()
                    games.clear()
                    games.addAll(userList)

                    runOnUiThread{
                        val aadapter = AdapterGames(this, games)
                        rvGame?.adapter = aadapter
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }

    fun btn_new(view: View){

        ant.visibility = View.GONE
        new.visibility = View.VISIBLE
        list.visibility = View.GONE

    }
}