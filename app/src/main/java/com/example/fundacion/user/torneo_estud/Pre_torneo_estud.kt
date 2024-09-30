package com.example.fundacion.user.torneo_estud

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.TokenManager
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.user.EGAME_torneo_list
import com.example.fundacion.user.ESTUDdatos
import com.example.fundacion.user.ESTUDdatos.Companion.temaActivityMap
import com.example.fundacion.user.ETorneo_Game
import com.example.fundacion.user.ETorneo_PreGame
import com.example.fundacion.user.adapter.Torneo_pre_Adapter
import com.example.fundacion.user.adapter.Torneo_pregame_Adapter
import com.example.fundacion.user.inicio
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty


private lateinit var rvTorneo : RecyclerView
private val torneo_list: MutableList<ETorneo_Game> = mutableListOf()
private val torneo_prelist: MutableList<ETorneo_PreGame> = mutableListOf()


class Pre_torneo_estud : BaseActivity() {


    var IDTORNEO : String? = null

    private lateinit var revTorneo : RecyclerView
    private lateinit var revPreTorneo : RecyclerView
    private lateinit var userData: Map<String, Any?>


    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_torneo_estud)

        val tokenManager = TokenManager(this)

        userData = tokenManager.getUserData()

        IDTORNEO = intent.getStringExtra("TORNEO_ID")
        val torneoNombre = intent.getStringExtra("TORNEO_NOMBRE")

        findViewById<TextView>(R.id.title_torneo).text = torneoNombre.toString()

        rvTorneo = findViewById(R.id.torneo_list_pre)
        rvTorneo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        datos()



        mediaPlayer = MediaPlayer.create(this, R.raw.pre_game)
        mediaPlayer.isLooping = true

        mediaPlayer.start()


    }
    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
    fun datos()
    {
        Log.e("error IDTORNEO", "$IDTORNEO")
        Fuel.get("${config.url}estud/torneo/games-all-pre/$IDTORNEO").responseString{ result ->
            result.fold(
                success = { r ->
                    Log.d("respuesta get","$r")


                    val datos = Gson().fromJson(r, Array<ETorneo_Game>::class.java).toList()
                    torneo_list.clear()
                    torneo_list.addAll(datos)


                    ESTUDdatos.datos.clear()

                    for (item in torneo_list) {
                        ESTUDdatos.datos.add(EGAME_torneo_list("${item.games.tema}", "${item.games.tema_actual}", "${item.games.id}","${item.games.tarea}"))
                    }

                    ESTUDdatos.Games = torneo_list.size

                    rvTorneo.adapter = Torneo_pre_Adapter(this, torneo_list)
                },
                failure = {e -> Log.e("error get", "$e")}
            )
        }


    }

    lateinit var texto : TextView

    lateinit var maxPuntajeTorneo : TextView

    lateinit var maxIntentoTorneo : TextView
    var intentos = 0

    fun comenzar(view: View)
    {
        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_pre_torneo_preplay)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        revPreTorneo = dialog.findViewById(R.id.recy)
        revPreTorneo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        texto = dialog.findViewById(R.id.text)

        maxIntentoTorneo = dialog.findViewById(R.id.total_intentos)
        maxPuntajeTorneo = dialog.findViewById(R.id.record)

        dialog.findViewById<Button>(R.id.cancelar).setOnClickListener { dialog.dismiss() }

        revPreTorneo.adapter = Torneo_pregame_Adapter(this, torneo_prelist)

        val millis: Long = ESTUDdatos.GameTime // Milisegundos

        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)

        dialog.findViewById<TextView>(R.id.tiempo).text = timeFormatted
        dialog.findViewById<TextView>(R.id.intentos).text = ESTUDdatos.GameIntentos.toString()
        dialog.findViewById<TextView>(R.id.fecha_inicio).text = ESTUDdatos.GameFechaInicio
        dialog.findViewById<TextView>(R.id.fecha_fin).text = ESTUDdatos.GameFechaFin

        datosPre()


        dialog.findViewById<Button>(R.id.jugar).setOnClickListener{
            if (intentos == ESTUDdatos.GameIntentos){
                Toasty.error(this, "ya no tienes intentos permitidos", Toasty.LENGTH_SHORT).show()
            }
            else
            {
                val intentoss = intentos+1
                val postData = """
            {
                "estudiante": "${userData.get("id")}",
                "torneo": "$IDTORNEO",
                "intento": "$intentoss"
            }
        """.trimIndent()

                Fuel.post("${config.url}estud/new_torneo")
                    .jsonBody(postData)
                    .responseString{ result ->
                        result.fold(
                            success = { r ->
                                val jsonResponse = Gson().fromJson(r, JsonObject::class.java)

                                ESTUDdatos.GameID = jsonResponse.get("id").asInt
                                ESTUDdatos.GameUserIntento = intentos+1
                                playgame()

                            },
                            failure = {e -> Log.e("error get", "$e")}
                        )
                    }

            }
        }


        dialog.show()
        configurefullScreen_fullview(dialog)
    }

    fun datosPre()
    {
        val postData = """
            {
                "estudiante": "${userData.get("id")}",
                "torneo": "$IDTORNEO"
            }
        """.trimIndent()

        Fuel.post("${config.url}estud/result_torneo")
            .jsonBody(postData)
            .responseString{ result ->
                result.fold(
                    success = { r ->
                        Log.e("respuesta get","$r")

                        if (r.trim() == "[]"){
                            Log.e("respuesta get","esta vacio")
                        }
                        else
                        {
                            Log.e("respuesta get","esta llenio")

                            texto.visibility = View.GONE
                            revPreTorneo.visibility = View.VISIBLE

                            val datos = Gson().fromJson(r, Array<ETorneo_PreGame>::class.java).toList()
                            torneo_prelist.clear()
                            torneo_prelist.addAll(datos)

                            maxPuntajeTorneo.text = torneo_prelist.maxByOrNull { it.total }?.total ?: "0"
                            maxIntentoTorneo.text = torneo_prelist.maxByOrNull { it.intentos }?.intentos ?: "0"

                            intentos = torneo_prelist.maxByOrNull { it.intentos }?.intentos?.toInt() ?: 0

                            revPreTorneo.adapter = Torneo_pregame_Adapter(this, torneo_prelist)

                            Log.e("respuesta get","esta $torneo_prelist")

                        }
                    },
                    failure = {e -> Log.e("error get", "$e")}
                )
            }
    }
    fun playgame()
    {




        val currentIndex = ESTUDdatos.indexActivity

        if (currentIndex <  ESTUDdatos.datos.size) {
            val currentTarea = ESTUDdatos.datos[currentIndex]
            val nextActivityClass = temaActivityMap[currentTarea.nameTEMA]

            Log.e("game", "$nextActivityClass")
            Log.e("game", "$currentTarea")

            if (nextActivityClass != null) {
                val intent = Intent(this, nextActivityClass)
                startActivity(intent)
                finish()
            }
        } else {
            // Manejar el caso cuando todas las tareas han sido completadas
        }



    }

    fun volver(view: View){
        val intent = Intent(this, inicio::class.java)
        startActivity(intent)
        finish()
    }

    fun reglas(view: View)
    {
        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_pre_torneo_reglas)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }


        dialog.show()
        configurefullScreen(dialog)
    }
    fun premios(view: View)
    {
        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_pre_torneo_premios)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }

        dialog.show()
        configurefullScreen(dialog)
    }
}