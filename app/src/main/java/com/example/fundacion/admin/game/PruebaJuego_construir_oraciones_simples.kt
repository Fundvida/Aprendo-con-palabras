package com.example.fundacion.admin.game

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class PruebaJuego_construir_oraciones_simples : BaseActivity(), TextToSpeech.OnInitListener {


    lateinit var jsonArrayDatos : JSONArray
    var position = 0
    var botonclick = 0
    var oracionV : String? = null

    lateinit var txtimage : ImageView
    lateinit var GridBoton : GridLayout
    lateinit var txtoracion : TextView


    private lateinit var tts: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_construir_oraciones_simples)

        tts = TextToSpeech(this, this)

        Handler(Looper.getMainLooper()).postDelayed({
            speakOut("ordena la oracion")
        }, 2000)

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(config.NAMEJuegoPrueba)

        txtimage = findViewById(R.id.txtimg)
        GridBoton = findViewById(R.id.botones)
        txtoracion = findViewById(R.id.textoracion)
        datos()
    }

    fun datos(){

        Fuel.get("${config.url}admin/preg-oraciones-simples-all/${config.IDJuegoPrueba}").responseString{ result ->
            result.fold(
                success = { data ->
                    println(data)
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

    fun inicio(){
        if (jsonArrayDatos.length() > position){



            val item = jsonArrayDatos.getJSONObject(position)
            val img = item.getString("img")
            Glide.with(this)
                .load("${config.url}admin/preg-oraciones-simples-imagen/$img")
                .into(txtimage)



            oracionV = item.getString("palabra").toString()


            val oracion = item.getString("palabra")
            val palabras = oracion.split(" ")
            val arrayPalabras = palabras.toTypedArray()
            arrayPalabras.shuffle()


            val columnas = if (arrayPalabras.size <= 3) 3 else 4
            GridBoton.columnCount = columnas


            println(arrayPalabras.size)

            for (palabra in arrayPalabras) {
                val button = Button(this)
                button.text = palabra
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(10, 10, 10, 10)
                }

                button.setOnClickListener {
                    var texto = ""
                    if (txtoracion.text.toString().isEmpty() ){
                        texto = "$palabra"

                    }else{
                        texto = "${txtoracion.text} $palabra"
                    }
                    txtoracion.setText(texto)
                    button.isEnabled = false
                    botonclick++
                    if (botonclick == arrayPalabras.count()){
                        siguiente()
                    }
                }
                button.setBackgroundResource(R.drawable.fondo_vocal_noselect)
                //button.typeface = ResourcesCompat.getFont(this, R.font.digitalt)
                button.setTypeface(null, Typeface.BOLD)
                button.isAllCaps = false
                GridBoton.addView(button)
            }

        }
        else{
            val textToSpeak = "felicidades terminaste quieres volver a jugar"
            speakOut(textToSpeak)
            alertTerminado()
        }

    }


    fun siguiente(){
        if(txtoracion.text == oracionV){
            println("la oracion es correcta")
            GridBoton.removeAllViews()
            GridBoton.invalidate()
            alertCorrecto()


            val textToSpeak = "la oracion es correcta"
            speakOut(textToSpeak)
        }else{
            println("la oracion era $oracionV")
            GridBoton.removeAllViews()
            GridBoton.invalidate()
            alertIncorrecto()

            val textToSpeak = "la oracion correcta era $oracionV"
            speakOut(textToSpeak)
        }
    }

    fun retroceder(view: View){

        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.titleText = "ESTAS SEGURO!"
        sweetAlertDialog.contentText = "¿QUE QUIERES SALIR?"
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
        sweetAlertDialog.titleText = "Oracion Correcta!!!"
        sweetAlertDialog.confirmText = "siguiente"
        sweetAlertDialog.setConfirmClickListener {
            opcionSeleccionada = true
            position++
            botonclick = 0
            inicio()
            txtoracion.setText("")
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
        sweetAlertDialog.titleText = "Oracion Icorrecta!!!"
        sweetAlertDialog.confirmText = "siguiente"
        sweetAlertDialog.setConfirmClickListener {
            opcionSeleccionada = true
            position++
            botonclick = 0
            inicio()
            txtoracion.setText("")
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
        sweetAlertDialog.contentText = "¿QUIERES VOLVER A JUGAR?"
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