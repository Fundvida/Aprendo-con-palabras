package com.example.fundacion.admin

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.AdapterDocente
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.docente.Dlogin
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Calendar


private lateinit var rvDocentes: RecyclerView
private val docentes: MutableList<ldocentes> = mutableListOf()

class Adocentes : BaseActivity(), Refresh {


    private lateinit var list: LinearLayout
    private lateinit var new: LinearLayout
    private lateinit var ant: LinearLayout

    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText
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
        setContentView(R.layout.activity_adocentes)


        rvDocentes = findViewById(R.id.listusuarios)
        rvDocentes.layoutManager = LinearLayoutManager(this)
        cargar_datos()

        list = findViewById(R.id.view_list)
        new = findViewById(R.id.view_new)
        ant = findViewById(R.id.view_ant)



        val nombre = findViewById<EditText>(R.id.nombres)
        val apellido = findViewById<EditText>(R.id.apellidos)
        val carnet = findViewById<EditText>(R.id.carnet)
        val celular = findViewById<EditText>(R.id.celular)
        val codigo = findViewById<EditText>(R.id.codigo)
        nacimiento = findViewById(R.id.nacimiento)
        val pais = findViewById<EditText>(R.id.pais)
        val ciudad = findViewById<EditText>(R.id.ciudad)
        val correo = findViewById<EditText>(R.id.correo)
        val user = findViewById<EditText>(R.id.user)
        nacimiento.setOnClickListener{mostrarDatePickerDialog()}


        val btnaceptar = findViewById<Button>(R.id.aceptar_new)
        btnaceptar.setOnClickListener {
            val posData = """
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

            Fuel.post("${config.url}admin/docente-create")
                .jsonBody(posData)
                .responseString{result ->
                    result.fold(
                        success = { data->

                            Toasty.success(this, "docente creado", Toasty.LENGTH_SHORT).show()
                            refresha()
                            new.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            nombre.setText("")
                            apellido.setText("")
                            carnet.setText("")
                            celular.setText("")
                            codigo.setText("")
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


    private fun cargar_datos(){


        Fuel.get("${config.url}admin/docentes-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val list = Gson().fromJson(data, Array<ldocentes>::class.java).toList()
                    docentes.clear()
                    docentes.addAll(list)

                    runOnUiThread{
                        val aadapter = AdapterDocente(this, docentes, this@Adocentes)
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



    fun inicio(view : View){
        val intent = Intent(this, Ainicio::class.java)
        startActivity(intent)
        finish()
    }
    fun usuarios(view : View){
        val intent = Intent(this, Ausuarios::class.java)
        startActivity(intent)
        finish()
    }
    fun estudiantes(view : View){
        val intent = Intent(this, Aestudiantes::class.java)
        startActivity(intent)
        finish()

    }
    fun juegos(view : View){
        val intent = Intent(this, Agame::class.java)
        startActivity(intent)
        finish()

    }
    fun torneo(view : View){

        //Toasty.warning(this, "torneo", Toasty.LENGTH_LONG).show()
        val intent = Intent(this, Atorneo::class.java)
        startActivity(intent)
        finish()

    }
    fun ayuda(view : View){

        //  val intent = Intent(this, PruebaJuego_silabas_CTI::class.java)
        //startActivity(intent)
    }
    fun reportes(view : View){
        val intent = Intent(this, Areportes::class.java)
        startActivity(intent)
        finish()

    }
    fun pruebas(view : View){
        val intent = Intent(this, Apruebas::class.java)
        startActivity(intent)
        finish()

    }

    fun off(view: View){

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.aaa_modal_btn_off)


        val close = dialog.findViewById<ImageButton>(R.id.btn_close)


        val cerrar = dialog.findViewById<Button>(R.id.cerrar_sesion)
        val salir = dialog.findViewById<Button>(R.id.salir)
        val ayuda = dialog.findViewById<Button>(R.id.ayuda)
        val perfil = dialog.findViewById<Button>(R.id.editar_perfil)


        cerrar.setOnClickListener { startActivity(Intent(this, Dlogin::class.java)); finish() }
        salir.setOnClickListener { finishAffinity() }

        dialog.show()
        configurefullScreen_fullview(dialog)

        close.setOnClickListener { dialog.dismiss() }


        //val intent = Intent(this, login::class.java)
        //startActivity(intent)
    }

    fun volver(view: View){
        finish()
    }





}