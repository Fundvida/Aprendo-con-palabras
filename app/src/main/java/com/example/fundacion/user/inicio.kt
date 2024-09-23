package com.example.fundacion.user

import TareaAdapter
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.TokenManager
import com.example.fundacion.applyAnimationG_BTN
import com.example.fundacion.applyAnimationG_P
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen
import com.example.fundacion.login
import com.example.fundacion.muteSound
import com.example.fundacion.unmuteSound
import com.example.fundacion.user.adapter.TorneoAdapter
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class inicio : BaseActivity() {

    private val torneo_list: MutableList<ETorneoEstudiante> = mutableListOf()


    lateinit var BTNtorneo : ImageButton


    private lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val tokenManager = TokenManager(this)

        val token = tokenManager.getToken()

        var userData = tokenManager.getUserData()

        val nombreCom : TextView = findViewById(R.id.nombreCompleto)
        nombreCom.setText(userData.get("nombres").toString()+" "+userData.get("apellidos").toString())


        BTNtorneo = findViewById(R.id.torneoBTN)
        Fuel.get("${config.url}estud/torneo/${userData.get("id")}").responseString{result ->
            result.fold(
                success = { d ->

                    Log.e("dato", "$d")

                    val jsonArray = JSONArray(d)

                    // Verificar si el array tiene más de un elemento
                    if (jsonArray.length() > 0) {

                        BTNtorneo.visibility = View.VISIBLE

                        val datos = Gson().fromJson(d, Array<ETorneoEstudiante>::class.java).toList()
                        torneo_list.clear()
                        torneo_list.addAll(datos)


                    } else {
                        BTNtorneo.visibility = View.GONE

                    }


                    /*
                      val userList = Gson().fromJson(data, Array<lusuarios>::class.java).toList()
                    usuarioss.clear()
                    usuarioss.addAll(userList)

                    runOnUiThread{
                        val aadapter = AdapterUsuario(this, usuarioss, this@Ausuarios)
                        rvUsuarios?.adapter = aadapter
                    }

                     */
                },
                failure = {error ->

                    println("Error en la solicitud: $error")
                }
            )
        }





        val imageButton: ImageButton = findViewById(R.id.imageButton)

        applyAnimationG_P(imageButton)

        mediaPlayer = MediaPlayer.create(this, R.raw.inicio2)
        mediaPlayer.isLooping = true

        mediaPlayer.start()


        val abecedario: ImageButton = findViewById(R.id.abecedario)
        val vocales: ImageButton = findViewById(R.id.vocales)
        val silabaSimple: ImageButton = findViewById(R.id.silabas)

        applyAnimationG_BTN(abecedario)
        applyAnimationG_BTN(vocales)
        applyAnimationG_BTN(silabaSimple)


        val btnSonido = findViewById<ImageButton>(R.id.btn_sonido)
        val btnSilencio = findViewById<ImageButton>(R.id.btn_silencio)

        btnSonido.setOnClickListener {
            btnSonido.visibility = View.GONE
            btnSilencio.visibility = View.VISIBLE
            unmuteSound(this)
        }
        btnSilencio.setOnClickListener {
            btnSonido.visibility = View.VISIBLE
            btnSilencio.visibility = View.GONE
            muteSound(this)

        }

    }



    fun menu_btn(view: View){
        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_menuinicio)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val btnnivel = dialog.findViewById<ImageButton>(R.id.btn_nivel)
        val btnsalir = dialog.findViewById<ImageButton>(R.id.btn_salir)
        val btncerrar = dialog.findViewById<ImageButton>(R.id.btn_cerrar)
        val close = dialog.findViewById<ImageButton>(R.id.btn_close)

        applyAnimationG_P(btnnivel)
        applyAnimationG_P(btncerrar)
        applyAnimationG_P(btnsalir)

        btnnivel.setOnClickListener{
            val intent = Intent(this, Ueligirnivel::class.java)
            startActivity(intent)
            finish()

        }
        btncerrar.setOnClickListener{
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()

        }
        btnsalir.setOnClickListener{
            finishAffinity()
        }
        close.setOnClickListener { dialog.dismiss() }



        dialog.show()
        configurefullScreen(dialog)
    }



    fun whatsapp(view: View){
        val phoneNumber = "59174025156"
        val message = "¡Hola! ¿Cómo estás?" // El mensaje que deseas enviar

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra("jid", "$phoneNumber@s.whatsapp.net")
        startActivity(intent)

    }

    fun instagram(view: View){
        val username = "fundeducarparalavida"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.instagram.com/$username/")
        startActivity(intent)
    }

    fun facebook(view: View){
        val facebookPageId = "100063510101095"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.facebook.com/$facebookPageId")
        startActivity(intent)
    }


    private lateinit var revTorneo : RecyclerView
    private lateinit var recyy : RecyclerView
    fun gamelist(int: Int){
        Fuel.get("${config.url}estud/game/$int")
            .responseString{ _, _, result ->
                result.fold(
                    success = { d ->
                        println(d)

                        val tareaList: List<Tarea> = parseJsonToTareaList(d)

                        recyy.adapter = TareaAdapter(tareaList)
                    },
                    failure = {error ->

                        println("Error en la solicitud: $error")
                    }
                )
            }

        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_model_inicio_list)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        recyy = dialog.findViewById(R.id.recy)
        recyy.layoutManager = LinearLayoutManager(this)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }





        dialog.show()

        configurefullScreen(dialog)
    }
    fun torneoGame(){

        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_model_inicio_list)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        revTorneo = dialog.findViewById(R.id.recy)
        revTorneo.layoutManager = LinearLayoutManager(this)
        revTorneo.adapter = TorneoAdapter(this, torneo_list){
            finish()
        }

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }





        dialog.show()
        configurefullScreen(dialog)

    }


    private fun parseJsonToTareaList(jsonString: String): List<Tarea> {
        val gson = Gson()
        val type = object : TypeToken<List<Tarea>>() {}.type
        return gson.fromJson(jsonString, type)
    }



    fun vocales(view: View){
        gamelist(1)
    }

    fun abecedario(view: View){
        gamelist(2)
    }

    fun silabas(view: View){
        gamelist(3)
    }

    fun deletreo(view: View){
        gamelist(4)
    }

    fun sopaletras(view: View){
        gamelist(5)
    }

    fun oraciones(view: View){
        gamelist(6)
    }


    fun torneo(view: View){
        torneoGame()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()  // Pausar la música cuando la app está minimizada
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()  // Reanudar la música cuando la app está activa de nuevo
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

}