package com.example.fundacion.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.user.ETorneo_PreGame

class Torneo_pregame_Adapter (
    private val context: Context,
    private val torneo: List<ETorneo_PreGame>
    //private val refreshableComponent: Refreshtorneo
    ) : RecyclerView.Adapter<Torneo_pregame_Adapter.ViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.uuu_adapter_torneo_estadistica, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = torneo[position]
        holder.bind(dato)




    }
    override fun getItemCount(): Int {
        return torneo.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val intento : TextView = itemView.findViewById(R.id.intento)
        private val puntaje : TextView = itemView.findViewById(R.id.puntaje)
        private val errores : TextView = itemView.findViewById(R.id.errores)
        private val correctas : TextView = itemView.findViewById(R.id.correctas)
        private val tiempo : TextView = itemView.findViewById(R.id.tiempo)
        private val bonus : TextView = itemView.findViewById(R.id.bonus)
        private val total : TextView = itemView.findViewById(R.id.total)


        fun bind(dato: ETorneo_PreGame) {
            if (dato != null){

                val bonnus = dato.total.toInt() - dato.puntaje.toInt()
                intento.setText(dato.intentos)
                puntaje.setText(dato.puntaje)
                errores.setText(dato.erroes)
                correctas.setText(dato.correctas)
                tiempo.setText(dato.tiempo)
                bonus.setText(bonnus.toString())
                total.setText(dato.total)


            }
        }
    }


}
