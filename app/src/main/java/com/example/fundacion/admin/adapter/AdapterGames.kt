package com.example.fundacion.admin.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.CGame
import com.example.fundacion.admin.RefreshGame
import com.example.fundacion.admin.fragment.FGame_abecedario
import com.example.fundacion.admin.fragment.FGame_contruir_oraciones_simple
import com.example.fundacion.admin.fragment.FGame_deletreo_simple
import com.example.fundacion.admin.fragment.FGame_silabas_simples
import com.example.fundacion.admin.fragment.FGame_sopa_letras_simple
import com.example.fundacion.admin.fragment.FGame_vocales
import com.example.fundacion.admin.lgames
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

class AdapterGames(
    private val context: Context,
    private val userList: List<lgames>,
    private val refresh: RefreshGame
): RecyclerView.Adapter<AdapterGames.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_juegos_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val games = userList[position]
        holder.bind(games)

        holder.btn_ver.setOnClickListener {
            //refresh.verlist(Admin_fragment_game_list())
            config.GameTarea =   games.tarea
            config.IDGameTarea = games.id
            //if (games.tema.tema == "")
            when (games.tema.id){
                1 -> {
                    refresh.verlist(FGame_vocales())
                }
                2 -> {
                    refresh.verlist(FGame_abecedario())
                }
                3 -> {
                    refresh.verlist(FGame_silabas_simples())
                }
                4 -> {
                    refresh.verlist(FGame_deletreo_simple())
                }
                5 -> {
                    refresh.verlist(FGame_sopa_letras_simple())
                }
                6 -> {
                    refresh.verlist(FGame_contruir_oraciones_simple())
                }
            }
        }

        holder.btn_edit.setOnClickListener {
            holder.itemView.post{
                modal_edit(holder.adapterPosition)
            }
        }

        holder.btn_delete.setOnClickListener {
            println(games.id)
            Fuel.delete("${config.url}admin/game-delete/${games.id}"
            ).responseString{result ->
                result.fold(
                    success = {d ->

                        Toasty.error(context, "TArea Eliminada", Toasty.LENGTH_SHORT).show()
                        refresh.refresh()
                    },
                    failure = {e ->
                        println("Error en la solicitud : $e")
                    }
                )

            }
        }
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nivel: TextView = itemView.findViewById(R.id.nivel)
        private val tema: TextView = itemView.findViewById(R.id.tema)
        private val tarea: TextView = itemView.findViewById(R.id.tarea)
        private val tiempo: TextView = itemView.findViewById(R.id.tiempo)
        val btn_ver: ImageButton = itemView.findViewById(R.id.eyes)
        val btn_edit: ImageButton = itemView.findViewById(R.id.edit)
        val btn_delete: ImageButton = itemView.findViewById(R.id.delete)

        fun bind(games: lgames) {
            if (games != null){

                nivel.text = games.tema.nivel.tipo
                tema.text = games.tema.tema
                tarea.text = games.tarea
                tiempo.text = games.tiempo

            }
        }
    }

    var idtema : String? = null
    fun modal_edit(position: Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_game_edit)
        val spinner = dialog.findViewById<Spinner>(R.id.spiner)
        val spinnerTwo = dialog.findViewById<Spinner>(R.id.spinerTwo)
        val btnaceptar = dialog.findViewById<Button>(R.id.btn_aceptar)
        val tarea = dialog.findViewById<EditText>(R.id.tarea)
        val time = dialog.findViewById<EditText>(R.id.time)

        val btncancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val btnclose = dialog.findViewById<ImageButton>(R.id.btn_close)

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
        val gamesList = Gson().fromJson(select, Array<CGame>::class.java).toList()

        val firt = gamesList.map { it.tipo }.toMutableList()
        firt.add(0, "Elige un nivel")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, firt)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val adapterT = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf("Elige un tema"))
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTwo.adapter = adapterT




        val datos = userList[position]



        val SelectPosition = firt.indexOf(datos.tema.nivel.tipo)
        spinner.setSelection(SelectPosition)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val valorSeleccionado = parent?.getItemAtPosition(position).toString()

                val tareas = gamesList.filter { it.tipo == valorSeleccionado }.firstOrNull()?.temas
                val temas = tareas?.map { it.tema }?.toMutableList() ?: mutableListOf()
                temas.add(0, "Elige un tema")


                adapterT.clear()
                adapterT.addAll(temas)
                adapterT.notifyDataSetChanged()

                val selectTema = temas.indexOf(datos.tema.tema)
                if (selectTema != -1) {
                    spinnerTwo.setSelection(selectTema)
                }

                spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position > 0) {
                            val temaSeleccionado = tareas?.get(position - 1)
                            temaSeleccionado?.let {
                                idtema = it.id.toString()
                            }
                        }else{
                            idtema = null
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejo cuando no se selecciona nada
            }
        }


        tarea.setText(datos.tarea)
        time.setText(datos.tiempo)

        btnaceptar.setOnClickListener {
            println(datos.id)

            val postdata = """
                 {
                     "tema": "$idtema",
                     "tarea": "${tarea.text}",
                     "tiempo": "${time.text}"
                 }
            """.trimIndent()
            println(postdata)
            println("${config.url}admin/game-edit/"+datos.id)

            Fuel.put("${config.url}admin/game-edit/"+datos.id)
                .jsonBody(postdata)
                .responseString{_,_, result ->
                    result.fold(
                        success = { data->
                            Toasty.success(context, "Actualizado Correctamente", Toasty.LENGTH_SHORT).show()
                            refresh.refresh()
                            dialog.dismiss()

                        },
                        failure = { error -> println(error) }
                    )

                }
        }

        btnclose.setOnClickListener { dialog.dismiss() }
        btncancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }




}