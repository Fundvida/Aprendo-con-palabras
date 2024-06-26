package com.example.fundacion.admin.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.FTorneo_estudiantes
import com.example.fundacion.admin.lestudiantes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty

class Adapter_admin_torneo_estudiante_new(
    private val context: Context,
    private val estudiantes: List<lestudiantes>,
    private val refresh: FTorneo_estudiantes
): RecyclerView.Adapter<Adapter_admin_torneo_estudiante_new.ViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_list_torneoestudiantes, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = estudiantes[position]
        holder.bind(dato)

        holder.eyes.setOnClickListener {
            holder.itemView.post{
                modal_ver(holder.adapterPosition)
            }
        }

        holder.agregar.setOnClickListener {


            Log.e("raro","${dato.id} +++ ${config.IDTorneo}")

            val postData = """
            {
                "IDTorneoGame": "${config.IDTorneo}",
                "IDEstu": "${dato.id}"
            }
        """.trimIndent()

            Fuel.post("${config.url}admin/torneo-estudiante-agregar")
                .jsonBody(postData)
                .responseString{ _, _, result ->
                    result.fold(
                        success = { d ->

                            Toasty.success(context, "Estudiante Agregado", Toasty.LENGTH_SHORT).show()
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
        return estudiantes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var agregar : Button = itemView.findViewById(R.id.agregar)
        var eyes : ImageButton = itemView.findViewById(R.id.ver)

        val nombres : TextView = itemView.findViewById(R.id.nombres)
        val apellidos : TextView = itemView.findViewById(R.id.apellidos)
        val edad : TextView = itemView.findViewById(R.id.edad)
        val docente : TextView = itemView.findViewById(R.id.docente)
        val pais : TextView = itemView.findViewById(R.id.pais)
        val ciudad : TextView = itemView.findViewById(R.id.ciudad)


        fun bind(datos: lestudiantes) {
            if (datos != null){

                nombres.setText(datos.nombres)
                apellidos.setText(datos.apellidos)
                edad.setText(datos.edad)
                docente.setText(datos.docentes.nombres+" "+datos.docentes.apellidos)
                ciudad.setText(datos.ciudad)
                pais.setText(datos.pais)



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


        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.carnet)
        celular.setText(datos.celular)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.pais)
        ciudad.setText(datos.ciudad)
        correo.setText(datos.email)
        user.setText(datos.user)
        docente.setText(datos.docentes.nombres +" "+ datos.docentes.apellidos )
        puntos.setText(datos.puntos)


        dialog.show()
    }




}