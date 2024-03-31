package com.example.fundacion.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.Refresh
import com.example.fundacion.admin.lusuarios
import com.example.fundacion.admin.lusuariosAn
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import es.dmoral.toasty.Toasty

class AdapterUsuarioAnt(
    private val context: Context,
    private val userList: List<lusuariosAn>,
    private val refreshableComponent: Refresh
): RecyclerView.Adapter<AdapterUsuarioAnt.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aaa_listausuariosant, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = userList[position]
        holder.bind(usuario)



        holder.btnEdit.setOnClickListener {

            Fuel.delete("${config.url}admin/user-restart/"+usuario.id).responseString { _, _, result ->
                result.fold(
                    success = { data ->
                        Toasty.warning(context, "Usuario Restaurado", Toasty.LENGTH_SHORT).show()
                        refreshableComponent.refresha()
                    },
                    failure = { error -> println(error) }

                )
            }

        }
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.nombres)
        private val txtApellidos: TextView = itemView.findViewById(R.id.apellidos)
        private val txtCarnet: TextView = itemView.findViewById(R.id.carnet)
        private val txtCelular: TextView = itemView.findViewById(R.id.celular)
        private val txtCodgio: TextView = itemView.findViewById(R.id.codigo)
        private val txtNacimiento: TextView = itemView.findViewById(R.id.nacimiento)
        private val txtPais: TextView = itemView.findViewById(R.id.pais)
        private val txtCiudad: TextView = itemView.findViewById(R.id.ciudad)
        private val txtCorreo: TextView = itemView.findViewById(R.id.correo)
        private val txtUser: TextView = itemView.findViewById(R.id.user)
        val btnEdit: ImageButton = itemView.findViewById(R.id.edit)

        fun bind(usuario: lusuariosAn) {
            if (usuario != null){
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
            }
        }
    }

}