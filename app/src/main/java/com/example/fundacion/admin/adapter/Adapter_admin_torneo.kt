package com.example.fundacion.admin.adapter

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Refreshtorneo
import com.example.fundacion.admin.Torneo
import com.example.fundacion.admin.fragment.FTorneo_estudiantes
import com.example.fundacion.admin.fragment.FTorneo_games
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import java.util.Calendar

class Adapter_admin_torneo (
    private val context: Context,
    private val torneo: List<Torneo>,
    private val refreshableComponent: Refreshtorneo
): RecyclerView.Adapter<Adapter_admin_torneo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_torneo_all, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = torneo[position]
        holder.bind(dato)




        holder.btnEstudiante.setOnClickListener {

            config.IDTorneo = dato.id
            config.NameTorneo = dato.nombre
            refreshableComponent.verlist(FTorneo_estudiantes())

        }

        holder.btnJuegos.setOnClickListener {
            config.IDTorneo = dato.id
            config.NameTorneo = dato.nombre
            refreshableComponent.verlist(FTorneo_games())

        }
        holder.btnActualizar.setOnClickListener {

            holder.itemView.post{
                modal_edit(holder.adapterPosition)
            }
        }

        holder.btnEliminar.setOnClickListener {

            Fuel.delete("${config.url}admin/torneo-delete/"+dato.id).responseString { _, _, result ->
                result.fold(
                    success = { data ->
                        Toasty.error(context, "Torneo Eliminado", Toasty.LENGTH_SHORT).show()
                        refreshableComponent.refresha()
                    },
                    failure = { error -> println(error) }

                )
            }

        }
    }
    override fun getItemCount(): Int {
        return torneo.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.nombre)
        private val txtFechaIn: TextView = itemView.findViewById(R.id.fecha_inicio)
        private val txtFechaFi: TextView = itemView.findViewById(R.id.fecha_fin)
        private val txtTiempo: TextView = itemView.findViewById(R.id.tiempo)
        private val txtIntentos: TextView = itemView.findViewById(R.id.intentos)
        private val txtEstado: TextView = itemView.findViewById(R.id.estado)
        val btnActualizar: ImageButton = itemView.findViewById(R.id.edit)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.delete)
        val btnEstudiante: ImageButton = itemView.findViewById(R.id.estudiante)
        val btnJuegos: ImageButton = itemView.findViewById(R.id.games)

        fun bind(dato: Torneo) {
            if (dato != null){
                txtNombre.text = dato.nombre
                txtFechaIn.text = dato.fecha_inicio
                txtFechaFi.text = dato.fecha_fin
                txtTiempo.text = dato.tiempo
                txtIntentos.text = dato.intentos

                if (dato.estado == "1"){
                    txtEstado.text = "Disponible"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.successColor)
                    btnActualizar.isEnabled = true
                    btnEliminar.isEnabled = true
                    btnEstudiante.isEnabled = true
                    btnJuegos.isEnabled = true
                }
                else if(dato.estado == "2"){
                    txtEstado.text = "terminado"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.errorColor)
                    btnActualizar.isEnabled = false
                    btnEliminar.isEnabled = false
                    btnEstudiante.isEnabled = false
                    btnJuegos.isEnabled = false

                }

            }
        }
    }


    private val calendar = Calendar.getInstance()
    private lateinit var fechaIn: EditText
    private lateinit var fechaFin: EditText

    fun modal_edit(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_torneo_edit)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val aceptar = dialog.findViewById<Button>(R.id.btn_aceptar)

        val nombre = dialog.findViewById<EditText>(R.id.txt_nombre)
        fechaIn = dialog.findViewById(R.id.txt_fecha_inicio)
        fechaFin = dialog.findViewById(R.id.txt_fecha_fin)
        val tiempo = dialog.findViewById<EditText>(R.id.txt_tiempo)
        val intentos = dialog.findViewById<EditText>(R.id.txt_intentos)

        val datos = torneo[position]

        nombre.setText(datos.nombre)
        fechaIn.setText(datos.fecha_inicio)
        fechaFin.setText(datos.fecha_fin)
        tiempo.setText(datos.tiempo)
        intentos.setText(datos.intentos)


        dialog.show()

        fechaIn.setOnClickListener{ mostrarDatePickerDialogInicio() }
        fechaFin.setOnClickListener{ mostrarDatePickerDialogFin() }
        close.setOnClickListener { dialog.dismiss() }
        cancel.setOnClickListener { dialog.dismiss() }

        aceptar.setOnClickListener {

            val postData = """
            {
                        "nombre": "${nombre.text}",
                        "fecha_inicio": "${fechaIn.text}",
                        "fecha_fin": "${fechaFin.text}",
                        "tiempo": "${tiempo.text}",
                        "intentos": "${intentos.text}"
            }
        """.trimIndent()

            Fuel.put("${config.url}admin/torneo-update/"+datos.id)
                .jsonBody(postData)
                .responseString { _, _, result ->
                    result.fold(
                        success = { data->


                            Toasty.success(context, "Actualizado Correctamente", Toasty.LENGTH_SHORT).show()

                            refreshableComponent.refresha()
                            dialog.dismiss()

                        },
                        failure = { error -> println(error) }

                    )
                }
        }
    }

    private fun mostrarDatePickerDialogInicio() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                fechaIn.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    private fun mostrarDatePickerDialogFin() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                fechaFin.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }




}