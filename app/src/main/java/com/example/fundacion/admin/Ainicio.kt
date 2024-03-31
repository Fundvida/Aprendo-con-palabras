package com.example.fundacion.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.login
import es.dmoral.toasty.Toasty

class Ainicio : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ainicio)

    }
    fun inicio(view : View){
        Toasty.warning(this, "Estas en inicio", Toasty.LENGTH_LONG).show()
    }
    fun usuarios(view : View){
        val intent = Intent(this, Ausuarios::class.java)
        startActivity(intent)
        //finish()
    }

    fun docentes(view : View){

        Toasty.warning(this, "docente", Toasty.LENGTH_LONG).show()
    }

    fun estudiantes(view : View){

        Toasty.warning(this, "Estudiante", Toasty.LENGTH_LONG).show()
    }
    fun juegos(view : View){
        val intent = Intent(this, Agame::class.java)
        startActivity(intent)
    }
    fun torneo(view : View){

        Toasty.warning(this, "torneo", Toasty.LENGTH_LONG).show()
    }
    fun demo(view : View){

        Toasty.warning(this, "demo", Toasty.LENGTH_LONG).show()
    }
    fun reportes(view : View){

        Toasty.warning(this, "reportes", Toasty.LENGTH_LONG).show()
    }
    fun buscador(view : View){

        Toasty.warning(this, "buscador", Toasty.LENGTH_LONG).show()
    }

    fun off(view: View){
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        //finish()
    }

}