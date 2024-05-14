package com.example.fundacion.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.FGame_silabas_simples
import com.example.fundacion.admin.lpreguntas
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import es.dmoral.toasty.Toasty

class AdapterPregSilabasSimples(
    private val context: Context,
    private val preguntas: List<lpreguntas>,
    private val refresh: FGame_silabas_simples
): RecyclerView.Adapter<AdapterPregSilabasSimples.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPregSilabasSimples.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_juegos_list_preguntas_silabas_simples, parent, false)
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
            Fuel.delete("${config.url}admin/preg-silabas-simples-delete/${games.id}").responseString{ result ->
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

        private val nombre : TextView = itemView.findViewById(R.id.nombre)
        private val palabra : TextView = itemView.findViewById(R.id.palabra)
        private val puntaje : TextView = itemView.findViewById(R.id.puntaje)
        private val silaba_est : TextView = itemView.findViewById(R.id.silaba_one)
        private val silaba_pre : TextView = itemView.findViewById(R.id.silaba_two)
        private val imageView : ImageView = itemView.findViewById(R.id.image)
        var edit : ImageButton = itemView.findViewById(R.id.edit)
        var delete : ImageButton = itemView.findViewById(R.id.delete)

        fun bind(preg: lpreguntas) {
            if (preg != null){

                nombre.text = preg.img
                palabra.text = preg.palabra
                puntaje.text = preg.puntaje
                silaba_est.text = preg.silaba_estatico
                silaba_pre.text = preg.silaba_pregunta
                Glide.with(itemView.context)
                    .load("${config.url}admin/preg-silabas-simples-imagen/${preg.img}")
                    .into(imageView)
            }
        }
    }



}