package com.example.fundacion.admin.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Torneo_estudiante
import com.example.fundacion.admin.fragment.FTorneo_estudiantes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import es.dmoral.toasty.Toasty

class Adapter_admin_torneo_estudiante (
    private val context: Context,
    private val estudiantes: List<Torneo_estudiante>,
    private val refresh: FTorneo_estudiantes
): RecyclerView.Adapter<Adapter_admin_torneo_estudiante.ViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_torneoestudiantes, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = estudiantes[position]
        holder.bind(dato)

        holder.edit.setOnClickListener {
            holder.itemView.post{
                refresh.edit(dato.id.toInt())
            }
        }
        holder.eyes.setOnClickListener {
            holder.itemView.post{
                modal_ver(holder.adapterPosition)
            }
        }

        holder.delete.setOnClickListener {
            Fuel.delete("${config.url}admin/torneo-estudiantes-delete/${dato.id}").responseString{ result ->
                result.fold(
                    success = {data->
                        Toasty.error(context, "Estudiante Eliminado", Toasty.LENGTH_SHORT).show()
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
        return estudiantes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        private val imageView : ImageView = itemView.findViewById(R.id.image)
        var edit : ImageButton = itemView.findViewById(R.id.edit)
        var delete : ImageButton = itemView.findViewById(R.id.delete)
        var eyes : ImageButton = itemView.findViewById(R.id.eyes)

        val nombres : TextView = itemView.findViewById(R.id.nombres)
        val apellidos : TextView = itemView.findViewById(R.id.apellidos)
        val edad : TextView = itemView.findViewById(R.id.edad)
        val docente : TextView = itemView.findViewById(R.id.docente)

        fun bind(datos: Torneo_estudiante) {
            if (datos != null){


                nombres.setText(datos.estu.nombres)
                apellidos.setText(datos.estu.apellidos)
                edad.setText(datos.estu.edad)
                docente.setText(datos.estu.docentes.nombres+" "+datos.estu.docentes.apellidos)


                /*
                Glide.with(itemView.context)
                    .load("${config.url}admin/preg-vocal-imagen/${preg.img}")
                    .into(imageView)

                 */
            }
        }
    }


    fun modal_ver(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_estudiante_ver)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }

        val nombre = dialog.findViewById<TextView>(R.id.nombres)
        val apellido = dialog.findViewById<TextView>(R.id.apellidos)
        val carnet = dialog.findViewById<TextView>(R.id.carnet)
        val celular = dialog.findViewById<TextView>(R.id.celular)
        val nacimiento = dialog.findViewById<TextView>(R.id.fecha_nac)
        val pais = dialog.findViewById<TextView>(R.id.pais)
        val ciudad = dialog.findViewById<TextView>(R.id.ciudad)
        val correo = dialog.findViewById<TextView>(R.id.correo)
        val user = dialog.findViewById<TextView>(R.id.user)
        val docente = dialog.findViewById<TextView>(R.id.docente)
        val puntos = dialog.findViewById<TextView>(R.id.puntos)

        val datos = estudiantes[position]

        nombre.setText(datos.estu.nombres)
        apellido.setText(datos.estu.apellidos)
        carnet.setText(datos.estu.carnet)
        celular.setText(datos.estu.celular)
        nacimiento.setText(datos.estu.nacimiento)
        pais.setText(datos.estu.pais)
        ciudad.setText(datos.estu.ciudad)
        correo.setText(datos.estu.email)
        user.setText(datos.estu.user)
        docente.setText(datos.estu.docentes.nombres +" "+ datos.estu.docentes.apellidos )
        puntos.setText(datos.estu.puntos)

        dialog.show()
    }



}