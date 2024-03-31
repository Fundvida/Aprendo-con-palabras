package com.example.fundacion

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.fundacion.docente.Dlogin
import com.example.fundacion.user.inicio
import com.example.fundacion.user.registrarme
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject
import java.io.Console

class login : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btnMas = findViewById<Button>(R.id.btnmas)
        btnMas.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val popupView: View = inflater.inflate(R.layout.menu_btnmas, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            val offsetY = resources.getDimensionPixelSize(R.dimen.popup_offset_y)
            popupWindow.showAtLocation(
                btnMas,
                Gravity.BOTTOM or Gravity.NO_GRAVITY,
                0,
                offsetY
            )

        }

        val txtuser = findViewById<EditText>(R.id.txtuser)
        val txtpass = findViewById<TextView>(R.id.txtpass)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            /*val intent = Intent(this, inicio::class.java)
            startActivity(intent)*/
            val u = txtuser.text.toString()
            val p = txtpass.text.toString()
            LOGIN(u,p)
        }



    }

    fun LOGIN(u: String, p: String) {


        val postData = """
            {
                "username": "$u",
                "password": "$p"
            }
        """.trimIndent()

        Fuel.post("${config.url}login")
            .jsonBody(postData)
            .responseString{ _, _, result ->
                result.fold(
                    success = { d ->
                        val jsonresp = JSONObject(d)
                        if(jsonresp.getString("type") == "error") {
                            val error = jsonresp.getString("error").toString()
                            Toasty.error(this, error, Toasty.LENGTH_LONG).show()
                        }
                        else {
                            Toasty.success(this, "estudiante encontrado", Toasty.LENGTH_SHORT).show()
                            val intent = Intent(this, inicio::class.java)
                            startActivity(intent)
                        }
                    },
                    failure = {error ->

                        println("Error en la solicitud: $error")
                    }
                )
            }

    }
    fun soydocente(view: View){
        val intent = Intent(this, Dlogin::class.java)
        startActivity(intent)
        //finish()
    }
    fun soytutor(view: View){
        val intent = Intent(this, Dlogin::class.java)
        startActivity(intent)
    }

    fun registrarme(view: View){
        val intent = Intent(this, registrarme::class.java)
        startActivity(intent)
    }

}