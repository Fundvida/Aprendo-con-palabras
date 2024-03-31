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
import com.example.fundacion.admin.lgames
import com.example.fundacion.admin.lusuarios
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import java.util.Calendar

class AdapterGames(
    private val context: Context,
    private val userList: List<lgames>
    //private val refreshableComponent: Refresh
): RecyclerView.Adapter<AdapterGames.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_view_juegos_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val games = userList[position]
        holder.bind(games)
/*
        holder.btnActualizar.setOnClickListener {

            holder.itemView.post{
                modal_edit(holder.adapterPosition)
            }
        }

        holder.btnEliminar.setOnClickListener {

            Fuel.delete("${config.url}admin/user-delete/"+games.id).responseString { _, _, result ->
                result.fold(
                    success = { data ->
                        Toasty.error(context, "Usuario Eliminado", Toasty.LENGTH_SHORT).show()
                        //refreshableComponent.refresha()
                    },
                    failure = { error -> println(error) }

                )
            }

        }*/
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      /*  private val txtNombre: TextView = itemView.findViewById(R.id.nombres)
        private val txtApellidos: TextView = itemView.findViewById(R.id.apellidos)
        private val txtCarnet: TextView = itemView.findViewById(R.id.carnet)
        private val txtCelular: TextView = itemView.findViewById(R.id.celular)
        private val txtCodgio: TextView = itemView.findViewById(R.id.codigo)
        private val txtNacimiento: TextView = itemView.findViewById(R.id.nacimiento)
        private val txtPais: TextView = itemView.findViewById(R.id.pais)
        private val txtCiudad: TextView = itemView.findViewById(R.id.ciudad)
        private val txtCorreo: TextView = itemView.findViewById(R.id.correo)
        private val txtUser: TextView = itemView.findViewById(R.id.user)
        val btnActualizar: ImageButton = itemView.findViewById(R.id.edit)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.delete)*/

        fun bind(games: lgames) {
            if (games != null){
            /*
                txtNombre.text = usuario.nombres
                txtApellidos.text = usuario.apellidos
                txtCarnet.text = usuario.ci
                txtCelular.text = usuario.celular
                txtCodgio.text = usuario.codigo
                txtNacimiento.text = usuario.nacimiento
                txtPais.text = usuario.pais
                txtCiudad.text = usuario.ciudad
                txtCorreo.text = usuario.email
                txtUser.text = usuario.user
                */
            }
        }
    }


    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText

    fun modal_edit(position: Int){

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.aaa_modal_usuario_edit)

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

        val datos = userList[position]

        nombre.setText(datos.nombres)
        apellido.setText(datos.apellidos)
        carnet.setText(datos.ci)
        celular.setText(datos.celular)
        codigo.setText(datos.codigo)
        nacimiento.setText(datos.nacimiento)
        pais.setText(datos.ciudad)
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

                            //refreshableComponent.refresha()
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


}