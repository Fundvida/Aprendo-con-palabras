package com.example.fundacion.admin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.fundacion.BaseActivity
import com.example.fundacion.NetworkListener
import com.example.fundacion.R
import com.example.fundacion.TokenUser
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.docente.Dlogin

class Ainicio : BaseActivity() {
    private lateinit var networkListener: NetworkListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ainicio)

        networkListener = NetworkListener(this)
        networkListener.startListening()

        val tokenManager = TokenUser(this)

        val token = tokenManager.getToken()

        var userData = tokenManager.getUserData()


        findViewById<TextView>(R.id.nombres).text = "${userData.get("nombres")}"
        findViewById<TextView>(R.id.apellidos).text = "${userData.get("apellidos")}"

    }

    override fun onDestroy() {
        super.onDestroy()
        networkListener.stopListening()
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

}