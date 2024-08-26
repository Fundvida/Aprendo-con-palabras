package com.example.fundacion.user.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.user.ETorneo_Game
class Torneo_pre_Adapter  (
    private val context: Context,
    private val torneo: List<ETorneo_Game>
    //private val refreshableComponent: Refreshtorneo
): RecyclerView.Adapter<Torneo_pre_Adapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.uuu_adapter_list_torneo_pre, parent, false)
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

        private val tipo : TextView = itemView.findViewById(R.id.tipo)
        private val puntos : TextView = itemView.findViewById(R.id.puntos)
        private val preguntas : TextView = itemView.findViewById(R.id.preguntas)
        private val tema : TextView = itemView.findViewById(R.id.tema)
        private val img : ImageView = itemView.findViewById(R.id.img)
       // var Jugar_btn : ImageButton = itemView.findViewById(R.id.jugar)

        fun bind(dato: ETorneo_Game) {
            if (dato != null){

                tipo.setText(dato.games.tarea)
                tema.setText(dato.games.tema_actual)
                puntos.setText(dato.games.total_puntaje+" pts.")
                preguntas.setText(dato.games.juegos+" preg.")


                when (dato.games.tema.toInt()) {
                    1 -> {
                        img.setImageResource(R.drawable.icono_vocal);
                    }
                    2-> {
                        img.setImageResource(R.drawable.icono_abecedario);
                    }
                    3 -> {
                        img.setImageResource(R.drawable.icono_silabas_simples);
                    }
                    4 -> {
                        img.setImageResource(R.drawable.icono_deletreo);
                    }
                    5 -> {
                        img.setImageResource(R.drawable.icono_sopa_letras);
                    }
                    6 -> {
                        img.setImageResource(R.drawable.icono_oracion);
                    }
                    else -> { // Opción default

                    }
                }



                Log.e("puntaje tprne", "${dato.games.total_puntaje}")
            }
        }
    }



}