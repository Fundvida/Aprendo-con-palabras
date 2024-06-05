package com.example.fundacion.admin.game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.Locale

class PruebaJuego_silabasimple : BaseActivity(), TextToSpeech.OnInitListener {

    lateinit var jsonArrayDatos : JSONArray
    var position = 0

    lateinit var txtimage : ImageView
    lateinit var silaba_est : TextView
    lateinit var silaba_resp : TextView

    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    lateinit var button5 : Button

    var vocales = listOf('a', 'e', 'i', 'o', 'u')
    var variantes = mutableListOf<String>()
    var PalabraCompleta: String? = null

    private lateinit var tts: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_silabasimple)

        tts = TextToSpeech(this, this)


        Handler(Looper.getMainLooper()).postDelayed({
            speakOut("Escoge una de las opciones que tienes abajo para completar la palabra guiandote de la imagen que se muestra")
        }, 2000)

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(config.NAMEJuegoPrueba)



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

        Fuel.get("${config.url}admin/preg-silabas-simples-all/${config.IDJuegoPrueba}").responseString{ result ->
            result.fold(
                success = { data ->
                    println(data)
                    val textToSpeak = "${config.NAMEJuegoPrueba}"
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
        var opcionSeleccionada = false
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        sweetAlertDialog.titleText = "NO HAY DATOS"
        sweetAlertDialog.cancelText = "volver"
        sweetAlertDialog.confirmText = "ir inicio"
        sweetAlertDialog.setConfirmClickListener {
            opcionSeleccionada = true
            val intent = Intent(this, Ainicio::class.java)
            startActivity(intent)
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            opcionSeleccionada = true
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setOnDismissListener {
            if (!opcionSeleccionada) {
                alertFAIL()
            }
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
            val textToSpeak = "felicidades terminaste quieres volver a jugar"
            speakOut(textToSpeak)
            alertTerminado()
        }

    }

    fun siguiente(resp:String){
        silaba_resp.setText(resp)
        val juntarresp = silaba_est.text.toString() + resp

        if (PalabraCompleta == juntarresp)
        {
            val textToSpeak = "la palabra formada es $juntarresp y es correcto"
            speakOut(textToSpeak)


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

            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                withContext(Dispatchers.Main) {
                    alertIncorrecto()
                }
            }
        }

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

    fun alertTerminado(){
        var opcionSeleccionada = false

        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        sweetAlertDialog.titleText = "FELICIDADES TERMINASTE!"
        sweetAlertDialog.contentText = "QUIERES VOLVER A JUGAR"
        sweetAlertDialog.confirmText = "si"
        sweetAlertDialog.cancelText = "no"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmClickListener {
            position = 0
            opcionSeleccionada = true
            recreate()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            opcionSeleccionada = true
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setOnDismissListener {
            if (!opcionSeleccionada) {
                alertTerminado()
            }
        }
        sweetAlertDialog.show()
    }

    //texto speach

  /*  override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::tts.isInitialized && tts.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE) {
            val tex = config.NAMEJuegoPrueba.toString()
            val textToSpeak =  tex
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "AUDIO_AFTER_LOADING")
        }
    }*/



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