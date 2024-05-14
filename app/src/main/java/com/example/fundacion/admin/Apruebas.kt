package com.example.fundacion.admin

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.NetworkListener
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.Adapter_ListaJuegos
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

class Apruebas : BaseActivity() {

    private val games: MutableList<Juego> = mutableListOf()

    private lateinit var recyclerViewJuegos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apruebas)

        recyclerViewJuegos = findViewById(R.id.lista_juegos)
        recyclerViewJuegos.layoutManager = LinearLayoutManager(this)



        val spinner = findViewById<Spinner>(R.id.spiner)
        val spinnerTwo = findViewById<Spinner>(R.id.spinerTwo)

        val select = """
            [
                {
                    "id": 1,
                    "tipo": "BASICO",
                    "estado": 1,
                    "temas": [
                        {
                            "id": 1,
                            "nivel": 1,
                            "tema": "LAS VOCALES",
                            "estado": 1
                        },
                        {
                            "id": 2,
                            "nivel": 1,
                            "tema": "EL ABECEDARIO",
                            "estado": 1
                        },
                        {
                            "id": 3,
                            "nivel": 1,
                            "tema": "LAS SILABAS SIMPLES",
                            "estado": 1
                        },
                        {
                            "id": 4,
                            "nivel": 1,
                            "tema": "EL DELETREO DE PALABRAS HASTA TRES SILABAS",
                            "estado": 1
                        },
                        {
                            "id": 5,
                            "nivel": 1,
                            "tema": "SOPA DE LETRAS DE ALIMENTOS, MUEBLES DE LA CASA, CIUDADES DE BOLIVIA",
                            "estado": 1
                        },
                        {
                            "id": 6,
                            "nivel": 1,
                            "tema": "CONSTRUIR ORACIONES CORTAS ORDENANDO PALABRAS",
                            "estado": 1
                        }
                    ]
                },
                {
                    "id": 2,
                    "tipo": "INTERMEDIO",
                    "estado": 1,
                    "temas": [
                        {
                            "id": 7,
                            "nivel": 2,
                            "tema": "CONSTRUIR ORACIONES ORDENANDO PALABRAS",
                            "estado": 1
                        },
                        {
                            "id": 8,
                            "nivel": 2,
                            "tema": "SINONIMOS",
                            "estado": 1
                        },
                        {
                            "id": 9,
                            "nivel": 2,
                            "tema": "ANTONIMOS",
                            "estado": 1
                        },
                        {
                            "id": 10,
                            "nivel": 2,
                            "tema": "SILABAS COMPUESTAS, TRABADAS, E INVERTIDAS",
                            "estado": 1
                        },
                        {
                            "id": 11,
                            "nivel": 2,
                            "tema": "DELETREO DE PALABRAS DE MAS DE TRES SILABAS",
                            "estado": 1
                        },
                        {
                            "id": 12,
                            "nivel": 2,
                            "tema": "CUENTOS CORTOS CON PREGUNTAS DE COMPRENSION",
                            "estado": 1
                        },
                        {
                            "id": 13,
                            "nivel": 2,
                            "tema": "SOPA DE PALABRAS",
                            "estado": 1
                        }
                    ]
                },
                {
                    "id": 3,
                    "tipo": "AVANZADO",
                    "estado": 1,
                    "temas": [
                        {
                            "id": 14,
                            "nivel": 3,
                            "tema": "PALABRAS GRAVES, LLANAS, ESDRUJULAS, Y AGUDAS",
                            "estado": 1
                        },
                        {
                            "id": 15,
                            "nivel": 3,
                            "tema": "COMPOSION DE ORACIONES PARA IDENTIFICAR SUJETO, PREDICADO Y VERBO",
                            "estado": 1
                        },
                        {
                            "id": 16,
                            "nivel": 3,
                            "tema": "CUENTO Y SUS PARTES (PLANTEAMIENTO, BUDO Y DESENLACE)",
                            "estado": 1
                        },
                        {
                            "id": 17,
                            "nivel": 3,
                            "tema": "REDACCION DE UNA CARTA",
                            "estado": 1
                        },
                        {
                            "id": 18,
                            "nivel": 3,
                            "tema": "CRUCIGRAMA",
                            "estado": 1
                        },
                        {
                            "id": 19,
                            "nivel": 3,
                            "tema": "SOPA DE LETRAS",
                            "estado": 1
                        }
                    ]
                }
            ]
        """.trimIndent()
        var idtema: String? = null
        val gamesList = Gson().fromJson(select, Array<CGame>::class.java).toList()

        val firt = gamesList.map { it.tipo }.toMutableList()
        firt.add(0, "Elige un nivel")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, firt)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val adapterT = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mutableListOf("Elige un tema"))
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTwo.adapter = adapterT

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val valorSeleccionado = parent?.getItemAtPosition(position).toString()

                if (valorSeleccionado == "Elige un nivel"){
                    spinnerTwo.visibility = View.GONE
                    recyclerViewJuegos.visibility = View.GONE

                }else{
                    spinnerTwo.visibility = View.VISIBLE

                    val tareas = gamesList.filter { it.tipo == valorSeleccionado }.firstOrNull()?.temas
                    val temas = tareas?.map { it.tema }?.toMutableList() ?: mutableListOf()
                    temas.add(0, "Elige un tema")
                    adapterT.clear()
                    adapterT.addAll(temas)
                    adapterT.notifyDataSetChanged()
                    spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position > 0) {
                                val temaSeleccionado = tareas?.get(position - 1)
                                temaSeleccionado?.let {
                                    idtema = it.id.toString()

                                    juegos(idtema.toString())
                                    println("===========$idtema")
                                }
                            }else{
                                idtema = null
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



    }

    fun juegos(id: String)
    {
        recyclerViewJuegos.visibility = View.VISIBLE

        Fuel.get("${config.url}admin/prueba-all-juego/$id").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val ListG = Gson().fromJson(data, Array<Juego>::class.java).toList()
                    games.clear()
                    games.addAll(ListG)

                    runOnUiThread{
                        val adapter = Adapter_ListaJuegos(this, games)
                        recyclerViewJuegos?.adapter = adapter
                    }

                },
                failure = { error -> println(error) }
            )
        }


    }





    fun inicio(view: View){
        val intent = Intent(this, Ainicio::class.java)
        startActivity(intent)
    }

}