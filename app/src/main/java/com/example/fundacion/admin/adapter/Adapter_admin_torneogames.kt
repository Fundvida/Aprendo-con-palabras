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
import com.example.fundacion.admin.Torneo_Game
import com.example.fundacion.admin.fragment.FTorneo_games
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import es.dmoral.toasty.Toasty

class Adapter_admin_torneogames(
    private val context: Context,
    private val juegos: List<Torneo_Game>,
    private val refresh: FTorneo_games
): RecyclerView.Adapter<Adapter_admin_torneogames.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_toneogames, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val games = juegos[position]
        holder.bind(games)

        holder.edit.setOnClickListener {
            holder.itemView.post{
                refresh.edit(games.id.toInt())
            }
        }

        holder.delete.setOnClickListener {
            Fuel.delete("${config.url}admin/torneo-game-delete/${games.id}").responseString{ result ->
                result.fold(
                    success = {data->
                        Toasty.error(context, "Juego Eliminado", Toasty.LENGTH_SHORT).show()
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
        return juegos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nivel : TextView = itemView.findViewById(R.id.nivel)
        private val tema : TextView = itemView.findViewById(R.id.tema)
        private val tarea : TextView = itemView.findViewById(R.id.tarea)
//        private val imageView : ImageView = itemView.findViewById(R.id.image)
        var edit : ImageButton = itemView.findViewById(R.id.edit)
        var delete : ImageButton = itemView.findViewById(R.id.delete)

        fun bind(games: Torneo_Game) {
            if (games != null){

                println("los datos son ===>> ${juegos}")
                nivel.text = games.games.tema.nivel.tipo
                tema.text = games.games.tema.tema
                tarea.text = games.games.tarea
                /*
                Glide.with(itemView.context)
                    .load("${config.url}admin/preg-vocal-imagen/${preg.img}")
                    .into(imageView)

                 */
            }
        }
    }



}