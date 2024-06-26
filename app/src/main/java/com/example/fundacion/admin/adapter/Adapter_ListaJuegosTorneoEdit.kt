package com.example.fundacion.admin.adapter

import android.content.Context
import android.util.Log
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


class Adapter_ListaJuegosTorneoEdit(
    private val context : Context,
    private val juegosList: List<Juego>,
    private val refresh: FTorneo_games
) : RecyclerView.Adapter<Adapter_ListaJuegosTorneoEdit.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.aaa_adapter_listajuegos_torneo_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juego = juegosList[position]
        holder.bind(juego)

        holder.btn.setOnClickListener {

            Log.e("GET TORNEO", "${juego.id}")


            val postData = """
            {
                "IDTorneoGame": "${config.IDJuegoTorneo}",
                "IDGame": "${juego.id}"
            }
        """.trimIndent()

            Fuel.post("${config.url}admin/torneo-game-edit")
                .jsonBody(postData)
                .responseString{ _, _, result ->
                    result.fold(
                        success = { d ->

                            Toasty.success(context, "Juego Editado", Toasty.LENGTH_SHORT).show()
                            refresh.agregarEdit()


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
