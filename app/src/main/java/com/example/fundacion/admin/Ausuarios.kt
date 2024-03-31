package com.example.fundacion.admin

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint.Style
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.AdapterUsuario
import com.example.fundacion.admin.adapter.AdapterUsuarioAnt
import com.example.fundacion.config
import com.example.fundacion.login
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.Calendar


private lateinit var rvUsuarios: RecyclerView
private val usuarioss: MutableList<lusuarios> = mutableListOf()

private lateinit var rvAnt: RecyclerView
private val antList: MutableList<lusuariosAn> = mutableListOf()


class Ausuarios : BaseActivity(), Refresh {

    private val calendar = Calendar.getInstance()
    private lateinit var nacimiento: EditText
    private lateinit var list: LinearLayout
    private lateinit var new: LinearLayout
    private lateinit var ant: LinearLayout
    private lateinit var editTextSearch: EditText
    private lateinit var txtbuscador: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ausuarios)

        rvUsuarios = findViewById(R.id.listusuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(this)
        cargar_datos()

        rvAnt = findViewById(R.id.listusuariosAC)
        rvAnt.layoutManager = LinearLayoutManager(this)
        cargar_ant()

        //Buttons
        val btnaceptar = findViewById<Button>(R.id.aceptar_new)
        val btn_buscador = findViewById<ImageButton>(R.id.btn_list)
        val btnBuscador = findViewById<ImageButton>(R.id.btn_buscador)

        //Views
        list = findViewById(R.id.view_list)
        new = findViewById(R.id.view_new)
        ant = findViewById(R.id.view_ant)

        //Editext
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

        editTextSearch = findViewById(R.id.buscador_list)
        txtbuscador = findViewById(R.id.txt_buscador)


        btn_buscador.setOnClickListener {
            val searchText = editTextSearch.text.toString().trim()
            filterList(searchText)
            hideKeyboard()
        }
        btnBuscador.setOnClickListener {
            val searchTextant = txtbuscador.text.toString().trim()
            filterListAn(searchTextant)
            hideKeyboard()
        }


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

            Fuel.post("${config.url}admin/user-create")
                .jsonBody(posData)
                .responseString{result ->
                    result.fold(
                        success = { data->

                            Toasty.success(this, "usuario creado", Toasty.LENGTH_SHORT).show()
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

    private fun filterList(searchText: String) {
        val filteredList = mutableListOf<lusuarios>()
        for (item in usuarioss) {
            if (item.id.contains(searchText, true) ||
                item.nombres.contains(searchText, true) ||
                item.apellidos.contains(searchText, true) ||
                item.ci.contains(searchText, true) ||
                item.celular.contains(searchText, true) ||
                item.codigo.contains(searchText, true) ||
                item.nacimiento.contains(searchText, true) ||
                item.pais.contains(searchText, true) ||
                item.ciudad.contains(searchText, true) ||
                item.email.contains(searchText, true) ||
                item.user.contains(searchText, true) ||
                item.pass.contains(searchText, true) ||
                item.rol.contains(searchText, true) ||
                item.estado.contains(searchText, true)
            ) {
                filteredList.add(item)
            }
        }
        val adapter = AdapterUsuario(this, filteredList, this@Ausuarios)
        rvUsuarios.adapter = adapter
    }

    private fun filterListAn(searchTextAnt: String) {
        val filteredList = mutableListOf<lusuariosAn>()
        for (itema in antList) {
            if (itema.id.contains(searchTextAnt, true) ||
                itema.nombres.contains(searchTextAnt, true) ||
                itema.apellidos.contains(searchTextAnt, true) ||
                itema.ci.contains(searchTextAnt, true) ||
                itema.celular.contains(searchTextAnt, true) ||
                itema.codigo.contains(searchTextAnt, true) ||
                itema.nacimiento.contains(searchTextAnt, true) ||
                itema.pais.contains(searchTextAnt, true) ||
                itema.ciudad.contains(searchTextAnt, true) ||
                itema.email.contains(searchTextAnt, true) ||
                itema.user.contains(searchTextAnt, true) ||
                itema.pass.contains(searchTextAnt, true) ||
                itema.rol.contains(searchTextAnt, true) ||
                itema.estado.contains(searchTextAnt, true)
            ) {
                filteredList.add(itema)
            }
        }
        val adapter = AdapterUsuarioAnt(this, filteredList, this@Ausuarios)
        rvAnt.adapter = adapter
    }


    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
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

    override fun refresha(){
        cargar_datos()
        cargar_ant()
        editTextSearch.setText("")
        txtbuscador.setText("")
    }

    private fun cargar_datos(){


        Fuel.get("${config.url}admin/user-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userList = Gson().fromJson(data, Array<lusuarios>::class.java).toList()
                    usuarioss.clear()
                    usuarioss.addAll(userList)

                    runOnUiThread{
                        val aadapter = AdapterUsuario(this, usuarioss, this@Ausuarios)
                        rvUsuarios?.adapter = aadapter
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }

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

    fun off(view: View){
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        finish()
    }

    fun inicio(view : View){
        val intent = Intent(this, Ainicio::class.java)
        startActivity(intent)
        finish()
    }

    fun cancel_new(view: View){
        ant.visibility = View.GONE
        new.visibility = View.GONE
        list.visibility = View.VISIBLE
    }

    fun btn_new(view: View){
        ant.visibility = View.GONE
        new.visibility = View.VISIBLE
        list.visibility = View.GONE
    }

    fun btn_ant(view: View){
        ant.visibility = View.VISIBLE
        new.visibility = View.GONE
        list.visibility = View.GONE
    }


}