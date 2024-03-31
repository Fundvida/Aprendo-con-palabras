package com.example.fundacion.docente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.config
import com.example.fundacion.user.inicio
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty
import org.json.JSONObject

class Dlogin : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlogin)


        val button = findViewById<Button>(R.id.btnLogin)
        val user = findViewById<EditText>(R.id.txtuserA)
        val pass = findViewById<EditText>(R.id.txtpassA)

        button.setOnClickListener {

            val u = user.text.toString()
            val p = pass.text.toString()

            LOGIN(u, p)
        }
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
                "username": "admin",
                "password": "admin"
            }
        """.trimIndent()

        Fuel.post("${config.url}Alogin")
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
                            Toasty.success(this, "Credenciales Correctas", Toasty.LENGTH_SHORT).show()
                            val intent = Intent(this, Ainicio::class.java)
                            startActivity(intent)
                        }
                    },
                    failure = {error ->

                        println("Error en la solicitud: $error")
                    }
                )
            }

    }

}