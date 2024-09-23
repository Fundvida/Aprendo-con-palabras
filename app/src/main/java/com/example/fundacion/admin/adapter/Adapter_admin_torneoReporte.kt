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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Estadistica
import com.example.fundacion.admin.Refreshtorneo
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import java.util.Calendar

class Adapter_admin_torneoReporte (
    private val context: Context,
    private val estadistica: List<Estadistica>,
    private val refreshableComponent: Refreshtorneo
): RecyclerView.Adapter<Adapter_admin_torneoReporte.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_estadistica_all, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dato = estadistica[position]
        holder.bind(dato)

/*

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

        */
    }
    override fun getItemCount(): Int {
        return estadistica.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtNombre: TextView = itemView.findViewById(R.id.nombre)
        private val txtEstado: TextView = itemView.findViewById(R.id.estado)
        private val txtPrimero: TextView = itemView.findViewById(R.id.primero)
        private val txtSegundo: TextView = itemView.findViewById(R.id.segundo)
        private val txtTercero: TextView = itemView.findViewById(R.id.tercero)

        val btnTiempo: LinearLayout = itemView.findViewById(R.id.tiempo)
        val btnJugadores: LinearLayout = itemView.findViewById(R.id.jugadores)

        fun bind(dato: Estadistica) {
            if (dato != null){
                txtNombre.text = dato.nombre

                if (dato.estado == "1"){
                    txtEstado.text = "Disponible"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.successColor)
                }
                else if(dato.estado == "2"){
                    txtEstado.text = "terminado"
                    txtEstado.setBackgroundResource(es.dmoral.toasty.R.color.errorColor)
                }

                txtPrimero.text = dato.torneo[0].puntaje+",  "+dato.torneo[0].nombre+" "+dato.torneo[0].apellido
                txtSegundo.text = dato.torneo[0].puntaje+",  "+dato.torneo[0].nombre+" "+dato.torneo[0].apellido
                txtTercero.text = dato.torneo[0].puntaje+",  "+dato.torneo[0].nombre+" "+dato.torneo[0].apellido

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

        val datos = estadistica[position]

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