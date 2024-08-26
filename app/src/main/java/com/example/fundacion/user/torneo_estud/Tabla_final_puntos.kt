package com.example.fundacion.user.torneo_estud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.TokenManager
import com.example.fundacion.config
import com.example.fundacion.user.ESTUDdatos
import com.example.fundacion.user.inicio
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import es.dmoral.toasty.Toasty

class Tabla_final_puntos : BaseActivity() {



    private lateinit var userData: Map<String, Any?>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabla_final_puntos)



        val tokenManager = TokenManager(this)

        userData = tokenManager.getUserData()









        findViewById<TextView>(R.id.intentos).text = ESTUDdatos.GameUserIntento.toString()
        findViewById<TextView>(R.id.games_count).text = ESTUDdatos.Games.toString()
        findViewById<TextView>(R.id.correctas).text = ESTUDdatos.correctas.toString()

        val error = ESTUDdatos.errores *2
        val puntos = ESTUDdatos.puntaje_total + error

        findViewById<TextView>(R.id.puntos).text = puntos.toString()
        findViewById<TextView>(R.id.errores).text = ESTUDdatos.errores.toString()
        findViewById<TextView>(R.id.puntos_errores).text = error.toString()

        val millis: Long = ESTUDdatos.GameTime

        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)



        val dmillis: Long = ESTUDdatos.GameTimeGlobal-ESTUDdatos.GameTime

        val dtotalSeconds = dmillis / 1000
        val dminutes = dtotalSeconds / 60
        val dseconds = dtotalSeconds % 60

        val dtimeFormatted = String.format("%02d:%02d", dminutes, dseconds)



        val bonus = millis / 1000 * 2


        findViewById<TextView>(R.id.time_logrado).text = dtimeFormatted
        findViewById<TextView>(R.id.time_restante).text = timeFormatted
        findViewById<TextView>(R.id.time_bonus).text = bonus.toString()




        findViewById<TextView>(R.id.puntos_ganados).text = puntos.toString()
        findViewById<TextView>(R.id.puntos_descontar).text = error.toString()
        findViewById<TextView>(R.id.puntos_extras).text = bonus.toString()

        val puntaje_final = puntos-error+bonus
        findViewById<TextView>(R.id.puntaje_final).text = puntaje_final.toString()



        val guardar : Button = findViewById(R.id.btn_guardar)
        guardar.setOnClickListener {

            val postData = """
            {
                "id": "${ESTUDdatos.GameID}",
                "puntaje": "$puntos",
                "tiempo": "$dtimeFormatted",
                "errores": "${ESTUDdatos.errores}",
                "correctas": "${ESTUDdatos.correctas}",
                "total": "$puntaje_final"
            }
        """.trimIndent()

            Fuel.post("${config.url}estud/torneo_guardar")
                .jsonBody(postData)
                .responseString{ result ->
                    result.fold(
                        success = { r ->
                            Log.e("respuesta get","$r")

                            ESTUDdatos.GameTime = 0L
                            ESTUDdatos.GameTimeGlobal = 0L
                            ESTUDdatos.GameIntentos = 0
                            ESTUDdatos.GameFechaInicio = ""
                            ESTUDdatos.GameFechaFin = ""
                            ESTUDdatos.GameID = 0
                            ESTUDdatos.GameUserIntento = 0
                            ESTUDdatos.Games = 0

                            ESTUDdatos.indexActivity = 0

                            ESTUDdatos.puntaje_total = 0
                            ESTUDdatos.errores = 0
                            ESTUDdatos.correctas = 0

                            Toasty.success(this, "Felicidad terminaste existosamente", Toasty.LENGTH_SHORT).show()

                            startActivity(Intent(this, inicio::class.java))
                            finish()
                        },
                        failure = {e -> Log.e("error get", "$e")}
                    )
                }

            startActivity(Intent(this, inicio::class.java))
            finish()
        }

    }



}