package com.example.fundacion.admin.adapter

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Refresh
import com.example.fundacion.admin.ldocentes
import com.example.fundacion.admin.lestudiantes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Calendar

class AdapterEstudiantes (
    private val context: Context,
    private val Elist: List<lestudiantes>,
    private val refresh: Refresh
): RecyclerView.Adapter<AdapterEstudiantes.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_list_estudiantes, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estudiante = Elist[position]
        holder.bind(estudiante)

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

            Fuel.delete("${config.url}admin/estudiante-delete/"+estudiante.id).responseString { _, _, result ->
                result.fold(
                    success = { data ->
                        Toasty.error(context, "Estudiante Eliminado", Toasty.LENGTH_SHORT).show()
                        refresh.refresha()
                    },
                    failure = { error -> println(error) }

                )
            }

        }
    }

    override fun getItemCount(): Int {
        return Elist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.nombres)
        private val txtApellidos: TextView = itemView.findViewById(R.id.apellidos)
        private val txtPais: TextView = itemView.findViewById(R.id.pais)
        private val txtDocente: TextView = itemView.findViewById(R.id.docente)
        private val txtPuntos: TextView = itemView.findViewById(R.id.puntos)
        val btnActualizar: ImageButton = itemView.findViewById(R.id.edit)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.delete)
        val btnVer: ImageButton = itemView.findViewById(R.id.ver)

        fun bind(estudiantes: lestudiantes) {
            if (estudiantes != null){
                txtNombre.text = estudiantes.nombres
                txtApellidos.text = estudiantes.apellidos
                txtPais.text = estudiantes.pais
                txtDocente.text = estudiantes.docentes.nombres +" "+estudiantes.docentes.apellidos
                txtPuntos.text = estudiantes.puntos
            }
        }
    }


    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText
    lateinit var SpinnerDocente : Spinner
    var idDocente : String? = null

    private fun docentes(onDocentesLoaded: (List<ldocentes>) -> Unit) {
        Fuel.get("${config.url}admin/docentes-all").responseString { _, _, result ->
            result.fold(
                success = { data ->
                    println(data)
                    val list = Gson().fromJson(data, Array<ldocentes>::class.java).toList()

                    val firt = list.map { "${it.nombres} ${it.apellidos} "}.toMutableList()
                    firt.add(0, "Elige Un Docente")
                    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, firt)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    SpinnerDocente.adapter = adapter

                    SpinnerDocente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (position == 0) {
                                // No hacer nada o resetear el valor de idDocente cuando se selecciona "Elige Un Docente"
                                idDocente = null
                            } else {
                                val selectedPersona = list[position - 1]
                                idDocente = selectedPersona.id
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }

                    // Llama al callback pasando la lista de docentes
                    onDocentesLoaded(list)
                },
                failure = { error -> println(error) }
            )
        }
    }



    fun modal_edit(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_estudiante_edit)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val aceptar = dialog.findViewById<Button>(R.id.btn_aceptar)

        val nombre = dialog.findViewById<EditText>(R.id.nombres)
        val apellido = dialog.findViewById<EditText>(R.id.apellidos)
        val carnet = dialog.findViewById<EditText>(R.id.carnet)
        val celular = dialog.findViewById<EditText>(R.id.celular)
        nacimiento = dialog.findViewById(R.id.nacimiento)
        val pais = dialog.findViewById<EditText>(R.id.pais)
        val ciudad = dialog.findViewById<EditText>(R.id.ciudad)
        val correo = dialog.findViewById<EditText>(R.id.correo)
        val user = dialog.findViewById<EditText>(R.id.user)
        val rpass = dialog.findViewById<Button>(R.id.btn_rpass)
        SpinnerDocente = dialog.findViewById(R.id.spiner)

        val datos = Elist[position]

        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.carnet)
        celular.setText(datos.celular)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.pais)
        ciudad.setText(datos.ciudad)
        correo.setText(datos.email)
        user.setText(datos.user)

        docentes { list ->
            // Encuentra la posición del docente que quieres preseleccionar
            val docenteIndex = list.indexOfFirst { it.id == datos.docente } + 1 // +1 por "Elige Un Docente"
            if (docenteIndex > 0) {
                SpinnerDocente.setSelection(docenteIndex)
            }
        }

        dialog.show()

        nacimiento.setOnClickListener{ mostrarDatePickerDialog() }
        close.setOnClickListener { dialog.dismiss() }
        cancel.setOnClickListener { dialog.dismiss() }

        aceptar.setOnClickListener {

            val postData = """
            {
                "docente": "$idDocente",
                "nombres": "${nombre.text}",
                "apellidos": "${apellido.text}",
                "carnet": "${carnet.text}",
                "celular": "${celular.text}",
                "nacimiento": "${nacimiento.text}",
                "pais": "${pais.text}",
                "ciudad": "${ciudad.text}",
                "correo": "${correo.text}",
                "user": "${user.text}"
            }
        """.trimIndent()

            Fuel.put("${config.url}admin/estudiante-update/"+datos.id)
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

        val datos = Elist[position]

        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.carnet)
        celular.setText(datos.celular)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.pais)
        ciudad.setText(datos.ciudad)
        correo.setText(datos.email)
        user.setText(datos.user)
        docente.setText(datos.docente)
        puntos.setText(datos.puntos)

        dialog.show()
    }

}