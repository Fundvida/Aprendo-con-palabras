package com.example.fundacion.user.torneo_estud

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.fundacion.BarButtonOFF
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.config
import com.example.fundacion.user.ESTUDdatos
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.Locale

class Game_torneo_silabassimples_estud : BarButtonOFF(), TextToSpeech.OnInitListener {


    lateinit var jsonArrayDatos : JSONArray
    var position = 0
    var pts_ganados = 0
    var pts_fallados = 0
    var correctas = 0
    var incorrectas = 0

    lateinit var txtimage : ImageView
    lateinit var silaba_est : TextView
    lateinit var silaba_resp : TextView

    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    lateinit var button5 : Button

    lateinit var puntaje : TextView

    var vocales = listOf('a', 'e', 'i', 'o', 'u')
    var variantes = mutableListOf<String>()
    var PalabraCompleta: String? = null

    private lateinit var tts: TextToSpeech

    var index : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_torneo_silabassimples_estud)


        index = ESTUDdatos.indexActivity

        puntaje = findViewById(R.id.puntaje)

        findViewById<TextView>(R.id.puntaje_total).setText(ESTUDdatos.puntaje_total.toString())

        val games_total = ESTUDdatos.datos.size - index!! -1

        if (games_total == 1){
            findViewById<TextView>(R.id.faltan_games).setText("Falta " + games_total.toString() + " juego")
        }
        else if (games_total == 0){
            findViewById<TextView>(R.id.faltan_games).setText("ultimo juego")
        }
        else{
            findViewById<TextView>(R.id.faltan_games).setText("Faltan " + games_total.toString() + " juegos")
        }



        tts = TextToSpeech(this, this)


        Handler(Looper.getMainLooper()).postDelayed({
            speakOut("Escoge una de las opciones que tienes abajo para completar la palabra guiandote de la imagen que se muestra")
        }, 2000)

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(ESTUDdatos.datos[index!!].nameGAME)



        txtimage = findViewById(R.id.txtimg)
        silaba_est = findViewById(R.id.silaba_est)
        silaba_resp = findViewById(R.id.silaba_resp)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)

        datos()


        button1.setOnClickListener {
            val resp = button1.text.toString()
            siguiente(resp)

            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false

        }
        button2.setOnClickListener {
            val resp = button2.text.toString()
            siguiente(resp)
            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false
        }
        button3.setOnClickListener {
            val resp = button3.text.toString()
            siguiente(resp)
            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false
        }
        button4.setOnClickListener {
            val resp = button4.text.toString()
            siguiente(resp)

            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false
        }
        button5.setOnClickListener {
            val resp = button5.text.toString()
            siguiente(resp)

            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false
        }

    }


    fun datos(){

        Fuel.get("${config.url}admin/preg-silabas-simples-all/${ESTUDdatos.datos[index!!].idGAME}").responseString{ result ->
            result.fold(
                success = { data ->
                    Log.e("GAME Silabas", data)
                    val textToSpeak = "${ESTUDdatos.datos[index!!].nameGAME}"
                    speakOut(textToSpeak)
                    jsonArrayDatos = JSONArray(data)
                    if (jsonArrayDatos.length() > 0){
                        inicio()
                    }else{
                        alertFAIL()
                    }
                },
                failure = { error ->
                    Log.e("Upload", "Error: ${error.exception.message}")
                }
            )
        }
    }

    fun alertFAIL(){
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        sweetAlertDialog.titleText = "NO HAY DATOS"
        sweetAlertDialog.cancelText = "volver"
        sweetAlertDialog.confirmText = "ir inicio"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmClickListener {
            val intent = Intent(this, Ainicio::class.java)
            startActivity(intent)
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.show()
    }

    fun inicio(){
        if (jsonArrayDatos.length() > position){

            button1.isEnabled = true
            button2.isEnabled = true
            button3.isEnabled = true
            button4.isEnabled = true
            button5.isEnabled = true

            silaba_resp.setText(" _ _")
            val item = jsonArrayDatos.getJSONObject(position)
            val img = item.getString("img")
            Glide.with(this)
                .load("${config.url}admin/preg-silabas-simples-imagen/$img")
                .into(txtimage)

            silaba_est.setText(item.getString("silaba_estatico"))

            val pregunta = item.getString("silaba_pregunta")
            variantes.clear()

            if (pregunta.last().toString() == "a"){
                for (vocal in vocales) {
                    val variante = pregunta.replace('A', vocal, ignoreCase = true)
                    variantes.add(variante)
                }
            }
            else if (pregunta.last().toString() == "e"){
                for (vocal in vocales) {
                    val variante = pregunta.replace('E', vocal, ignoreCase = true)
                    variantes.add(variante)
                }
            }
            else if (pregunta.last().toString() == "i"){
                for (vocal in vocales) {
                    val variante = pregunta.replace('I', vocal, ignoreCase = true)
                    variantes.add(variante)
                }
            }
            else if (pregunta.last().toString() == "o"){
                for (vocal in vocales) {
                    val variante = pregunta.replace('O', vocal, ignoreCase = true)
                    variantes.add(variante)
                }
            }
            else if (pregunta.last().toString() == "u"){
                for (vocal in vocales) {
                    val variante = pregunta.replace('U', vocal, ignoreCase = true)
                    variantes.add(variante)
                }
            }

            variantes.shuffle()

            val botones = listOf(button1, button2, button3, button4, button5)
            for (i in botones.indices) {
                botones[i].text = variantes[i]
            }
            PalabraCompleta = item.getString("palabra")
        }
        else{



            val textToSpeak = "felicidades terminaste de jugar "
            speakOut(textToSpeak)
            alertTerminado()
        }

    }

    fun alertTerminado(){

        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_game_torneo_siguiente_sumpunts)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.findViewById<TextView>(R.id.cant_correctas).text = correctas.toString()
        dialog.findViewById<TextView>(R.id.cant_falladas).text = incorrectas.toString()
        dialog.findViewById<TextView>(R.id.pts_correctas).text = pts_ganados.toString()
        dialog.findViewById<TextView>(R.id.pts_falladas).text = pts_fallados.toString()
        val sumar = pts_ganados+pts_fallados
        dialog.findViewById<TextView>(R.id.total_pts).text = sumar.toString()

        dialog.findViewById<Button>(R.id.btn_salir).setOnClickListener { alertSALIR() }
        dialog.findViewById<Button>(R.id.btn_siguiente).setOnClickListener { Next_Activity() }


        dialog.show()

    }


    fun alertSALIR(){
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.titleText = "¿ESTAS SEGURO DE SALIR?"
        sweetAlertDialog.contentText = "perderas un intento y se guardara solo tu puntaje total"
        sweetAlertDialog.confirmText = "si"
        sweetAlertDialog.cancelText = "no"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmClickListener {
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.show()
    }

    fun Next_Activity(){
        val currentIndex = ESTUDdatos.indexActivity + 1

        if (currentIndex <  ESTUDdatos.datos.size) {
            val currentTarea = ESTUDdatos.datos[currentIndex]
            val nextActivityClass = ESTUDdatos.temaActivityMap[currentTarea.nameTEMA]

            Log.e("game", "$nextActivityClass")
            Log.e("game", "$currentTarea")

            ESTUDdatos.indexActivity++
            if (nextActivityClass != null) {
                val intent = Intent(this, nextActivityClass)
                startActivity(intent)
            }
        } else {
            // Manejar el caso cuando todas las tareas han sido completadas
            val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            sweetAlertDialog.titleText = "FELICIDADES TERMINASTE!"
            sweetAlertDialog.contentText = "¿QUIERES VOLVER A JUGAR?"
            sweetAlertDialog.confirmText = "si"
            sweetAlertDialog.cancelText = "no"
            sweetAlertDialog.setCancelable(false)
            sweetAlertDialog.setConfirmClickListener {
                position = 0
                recreate()
                sweetAlertDialog.dismissWithAnimation()
            }
            sweetAlertDialog.setCancelClickListener {
                finish()
                sweetAlertDialog.dismissWithAnimation()
            }

            sweetAlertDialog.show()
        }

    }


    fun siguiente(resp:String){
        silaba_resp.setText(resp)
        val juntarresp = silaba_est.text.toString() + resp

        val item = jsonArrayDatos.getJSONObject(position)

        if (PalabraCompleta == juntarresp)
        {
            val textToSpeak = "la palabra formada es $juntarresp y es correcto"
            speakOut(textToSpeak)

            pts_ganados = pts_ganados + item.getString("puntaje").toInt()
            puntaje.setText(pts_ganados.toString())

            ESTUDdatos.correctas++
            correctas++

            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                withContext(Dispatchers.Main) {

                    alertCorrecto()

                }
            }

        }
        else{
            val textToSpeak = "la palabra correcta era $PalabraCompleta"
            speakOut(textToSpeak)

            pts_fallados = pts_fallados - 2
            ESTUDdatos.errores++
            incorrectas++

            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                withContext(Dispatchers.Main) {
                    alertIncorrecto()
                }
            }
        }

    }



    fun salir_btn(view: View){

        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.titleText = "¿ESTAS SEGURO DE SALIR?"
        sweetAlertDialog.contentText = "perderas un intento y se guardara solo tu puntaje total"
        sweetAlertDialog.confirmText = "si"
        sweetAlertDialog.cancelText = "no"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmClickListener {
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.show()

    }





    fun alertCorrecto(){
        var opcionSeleccionada = false
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        sweetAlertDialog.titleText = "Silaba Correcta!!!"
        sweetAlertDialog.confirmText = "siguiente"
        sweetAlertDialog.setConfirmClickListener {
            opcionSeleccionada = true
            position++
            inicio()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setOnDismissListener {
            if (!opcionSeleccionada) {
                alertCorrecto()
            }
        }
        sweetAlertDialog.show()
    }

    fun alertIncorrecto(){
        var opcionSeleccionada = false
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        sweetAlertDialog.titleText = "Silaba Icorrecta!!!"
        sweetAlertDialog.confirmText = "siguiente"
        sweetAlertDialog.setConfirmClickListener {
            opcionSeleccionada = true
            position++
            inicio()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setOnDismissListener {
            if (!opcionSeleccionada) {
                alertIncorrecto()
            }
        }
        sweetAlertDialog.show()
    }








    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            }
            else{
                val voiceId = "narrator" // Identificador de la voz de narrador
                tts.voice = Voice(voiceId, Locale.getDefault(), Voice.QUALITY_HIGH, Voice.LATENCY_NORMAL, false, null)
            }
        }
    }
    private fun speakOut(text: String) {
        if (::tts.isInitialized && tts.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e("TextToSpeech", "TextToSpeech not initialized or language not available.")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
    }


}