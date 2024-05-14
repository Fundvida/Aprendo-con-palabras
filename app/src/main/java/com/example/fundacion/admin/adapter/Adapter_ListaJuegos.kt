package com.example.fundacion.admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.admin.Juego
import com.example.fundacion.admin.game.PruebaJuego_abecedario
import com.example.fundacion.admin.game.PruebaJuego_silabasimple
import com.example.fundacion.admin.game.PruebaJuego_vocal
import com.example.fundacion.config
import es.dmoral.toasty.Toasty

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
                1 -> {
                    val intent = Intent(context, PruebaJuego_vocal::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                2 ->{
                    val intent = Intent(context, PruebaJuego_abecedario::class.java)
                    context.startActivity(intent)
                    config.IDJuegoPrueba = juego.id
                    config.NAMEJuegoPrueba = juego.tarea
                }
                3 -> {
                    val intent = Intent(context, PruebaJuego_silabasimple::class.java)
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
