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
import com.example.fundacion.admin.Refresh
import com.example.fundacion.admin.ldocentes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import java.util.Calendar

class AdapterDocente (
    private val context: Context,
    private val Dlist: List<ldocentes>,
    private val refresh: Refresh
): RecyclerView.Adapter<AdapterDocente.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_list_docentes, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val docente = Dlist[position]
        holder.bind(docente)

        holder.btnActualizar.setOnClickListener {

            holder.itemView.post{
                modal_edit(holder.adapterPosition)
            }
        }

        holder.btnVer.setOnClickListener {

            holder.itemView.post{
                modal_ver(holder.adapterPosition)
            }
        }

        holder.btnEliminar.setOnClickListener {

            Fuel.delete("${config.url}admin/user-delete/"+docente.id).responseString { _, _, result ->
                result.fold(
                    success = { data ->
                        Toasty.error(context, "Usuario Eliminado", Toasty.LENGTH_SHORT).show()
                        refresh.refresha()
                    },
                    failure = { error -> println(error) }

                )
            }

        }
    }
    override fun getItemCount(): Int {
        return Dlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.nombres)
        private val txtApellidos: TextView = itemView.findViewById(R.id.apellidos)
        private val txtCodgio: TextView = itemView.findViewById(R.id.codigo)
        private val txtPais: TextView = itemView.findViewById(R.id.pais)
        val btnActualizar: ImageButton = itemView.findViewById(R.id.edit)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.delete)
        val btnVer: ImageButton = itemView.findViewById(R.id.ver)

        fun bind(docente: ldocentes) {
            if (docente != null){
                txtNombre.text = docente.nombres
                txtApellidos.text = docente.apellidos
                txtCodgio.text = docente.codigo
                txtPais.text = docente.pais
            }
        }
    }


    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText

    fun modal_edit(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_docentes_edit)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val aceptar = dialog.findViewById<Button>(R.id.btn_aceptar)

        val nombre = dialog.findViewById<EditText>(R.id.nombres)
        val apellido = dialog.findViewById<EditText>(R.id.apellidos)
        val carnet = dialog.findViewById<EditText>(R.id.carnet)
        val celular = dialog.findViewById<EditText>(R.id.celular)
        val codigo = dialog.findViewById<EditText>(R.id.codigo)
        nacimiento = dialog.findViewById(R.id.nacimiento)
        val pais = dialog.findViewById<EditText>(R.id.pais)
        val ciudad = dialog.findViewById<EditText>(R.id.ciudad)
        val correo = dialog.findViewById<EditText>(R.id.correo)
        val user = dialog.findViewById<EditText>(R.id.user)
        val rpass = dialog.findViewById<Button>(R.id.btn_rpass)

        val datos = Dlist[position]

        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.ci)
        celular.setText(datos.celular)
        codigo.setText(datos.codigo)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.pais)
        ciudad.setText(datos.ciudad)
        correo.setText(datos.email)
        user.setText(datos.user)

        dialog.show()

        nacimiento.setOnClickListener{ mostrarDatePickerDialog() }
        close.setOnClickListener { dialog.dismiss() }
        cancel.setOnClickListener { dialog.dismiss() }

        aceptar.setOnClickListener {

            val postData = """
            {
                "nombres": "${nombre.text}",
                "apellidos": "${apellido.text}",
                "carnet": "${carnet.text}",
                "celular": "${celular.text}",
                "codigo": "${codigo.text}",
                "nacimiento": "${nacimiento.text}",
                "pais": "${pais.text}",
                "ciudad": "${ciudad.text}",
                "correo": "${correo.text}",
                "user": "${user.text}"
            }
        """.trimIndent()

            Fuel.put("${config.url}admin/user-update/"+datos.id)
                .jsonBody(postData)
                .responseString { _, _, result ->
                    result.fold(
                        success = { data->


                            Toasty.success(context, "Actualizado Correctamente", Toasty.LENGTH_SHORT).show()

                            refresh.refresha()
                            dialog.dismiss()

                        },
                        failure = { error -> println(error) }

                    )
                }
        }
    }

    private fun mostrarDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                nacimiento.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun modal_ver(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_docente_ver)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }

        val nombre = dialog.findViewById<TextView>(R.id.nombres)
        val apellido = dialog.findViewById<TextView>(R.id.apellidos)
        val carnet = dialog.findViewById<TextView>(R.id.carnet)
        val celular = dialog.findViewById<TextView>(R.id.celular)
        val codigo = dialog.findViewById<TextView>(R.id.codigo)
        val nacimiento = dialog.findViewById<TextView>(R.id.fecha_nac)
        val pais = dialog.findViewById<TextView>(R.id.pais)
        val ciudad = dialog.findViewById<TextView>(R.id.ciudad)
        val correo = dialog.findViewById<TextView>(R.id.correo)
        val user = dialog.findViewById<TextView>(R.id.user)

        val datos = Dlist[position]

        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.ci)
        celular.setText(datos.celular)
        codigo.setText(datos.codigo)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.pais)
        ciudad.setText(datos.ciudad)
        correo.setText(datos.email)
        user.setText(datos.user)

        dialog.show()
    }

}