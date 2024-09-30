package com.example.fundacion.admin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.Adapter_admin_torneoReporte
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.docente.Dlogin
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson


private lateinit var rv: RecyclerView
private val estadistica: MutableList<Estadistica> = mutableListOf()

class Areportes : BaseActivity(), Refreshtorneo {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areportes)

        rv = findViewById(R.id.list)
        rv.layoutManager = LinearLayoutManager(this)


        cargar_datos()

    }


    private fun cargar_datos(){


        Fuel.get("${config.url}admin/estadistica-all").responseString { _, _, result ->
            result.fold(
                success = {data ->
                    println(data)
                    val userList = Gson().fromJson(data, Array<Estadistica>::class.java).toList()
                    estadistica.clear()
                    estadistica.addAll(userList)

                    runOnUiThread{
                        val aadapter = Adapter_admin_torneoReporte(this, estadistica, this@Areportes)
                        rv?.adapter = aadapter
                    }
                },
                failure = { error -> println(error) }
            )
        }
    }

    override fun refresha() {
        TODO("Not yet implemented")
    }

    override fun verlist(fragment: Fragment) {
        TODO("Not yet implemented")
    }

    override fun retro() {
        TODO("Not yet implemented")
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
    fun docentes(view : View){
        val intent = Intent(this, Adocentes::class.java)
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