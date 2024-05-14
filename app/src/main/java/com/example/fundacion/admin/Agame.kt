package com.example.fundacion.admin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.AdapterGames

import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

private lateinit var rvGame: RecyclerView
private val games: MutableList<lgames> = mutableListOf()

class Agame : BaseActivity(), RefreshGame {

    private lateinit var list: LinearLayout
    private lateinit var new: LinearLayout
    private lateinit var ant: LinearLayout
    private lateinit var fragment: LinearLayout

    private lateinit var spinner: Spinner
    private lateinit var array: Array<ArrayNivel>
    private lateinit var select: ArrayNivel

    private lateinit var spinnerTwo: Spinner
    private val arraytwo: MutableList<ArrayTema> = mutableListOf()
    private lateinit var selectwo: ArrayTema


    override fun refresh(){
        cargar_datos()
        /*cargar_ant()
        editTextSearch.setText("")
        txtbuscador.setText("")*/
        //Toasty.success(this, "funcioando refresh", Toasty.LENGTH_SHORT).show()


    }
    override fun verlist(fragmento: Fragment){

        ant.visibility = View.GONE
        list.visibility =View.GONE
        new . visibility = View.GONE
        fragment.visibility = View.VISIBLE

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragmento)
        transaction.commit()
    }

    override fun retro() {
        ant.visibility = View.GONE
        list.visibility =View.VISIBLE
        new . visibility = View.GONE
        fragment.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agame)

        spinner = findViewById(R.id.spiner)
        spinnerTwo = findViewById(R.id.spinerTwo)
        datos()

        rvGame = findViewById(R.id.listusuarios)
        rvGame.layoutManager = LinearLayoutManager(this)
        cargar_datos()


        list = findViewById(R.id.view_list)
        new = findViewById(R.id.view_new)
        ant = findViewById(R.id.view_ant)
        fragment = findViewById(R.id.fragment)



        val btnnew = findViewById<Button>(R.id.aceptar_new)
        val tarea = findViewById<EditText>(R.id.tarea)
        val time = findViewById<EditText>(R.id.time)

        btnnew.setOnClickListener {
            val posData = """
                {
                        "tema": "${selectwo.id}",
                        "tarea": "${tarea.text}",
                        "tiempo": "${time.text}"
                        
                }
            """.trimIndent()


            Fuel.post("${config.url}admin/game-create")
                .jsonBody(posData)
                .responseString{_,_,result ->
                result.fold(
                    success = {data->
                        Toasty.success(this, "Tarea Agregada", Toasty.LENGTH_SHORT).show()
                        new.visibility = View.GONE
                        list.visibility  = View.VISIBLE
                        cargar_datos()
                    },
                    failure = { error -> println(error) }
                )
            }
        }
    }
    private fun datos(){

        Fuel.get("${config.url}admin/game-all-nivel").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    array = Gson().fromJson(data, Array<ArrayNivel>::class.java)
                    val tipos = array.map { it.tipo }.toMutableList()
                    tipos.add(0, "Elige un nivel")
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter


                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                select = array[position - 1]
                                datosTwo(select)
                            }
                            else{
                                spinnerTwo.visibility = View.GONE
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Manejar la situación en la que no se selecciona nada
                        }
                    }

                },
                failure = { error -> println(error) }
            )
        }

        Fuel.get("${config.url}admin/game-all-tema").responseString { _, _, result ->
            result.fold(
                success = {data ->

                    val userList = Gson().fromJson(data, Array<ArrayTema>::class.java).toList()
                    arraytwo.clear()
                    arraytwo.addAll(userList)
                },
                failure = { error -> println(error) }
            )
        }

    }

    private fun datosTwo(array: ArrayNivel){
        spinnerTwo.visibility = View.VISIBLE

        val valor = array.id
        val two = mutableListOf<ArrayTema>()
        for (item in arraytwo) {
            if (item.nivel == valor){
                two.add(item)
            }
        }

        val tipos = two.map { it.tema }.toMutableList()
        tipos.add(0, "Elige un tema")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTwo.adapter = adapter

        spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    selectwo = two[position - 1]
                    println(selectwo)
                }
                else{
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar la situación en la que no se selecciona nada
            }
        }

    }

    private fun cargar_datos(){


        Fuel.get("${config.url}admin/game-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val gamelist = Gson().fromJson(data, Array<lgames>::class.java).toList()
                    games.clear()
                    games.addAll(gamelist)

                    runOnUiThread{
                        val aadapter = AdapterGames(this, games, this@Agame)
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
    fun cancel(view: View){
        ant.visibility = View.GONE
        new.visibility = View.GONE
        list.visibility = View.VISIBLE
    }
}