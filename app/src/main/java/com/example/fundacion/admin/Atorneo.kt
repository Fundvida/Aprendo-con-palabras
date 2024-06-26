package com.example.fundacion.admin

import android.app.DatePickerDialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.Adapter_admin_torneo
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Calendar

private lateinit var rvUsuarios: RecyclerView
private val torneo: MutableList<Torneo> = mutableListOf()

class Atorneo : BaseActivity(), Refreshtorneo {

    private lateinit var newLinear : LinearLayout
    private lateinit var antLinear : LinearLayout
    private lateinit var actLinear : LinearLayout
    private lateinit var fragLinear : LinearLayout


    private val calendar = Calendar.getInstance()

    private lateinit var nombre : EditText
    private lateinit var fecha_inicio : EditText
    private lateinit var fecha_fin : EditText
    private lateinit var tiempo : EditText
    private lateinit var intentos : EditText


    override fun retro() {
        antLinear.visibility = View.GONE
        actLinear.visibility =View.VISIBLE
        newLinear . visibility = View.GONE
        fragLinear.visibility = View.GONE
    }

    override fun verlist(fragmento: Fragment){

        antLinear.visibility = View.GONE
        actLinear.visibility =View.GONE
        newLinear . visibility = View.GONE
        fragLinear.visibility = View.VISIBLE

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragmento)
        transaction.commit()
    }
    override fun refresha(){
        cargar_datos()
        /*
        cargar_ant()
        editTextSearch.setText("")
        txtbuscador.setText("")

         */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atorneo)

        rvUsuarios = findViewById(R.id.list)
        rvUsuarios.layoutManager = LinearLayoutManager(this)


        newLinear = findViewById(R.id.view_new)
        actLinear = findViewById(R.id.view_list)
        antLinear = findViewById(R.id.view_ant)
        fragLinear = findViewById(R.id.fragment)


        nombre = findViewById(R.id.txt_nombre)
        fecha_inicio = findViewById(R.id.txt_fecha_inicio)
        fecha_fin = findViewById(R.id.txt_fecha_fin)
        tiempo = findViewById(R.id.txt_tiempo)
        intentos = findViewById(R.id.txt_intentos)

        fecha_inicio.setOnClickListener { mostrarDatePickerDialogInicio() }
        fecha_fin.setOnClickListener { mostrarDatePickerDialogFin() }

        val btnAceptar = findViewById<Button>(R.id.aceptar_new)


        cargar_datos()

        btnAceptar.setOnClickListener {
            val posData = """
                {
                        "nombre": "${nombre.text}",
                        "fecha_inicio": "${fecha_inicio.text}",
                        "fecha_fin": "${fecha_fin.text}",
                        "tiempo": "${tiempo.text}",
                        "intentos": "${intentos.text}"
                }
            """.trimIndent()

            Fuel.post("${config.url}admin/torneo-create")
                .jsonBody(posData)
                .responseString{result ->
                    result.fold(
                        success = { data->

                            Toasty.success(this, "torneo creado", Toasty.LENGTH_SHORT).show()
                            refresha()
                            newLinear.visibility = View.GONE
                            actLinear.visibility = View.VISIBLE
                            nombre.setText("")
                            fecha_inicio.setText("")
                            fecha_fin.setText("")
                            tiempo.setText("")
                            intentos.setText("")

                        },
                        failure = { error -> println(error) }

                    )
                }
        }



    }


    private fun cargar_datos(){


        Fuel.get("${config.url}admin/torneo-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userList = Gson().fromJson(data, Array<Torneo>::class.java).toList()
                    torneo.clear()
                    torneo.addAll(userList)

                    runOnUiThread{
                        val aadapter = Adapter_admin_torneo(this, torneo, this@Atorneo)
                        rvUsuarios?.adapter = aadapter
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }




/*

    private fun cargar_ant(){


        Fuel.get("${config.url}admin/user-all-ant").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userListan = Gson().fromJson(data, Array<lusuariosAn>::class.java).toList()
                    antList.clear()
                    antList.addAll(userListan)

                    runOnUiThread{
                        val adpterr = AdapterUsuarioAnt(this, antList, this@Ausuarios)
                        rvAnt?.adapter = adpterr
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }


 */
    fun btn_new(view: View){
        newLinear.visibility = View.VISIBLE
        actLinear.visibility = View.GONE
    }

    fun cancel_new(view: View){
        newLinear.visibility = View.GONE
        actLinear.visibility = View.VISIBLE
    }


    private fun mostrarDatePickerDialogInicio() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                fecha_inicio.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnShowListener {
            val datePicker = datePickerDialog.datePicker
            val headerView = datePicker.findViewById<ViewGroup>(Resources.getSystem().getIdentifier("android:id/date_picker_header", null, null))
            headerView?.setBackgroundColor(Color.parseColor("#2f89a8"))

        }
        datePickerDialog.show()
    }

    private fun mostrarDatePickerDialogFin() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                fecha_fin.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnShowListener {
            val datePicker = datePickerDialog.datePicker
            val headerView = datePicker.findViewById<ViewGroup>(Resources.getSystem().getIdentifier("android:id/date_picker_header", null, null))
            headerView?.setBackgroundColor(Color.parseColor("#2f89a8"))

        }
        datePickerDialog.show()
    }

}
