package com.example.fundacion.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.FGame_deletreo_simple
import com.example.fundacion.admin.lpreguntas
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import es.dmoral.toasty.Toasty

class AdapterPregDeletreoSimple(
    private val context: Context,
    private val preguntas: List<lpreguntas>,
    private val refresh: FGame_deletreo_simple
): RecyclerView.Adapter<AdapterPregDeletreoSimple.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPregDeletreoSimple.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_juego_list_preguntas_deletro_simple, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val games = preguntas[position]
        holder.bind(games)

        holder.edit.setOnClickListener {
            holder.itemView.post{
                refresh.modalEditSilabas(position)
            }
        }

        holder.delete.setOnClickListener {
            Fuel.delete("${config.url}admin/preg-deletreo-simples-delete/${games.id}").responseString{ result ->
                result.fold(
                    success = {data->
                        Toasty.error(context, "Pregunta Eliminada", Toasty.LENGTH_SHORT).show()
                        refresh.refresh()
                    },
                    failure = { error ->
                        Log.e("Upload", "Error: ${error.exception.message}")
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val palabra : TextView = itemView.findViewById(R.id.palabra)
        private val puntaje : TextView = itemView.findViewById(R.id.puntaje)
        var edit : ImageButton = itemView.findViewById(R.id.edit)
        var delete : ImageButton = itemView.findViewById(R.id.delete)

        fun bind(preg: lpreguntas) {
            if (preg != null){

                palabra.text = preg.palabra
                puntaje.text = preg.puntaje
            }
        }
    }




}

