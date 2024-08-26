package com.example.fundacion.user.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.user.ESTUDdatos
import com.example.fundacion.user.ETorneoEstudiante
import com.example.fundacion.user.torneo_estud.Pre_torneo_estud

class TorneoAdapter (
    private val context: Context,
    private val torneo: List<ETorneoEstudiante>
    //private val refreshableComponent: Refreshtorneo
): RecyclerView.Adapter<TorneoAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.uuu_adapter_listgame, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = torneo[position]
        holder.bind(dato)


        holder.Jugar_btn.setOnClickListener {
            Log.e("select torneo", dato.torneo.id)
            val intent = Intent(context, Pre_torneo_estud::class.java)
            intent.putExtra("TORNEO_ID", dato.torneo.id)
            intent.putExtra("TORNEO_NOMBRE", dato.torneo.nombre)
            ESTUDdatos.GameTime = (dato.torneo.tiempo.toInt()*60) * 1000L
            ESTUDdatos.GameTimeGlobal = (dato.torneo.tiempo.toInt()*60) * 1000L
            ESTUDdatos.GameIntentos = dato.torneo.intentos.toInt()
            ESTUDdatos.GameFechaInicio = dato.torneo.fecha_inicio
            ESTUDdatos.GameFechaFin = dato.torneo.fecha_fin
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return torneo.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val text : TextView = itemView.findViewById(R.id.tareaTextView)
        var Jugar_btn : ImageButton = itemView.findViewById(R.id.jugar)

        fun bind(dato: ETorneoEstudiante) {
            if (dato != null){

                text.setText(dato.torneo.nombre)

            }
        }
    }



}