package com.example.fundacion

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import com.example.fundacion.docente.Dlogin
import com.example.fundacion.user.Ueligirnivel
import com.example.fundacion.user.registrarme
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class login : BaseActivity() {


    private lateinit var popupWindow: PopupWindow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btnMas = findViewById<FloatingActionButton>(R.id.btnmas)
        /*
        val inflater = LayoutInflater.from(this)
        val popupView: View = inflater.inflate(R.layout.menu_btnmas, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        btnMas.setOnClickListener {
            if (!isFinishing && !isDestroyed) {
                val offsetY = resources.getDimensionPixelSize(R.dimen.popup_offset_y)
                popupWindow.showAtLocation(
                    btnMas,
                    Gravity.BOTTOM or Gravity.NO_GRAVITY,
                    0,
                    offsetY
                )
            }
        }
        */

        btnMas.setOnClickListener{
            val dialog = Dialog(this, R.style.TransparentDialog)
            dialog.setContentView(R.layout.uuu_modal_menulogin)

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            val docente = dialog.findViewById<ImageButton>(R.id.btn_docente)
            val tutor = dialog.findViewById<ImageButton>(R.id.btn_tutor)
            val registro = dialog.findViewById<ImageButton>(R.id.btn_registro)
            val close = dialog.findViewById<ImageButton>(R.id.btn_close)

            docente.setOnClickListener{
                val intent = Intent(this, Dlogin::class.java)
                startActivity(intent)
            }
            tutor.setOnClickListener{
                /*val intent = Intent(this, Dlogin::class.java)
                startActivity(intent)*/
                println(config.url)
            }
            registro.setOnClickListener{
                val intent = Intent(this, Dlogin::class.java)
                startActivity(intent)
            }
            close.setOnClickListener { dialog.dismiss() }



            dialog.show()
        }


        val txtuser = findViewById<EditText>(R.id.txtuser)
        val txtpass = findViewById<TextView>(R.id.txtpass)

        val btnLogin = findViewById<ImageButton>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val u = txtuser.text.toString()
            val p = txtpass.text.toString()
            LOGIN(u,p)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        //popupWindow.dismiss()
    }


    fun LOGIN(u: String, p: String) {

/*
        val postData = """
            {
                "username": "$u",
                "password": "$p"
            }
        """.trimIndent()
*/

        val postData = """
            {
                "username": "estu",
                "password": "estu"
            }
        """.trimIndent()
        var tokenManager = TokenManager(this)

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

                            tokenManager.saveTokenData(jsonresp.getString("token"))
                            Toasty.success(this, "estudiante encontrado", Toasty.LENGTH_SHORT).show()
                            val intent = Intent(this, Ueligirnivel::class.java)
                            startActivity(intent)


                            Log.e("login","$d")

                        }
                    },
                    failure = {error ->

                        println("Error en la solicitud: $error")
                    }
                )
            }

    }
    fun soydocente(view: View){
        Thread{
            val intent = Intent(this, Dlogin::class.java)
            startActivity(intent)
        }.start()
        //finish()
    }
    fun soytutor(view: View){
        //val intent = Intent(this, Dlogin::class.java)
        //startActivity(intent)
    }

    fun registrarme(view: View){
        GlobalScope.launch(Dispatchers.Main) {
            // Coloca aquí el código que quieres ejecutar de manera asíncrona
            // Por ejemplo, operaciones de red, procesamiento intensivo, etc.
            withContext(Dispatchers.Default) {
                // Operaciones en un hilo en segundo plano
            }

            val intent = Intent(this@login, registrarme()::class.java)
            startActivity(intent)
        }
    }

}