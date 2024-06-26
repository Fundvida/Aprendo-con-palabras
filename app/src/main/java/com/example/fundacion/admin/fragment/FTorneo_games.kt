package com.example.fundacion.admin.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.CGame
import com.example.fundacion.admin.Juego
import com.example.fundacion.admin.RefreshTorneoGames
import com.example.fundacion.admin.Refreshtorneo
import com.example.fundacion.admin.Torneo_Game
import com.example.fundacion.admin.adapter.Adapter_ListaJuegosTorneo
import com.example.fundacion.admin.adapter.Adapter_ListaJuegosTorneoEdit
import com.example.fundacion.admin.adapter.Adapter_admin_torneogames
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson


private val games: MutableList<Torneo_Game> = mutableListOf()
private lateinit var rvGames: RecyclerView


class FTorneo_games : Fragment(), RefreshTorneoGames {


    private val gamesList: MutableList<Juego> = mutableListOf()
    private val gamesListNew: MutableList<Juego> = mutableListOf()

    lateinit var spinner : Spinner
    lateinit var spinnerTwo : Spinner

    lateinit var Dspinner : Spinner
    lateinit var DspinnerTwo : Spinner

    lateinit var LinearCreate : LinearLayout
    lateinit var LinearList : LinearLayout

    private lateinit var recyclerViewJuegos: RecyclerView
    private lateinit var DrecyclerViewJuegos: RecyclerView

    override fun refresh() {
        datos()
    }

    override fun agregar(){
        datos()
        LinearCreate.visibility = View.GONE
        LinearList.visibility = View.VISIBLE
        spinner.setSelection(0)
        spinnerTwo.setSelection(0)
    }

    override fun agregarEdit(){
        datos()
        Dspinner.setSelection(0)
        DspinnerTwo.setSelection(0)
        dialog.dismiss()
    }

    fun retro(){
        LinearCreate.visibility = View.GONE
        LinearList.visibility = View.VISIBLE
        spinner.setSelection(0)
        spinnerTwo.setSelection(0)
    }
    fun retroEdit(){
        Dspinner.setSelection(0)
        DspinnerTwo.setSelection(0)
        dialog.dismiss()
    }

    private var refresh: Refreshtorneo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_torneo_games, container, false)


        val retro = view.findViewById<ImageButton>(R.id.retro)
        retro.setOnClickListener { refresh?.retro() }

        val title = view.findViewById<TextView>(R.id.title)
        title.setText(config.NameTorneo)

        rvGames = view.findViewById(R.id.listPreguntas)
        rvGames.layoutManager = LinearLayoutManager(context)


        recyclerViewJuegos = view.findViewById(R.id.lista_juegos)
        recyclerViewJuegos.layoutManager = LinearLayoutManager(context)

        spinner = view.findViewById(R.id.spiner)
        spinnerTwo = view.findViewById(R.id.spinerTwo)

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
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, firt)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val adapterT = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, mutableListOf("Elige un tema"))
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




        LinearCreate = view.findViewById(R.id.view_new)
        LinearList = view.findViewById(R.id.view_list)

        val nuevo : Button = view.findViewById(R.id.nuevo)
        nuevo.setOnClickListener {
            LinearCreate.visibility = View.VISIBLE
            LinearList.visibility = View.GONE

        }


        val cancel : Button = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            retro()
        }

        datos()




        return view
    }


    fun datos(){

        Fuel.get("${config.url}admin/torneo-game-all/${config.IDTorneo}").responseString{ result ->
            result.fold(
                success = {data->

                    val juegos = Gson().fromJson(data, Array<Torneo_Game>::class.java).toList()
                    games.clear()
                    games.addAll(juegos)
                    val adapter = Adapter_admin_torneogames(requireContext(), games, this@FTorneo_games)
                    rvGames.adapter = adapter
                },
                failure = { error ->
                    Log.e("GET TORNEO", "Error: ${error.exception.message}")
                }
            )
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
                    gamesList.clear()
                    gamesList.addAll(ListG)


                    val idsParaFiltrar = games.map { it.tareas }.toSet()

                    val gamesListFiltrada = gamesList.filter { it.id !in idsParaFiltrar }.toMutableList()

                    gamesListNew.clear()
                    gamesListNew.addAll(gamesListFiltrada)



                    val adapter = Adapter_ListaJuegosTorneo(requireContext(), gamesListNew, this@FTorneo_games)
                        recyclerViewJuegos?.adapter = adapter

                },
                failure = { error -> println(error) }
            )
        }



    }

    fun Djuegos(id: String)
    {


        DrecyclerViewJuegos.visibility = View.VISIBLE

        Fuel.get("${config.url}admin/prueba-all-juego/$id").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val ListG = Gson().fromJson(data, Array<Juego>::class.java).toList()
                    gamesList.clear()
                    gamesList.addAll(ListG)


                    val idsParaFiltrar = games.map { it.tareas }.toSet()

                    val gamesListFiltrada = gamesList.filter { it.id !in idsParaFiltrar }.toMutableList()

                    gamesListNew.clear()
                    gamesListNew.addAll(gamesListFiltrada)



                    val adapter = Adapter_ListaJuegosTorneoEdit(requireContext(), gamesListNew, this@FTorneo_games)
                        DrecyclerViewJuegos?.adapter = adapter

                },
                failure = { error -> println(error) }
            )
        }



    }

lateinit var dialog: Dialog

    override fun edit(position: Int){

        Log.e("GET TORNEO", "$position")

        config.IDJuegoTorneo = position.toString()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.aaa_modal_torneo_game_edit)

        val close : ImageButton = dialog.findViewById(R.id.btn_close)
        close.setOnClickListener { retroEdit(); dialog.dismiss() }


        DrecyclerViewJuegos = dialog.findViewById(R.id.lista_juegos)
        DrecyclerViewJuegos.layoutManager = LinearLayoutManager(context)


        Dspinner = dialog.findViewById(R.id.spiner)
        DspinnerTwo = dialog.findViewById(R.id.spinerTwo)

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
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, firt)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Dspinner.adapter = adapter

        val adapterT = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, mutableListOf("Elige un tema"))
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        DspinnerTwo.adapter = adapterT

        Dspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val valorSeleccionado = parent?.getItemAtPosition(position).toString()

                if (valorSeleccionado == "Elige un nivel"){
                    DspinnerTwo.visibility = View.GONE
                    DrecyclerViewJuegos.visibility = View.GONE

                }else{
                    DspinnerTwo.visibility = View.VISIBLE

                    val tareas = gamesList.filter { it.tipo == valorSeleccionado }.firstOrNull()?.temas
                    val temas = tareas?.map { it.tema }?.toMutableList() ?: mutableListOf()
                    temas.add(0, "Elige un tema")
                    adapterT.clear()
                    adapterT.addAll(temas)
                    adapterT.notifyDataSetChanged()
                    DspinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position > 0) {
                                val temaSeleccionado = tareas?.get(position - 1)
                                temaSeleccionado?.let {
                                    idtema = it.id.toString()
                                    Djuegos(idtema.toString())
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






        dialog.show()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Refreshtorneo) {
            refresh = context
        } else {
            throw RuntimeException("$context debe implementar OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        refresh = null
    }


}