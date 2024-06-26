package com.example.fundacion.admin

import android.app.DatePickerDialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.AdapterEstudiantes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Calendar



private lateinit var rvDocentes: RecyclerView
private val docentes: MutableList<ldocentes> = mutableListOf()

private val estudiantes: MutableList<lestudiantes> = mutableListOf()

class Aestudiantes : BaseActivity(), Refresh {




    private lateinit var list: LinearLayout
    private lateinit var new: LinearLayout
    private lateinit var ant: LinearLayout

    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText

    lateinit var SpinnerDocente : Spinner

    var idDocente : String? = null


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
        setContentView(R.layout.activity_aestudiantes)


        rvDocentes = findViewById(R.id.listusuarios)
        rvDocentes.layoutManager = LinearLayoutManager(this)
        cargar_datos()

        docentes()

        list = findViewById(R.id.view_list)
        new = findViewById(R.id.view_new)
        ant = findViewById(R.id.view_ant)



        val nombre = findViewById<EditText>(R.id.nombres)
        val apellido = findViewById<EditText>(R.id.apellidos)
        val carnet = findViewById<EditText>(R.id.carnet)
        val celular = findViewById<EditText>(R.id.celular)
        nacimiento = findViewById(R.id.nacimiento)
        val pais = findViewById<EditText>(R.id.pais)
        val ciudad = findViewById<EditText>(R.id.ciudad)
        val correo = findViewById<EditText>(R.id.correo)
        val user = findViewById<EditText>(R.id.user)
        SpinnerDocente = findViewById(R.id.spiner)
        nacimiento.setOnClickListener{mostrarDatePickerDialog()}


        val btnaceptar = findViewById<Button>(R.id.aceptar_new)
        btnaceptar.setOnClickListener {

            val posData = """
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


            Log.e("datos", "$posData")

            Fuel.post("${config.url}admin/estudiante-create")
                .jsonBody(posData)
                .responseString{result ->
                    result.fold(
                        success = { data->

                            Toasty.success(this, "estudiante creado", Toasty.LENGTH_SHORT).show()
                            refresha()
                            new.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            nombre.setText("")
                            apellido.setText("")
                            carnet.setText("")
                            celular.setText("")
                            nacimiento.setText("")
                            pais.setText("")
                            ciudad.setText("")
                            correo.setText("")
                            user.setText("")

                        },
                        failure = { error -> println(error) }

                    )
                }


        }
    }


    private fun docentes(){

        Fuel.get("${config.url}admin/docentes-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)

                    val list = Gson().fromJson(data, Array<ldocentes>::class.java).toList()

                    val firt = list.map { "${it.nombres} ${it.apellidos} "}.toMutableList()
                    firt.add(0, "Elige Un Docente")
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, firt)
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

                },
                failure = { error -> println(error) }
            )
        }

    }

    private fun cargar_datos(){


        Fuel.get("${config.url}admin/estudiante-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    val list = Gson().fromJson(data, Array<lestudiantes>::class.java).toList()
                    estudiantes.clear()
                    estudiantes.addAll(list)
                    Log.e("datos ", estudiantes.toString())

                    runOnUiThread{

                        val aadapter = AdapterEstudiantes(this, estudiantes, this@Aestudiantes)
                        rvDocentes?.adapter = aadapter
                    }

                },
                failure = { error -> println(error) }
            )
        }
    }


    fun btn_new(view: View){
        ant.visibility = View.GONE
        new.visibility = View.VISIBLE
        list.visibility = View.GONE
    }

    fun cancel_new(view: View){
        ant.visibility = View.GONE
        new.visibility = View.GONE
        list.visibility = View.VISIBLE
    }

    private fun mostrarDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$year-${month + 1}-$dayOfMonth"
                nacimiento.setText(fechaSeleccionada)
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