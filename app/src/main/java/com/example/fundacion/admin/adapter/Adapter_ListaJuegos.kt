package com.example.fundacion.admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Juego
import com.example.fundacion.admin.game.PruebaJuego_abecedario
import com.example.fundacion.admin.game.PruebaJuego_construir_oraciones_simples
import com.example.fundacion.admin.game.PruebaJuego_silabasimple
import com.example.fundacion.admin.game.PruebaJuego_sopaletras_basico
import com.example.fundacion.admin.game.PruebaJuego_vocal
import com.example.fundacion.config

class Adapter_ListaJuegos(
    private val context : Context,
    private val juegosList: List<Juego>
    ) : RecyclerView.Adapter<Adapter_ListaJuegos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.aaa_adapter_listajuegos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juego = juegosList[position]
        holder.bind(juego)

        holder.btn.setOnClickListener {
            val tema = juego.tema.toInt()
            when(tema){
                //vocales
                1 -> {
                    val intent = Intent(context, PruebaJuego_vocal::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                //abecedario
                2 ->{
                    val intent = Intent(context, PruebaJuego_abecedario::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                //silabas simples
                3 -> {
                    val intent = Intent(context, PruebaJuego_silabasimple::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                //deletreo de palabras
                4 -> {
                    val intent = Intent(context, PruebaJuego_silabasimple::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                //sopa de letras
                5 -> {
                    val intent = Intent(context, PruebaJuego_sopaletras_basico::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                //construir oraciones
                6 -> {
                    val intent = Intent(context, PruebaJuego_construir_oraciones_simples::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return juegosList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tarea: TextView = itemView.findViewById(R.id.tarea)
        private val tiempo: TextView = itemView.findViewById(R.id.tiempo)
        val btn :Button = itemView.findViewById(R.id.button)

        fun bind(juego: Juego) {
            if (juego != null) {
                tarea.text = juego.tarea
                tiempo.text = juego.tiempo
            }
        }
    }
}
