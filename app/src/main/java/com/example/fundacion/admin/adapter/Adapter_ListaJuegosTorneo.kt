package com.example.fundacion.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Juego
import com.example.fundacion.admin.fragment.FTorneo_games
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty


class Adapter_ListaJuegosTorneo(
    private val context : Context,
    private val juegosList: List<Juego>,
    private val refresh: FTorneo_games
) : RecyclerView.Adapter<Adapter_ListaJuegosTorneo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.aaa_adapter_listajuegos_torneo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juego = juegosList[position]
        holder.bind(juego)

        holder.btn.setOnClickListener {

            val postData = """
            {
                "IDTorneoGame": "${config.IDTorneo}",
                "IDGame": "${juego.id}"
            }
        """.trimIndent()

            Fuel.post("${config.url}admin/torneo-game-new")
                .jsonBody(postData)
                .responseString{ _, _, result ->
                    result.fold(
                        success = { d ->

                            Toasty.success(context, "Juego Agregado", Toasty.LENGTH_SHORT).show()
                            refresh.agregar()


                        },
                        failure = {error ->

                            println("Error en la solicitud: $error")
                        }
                    )
                }
        }
    }

    override fun getItemCount(): Int {
        return juegosList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tarea: TextView = itemView.findViewById(R.id.tarea)
        private val tiempo: TextView = itemView.findViewById(R.id.tiempo)
        val btn : Button = itemView.findViewById(R.id.button)

        fun bind(juego: Juego) {
            if (juego != null) {
                tarea.text = juego.tarea
                tiempo.text = juego.tiempo
            }
        }
    }
}
