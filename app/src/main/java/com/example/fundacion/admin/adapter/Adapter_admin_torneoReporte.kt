package com.example.fundacion.admin.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Est_play
import com.example.fundacion.admin.Estadistica
import com.example.fundacion.admin.Refreshtorneo
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen_fullview
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson

class Adapter_admin_torneoReporte (
    private val context: Context,
    private val estadistica: List<Estadistica>,
    private val refreshableComponent: Refreshtorneo
): RecyclerView.Adapter<Adapter_admin_torneoReporte.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_estadistica_all, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = estadistica[position]
        holder.bind(dato)


        holder.btnPrimero.setOnClickListener{
            estadistica(dato.id.toInt(), dato.torneo[0].id.toInt())
        }
        holder.btnSegundo.setOnClickListener{
            estadistica(dato.id.toInt(), dato.torneo[1].id.toInt())
        }
        holder.btnTercero.setOnClickListener{
            estadistica(dato.id.toInt(), dato.torneo[2].id.toInt())
        }
        holder.btnJugadores.setOnClickListener{
            jugadores_list(position)
        }
        holder.btnTiempo.setOnClickListener{
            time_list(position)
        }

    }
    override fun getItemCount(): Int {
        return estadistica.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtNombre: TextView = itemView.findViewById(R.id.nombre)
        private val txtEstado: TextView = itemView.findViewById(R.id.estado)
        private val txtPrimero: TextView = itemView.findViewById(R.id.primero)
        private val txtSegundo: TextView = itemView.findViewById(R.id.segundo)
        private val txtTercero: TextView = itemView.findViewById(R.id.tercero)

        val btnPrimero: Button = itemView.findViewById(R.id.btn_primero)
        val btnSegundo: Button = itemView.findViewById(R.id.btn_segundo)
        val btnTercero: Button = itemView.findViewById(R.id.btn_tercero)

        val btnTiempo: LinearLayout = itemView.findViewById(R.id.tiempo)
        val btnJugadores: LinearLayout = itemView.findViewById(R.id.jugadores)

        fun bind(dato: Estadistica) {
            if (dato != null){
                txtNombre.text = dato.nombre

                if (dato.estado == "1"){
                    txtEstado.text = "Disponible"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.successColor)
                }
                else if(dato.estado == "2"){
                    txtEstado.text = "terminado"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.errorColor)
                }


                if (dato.torneo.isNotEmpty()) {
                    if (dato.torneo.size > 1) {
                        txtSegundo.text = dato.torneo[1].puntaje+"\n"+dato.torneo[1].nombre+" "+dato.torneo[1].apellido
                    }else{
                        txtSegundo.text = "no hay participante"
                        btnSegundo.visibility = View.GONE
                    }
                    if (dato.torneo.size > 2) {
                        txtTercero.text = dato.torneo[2].puntaje+"\n"+dato.torneo[2].nombre+" "+dato.torneo[2].apellido
                    }else{
                        txtTercero.text = "no hay participante"
                        btnTercero.visibility = View.GONE
                    }
                    txtPrimero.text = dato.torneo[0].puntaje+"\n"+dato.torneo[0].nombre+" "+dato.torneo[0].apellido
                } else {
                    txtPrimero.text = "No Hay participante"
                    txtSegundo.text = "no hay participante"
                    txtTercero.text = "no hay participante"
                    btnPrimero.visibility = View.GONE
                    btnSegundo.visibility = View.GONE
                    btnTercero.visibility = View.GONE
                }




            }
        }
    }

    fun jugadores_list(x: Int) {


        val dato = estadistica[x].torneo


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_estadstica_jugadores)
        val containerLayout: LinearLayout = dialog.findViewById(R.id.container_layout)
        containerLayout.setBackgroundResource(R.color.adminU)


        var list = 0

        for (jugadores in dato) {

            list++


            val textESTA_puesto = TextView(context).apply {
                text = "$list \n lugar"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }


            val textESTA_nombre = TextView(context).apply {
                text = "${jugadores.nombre} ${jugadores.apellido}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }


            val textESTA_puntaje = TextView(context).apply {
                text = "${jugadores.puntaje}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    7F
                )
            }

            val button_jugador =  Button(context).apply {
                id = View.generateViewId()
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    50
                ).apply {
                    weight = 3F
                    setMargins(0,0,10,0)
                }

                text = "ver"
                background = ContextCompat.getDrawable(context, R.drawable.buttonbartrue)
                typeface = ResourcesCompat.getFont(context, R.font.digitalt)

                setOnClickListener {
                    estadistica(estadistica[x].id.toInt(), jugadores.id.toInt())
                }
            }

            val LinearLayoutESTA = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                setBackgroundResource(if (list == 1) R.color.colorPrimerLugar else if(list == 2) R.color.colorSegundoLugar else if (list == 3) R.color.colorTercerLugar else R.color.adminU)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }


            val LinearLayoutESTA0 = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3F
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }

            val LinearLayoutESTA1 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }
            val LinearLayoutESTA2 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }



            val viewH = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
                )
            }
            val viewH1 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    8
                )
            }
            val viewV = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                )
            }

            containerLayout.addView(LinearLayoutESTA)

            LinearLayoutESTA.addView(textESTA_puesto)
            LinearLayoutESTA.addView(viewV)
            LinearLayoutESTA.addView(LinearLayoutESTA0)

            LinearLayoutESTA0.addView(LinearLayoutESTA1)
            LinearLayoutESTA0.addView(viewH)
            LinearLayoutESTA0.addView(LinearLayoutESTA2)

            LinearLayoutESTA1.addView(textESTA_nombre)
            LinearLayoutESTA2.addView(textESTA_puntaje)
            LinearLayoutESTA2.addView(button_jugador)

            containerLayout.addView(viewH1)


        }





        val close = dialog.findViewById<ImageButton>(R.id.btn_close)

        dialog.show()
        configurefullScreen_fullview(dialog)

        close.setOnClickListener { dialog.dismiss() }
    }

    fun time_list(x: Int) {


        val dato = estadistica[x].torneo

        val listaOrdenada = dato.sortedBy { tiempoEnSegundos(it.tiempo) }

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_estadstica_jugadores)
        val containerLayout: LinearLayout = dialog.findViewById(R.id.container_layout)
        containerLayout.setBackgroundResource(R.color.adminU)


        var list = 0

        for (jugadores in listaOrdenada) {

            list++


            val textESTA_puesto = TextView(context).apply {
                text = "$list \n lugar"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }


            val textESTA_nombre = TextView(context).apply {
                text = "${jugadores.nombre} ${jugadores.apellido}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }


            val textESTA_puntaje = TextView(context).apply {
                text = "${jugadores.tiempo}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    7F
                )
            }

            val button_jugador =  Button(context).apply {
                id = View.generateViewId()
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    50
                ).apply {
                    weight = 3F
                    setMargins(0,0,10,0)
                }

                text = "ver"
                background = ContextCompat.getDrawable(context, R.drawable.buttonbartrue)
                typeface = ResourcesCompat.getFont(context, R.font.digitalt)

                setOnClickListener {
                    estadistica(estadistica[x].id.toInt(), jugadores.id.toInt())
                }
            }

            val LinearLayoutESTA = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                setBackgroundResource(if (list == 1) R.color.colorPrimerLugar else if(list == 2) R.color.colorSegundoLugar else if (list == 3) R.color.colorTercerLugar else R.color.adminU)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }


            val LinearLayoutESTA0 = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3F
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }

            val LinearLayoutESTA1 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }
            val LinearLayoutESTA2 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }



            val viewH = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
                )
            }
            val viewH1 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    8
                )
            }
            val viewV = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                )
            }

            containerLayout.addView(LinearLayoutESTA)

            LinearLayoutESTA.addView(textESTA_puesto)
            LinearLayoutESTA.addView(viewV)
            LinearLayoutESTA.addView(LinearLayoutESTA0)

            LinearLayoutESTA0.addView(LinearLayoutESTA1)
            LinearLayoutESTA0.addView(viewH)
            LinearLayoutESTA0.addView(LinearLayoutESTA2)

            LinearLayoutESTA1.addView(textESTA_nombre)
            LinearLayoutESTA2.addView(textESTA_puntaje)
            LinearLayoutESTA2.addView(button_jugador)

            containerLayout.addView(viewH1)


        }


        val close = dialog.findViewById<ImageButton>(R.id.btn_close)

        dialog.show()
        configurefullScreen_fullview(dialog)

        close.setOnClickListener { dialog.dismiss() }
    }


    fun tiempoEnSegundos(tiempo: String): Int {
        val parts = tiempo.split(":")
        val minutos = parts[0].toIntOrNull() ?: 0
        val segundos = parts[1].toIntOrNull() ?: 0
        return minutos * 60 + segundos
    }

    fun estadistica(torneo: Int, user: Int){

        val posData = """
                {
                        "torneo": "$torneo",
                        "estudiante": "$user"
                }
            """.trimIndent()

        Fuel.post("${config.url}admin/estadistica-user")
            .jsonBody(posData)
            .responseString { result ->
            result.fold(
                success = {data ->
                    Log.e("datos", data)
                    val estadisticas = Gson().fromJson(data, Array<Est_play>::class.java).toList()
                    est_user(estadisticas)

                },
                failure = { error -> println(error) }
            )
        }
    }

    fun est_user(x: List<Est_play>){


        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_estadistica_view)

        val maxPuntaje = x.maxOf { it.total }

        dialog.findViewById<TextView>(R.id.nombreCompleto).text = "${x[0].estu.nombres} ${x[0].estu.apellidos}"
        dialog.findViewById<TextView>(R.id.edad).text = "${x[0].estu.edad} años"
        dialog.findViewById<TextView>(R.id.pais).text = "${x[0].estu.pais}"
        dialog.findViewById<TextView>(R.id.ciudad).text = "${x[0].estu.ciudad}"



        val containerLayout: LinearLayout = dialog.findViewById(R.id.container_layout)
        containerLayout.setBackgroundResource(R.color.adminU)

        for (estadistica in x) {

            val textESTA_intentos = TextView(context).apply {
                text = "Intento\n \n${estadistica.intentos}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }
            val textESTA_puntaje = TextView(context).apply {
                text = "Puntaje\n${estadistica.puntaje}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }
            val textESTA_tiempo = TextView(context).apply {
                text = " Tiempo\n${estadistica.tiempo}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }
            val textESTA_correctas = TextView(context).apply {
                text = "Correctas\n${estadistica.correctas}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }
            val textESTA_errores = TextView(context).apply {
                text = "Errores\n${estadistica.erroes}"
                textSize = 20f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }
            val textESTA_total = TextView(context).apply {
                text = "Total\n${estadistica.total}"
                textSize = 22f
                setPadding(10, 8, 10, 8)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(ResourcesCompat.getFont(context, R.font.digitalt))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1F
                )
            }

            val LinearLayoutESTA = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                setBackgroundResource(if (estadistica.total == maxPuntaje) es.dmoral.toasty.R.color.successColor else R.color.adminU)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }
            val LinearLayoutESTA0 = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3F
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }
            val LinearLayoutESTA1 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }
            val LinearLayoutESTA2 = LinearLayout(context).apply {
                LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 5, 0, 5)
                }
            }

            val viewH = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
                )
            }
            val viewH1 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    8
                )
            }
            val viewV = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    )
            }
            val viewV1 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    )
            }
            val viewV2 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    )
            }
            val viewV3 = View(context).apply {
                setBackgroundResource(R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    2,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    )
            }



            containerLayout.addView(LinearLayoutESTA)

            LinearLayoutESTA.addView(textESTA_intentos)
            LinearLayoutESTA.addView(viewV)

            LinearLayoutESTA.addView(LinearLayoutESTA0)

            LinearLayoutESTA0.addView(LinearLayoutESTA1)
            LinearLayoutESTA0.addView(viewH)
            LinearLayoutESTA0.addView(LinearLayoutESTA2)


            LinearLayoutESTA1.addView(textESTA_puntaje)
            LinearLayoutESTA1.addView(viewV1)
            LinearLayoutESTA1.addView(textESTA_tiempo)
            LinearLayoutESTA1.addView(viewV2)
            LinearLayoutESTA1.addView(textESTA_errores)


            //LinearLayoutESTA.addView(viewH)
           // LinearLayoutESTA.addView(LinearLayoutESTA2)






            LinearLayoutESTA2.addView(textESTA_correctas)
            LinearLayoutESTA2.addView(viewV3)
            LinearLayoutESTA2.addView(textESTA_total)
            containerLayout.addView(viewH1)


        }


        val close = dialog.findViewById<ImageButton>(R.id.btn_close)

        dialog.show()
        configurefullScreen_fullview(dialog)

        close.setOnClickListener { dialog.dismiss() }

    }



}