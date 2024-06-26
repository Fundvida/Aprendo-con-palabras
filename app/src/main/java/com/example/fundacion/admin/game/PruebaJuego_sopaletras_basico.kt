package com.example.fundacion.admin.game

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.admin.RefreshGameSopaLetrasPrueba
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class PruebaJuego_sopaletras_basico : BaseActivity(), TextToSpeech.OnInitListener, RefreshGameSopaLetrasPrueba {

    lateinit var jsonArrayDatos : JSONArray
    var position = 0
    var completo = 0
    var botonclick = 1
    var contador = 1

    var letraSelect : String? = "default"
    var letraTag : String? = "default"


    lateinit var txtimage : ImageView
    lateinit var GridBoton : GridLayout

    lateinit var recyclear : RecyclerView


    private lateinit var tts: TextToSpeech


    override fun letraSelect(callback: (String, String) -> Unit) {
        letraSelect?.let { letraTag?.let { it1 -> callback(it, it1) } }
    }

    override fun botonSelect(tag: String) {
        val boton = GridBoton.findViewWithTag<Button>(tag)
        //boton?.visibility = View.GONE
        boton?.isEnabled = true

        letraTag = "default"
        letraSelect = "default"

        val textToSpeak = "letra correcta"
        speakOut(textToSpeak)
    }

    override fun letraincorrecto(tag: String) {
        letraTag = "default"
        letraSelect = "default"

        val boton = GridBoton.findViewWithTag<Button>(tag)
        boton.setBackgroundResource(R.drawable.fondo_boton_sopa_letras)
        val textToSpeak = "la letra incorrecta"
        speakOut(textToSpeak)
    }

    override fun noselect() {
        println("no seleccionado")
        val textToSpeak = "selecciona una letra"
        speakOut(textToSpeak)
    }

    override fun terminado() {
        botonclick++
        println(botonclick)
        println(contador)

        if (botonclick == contador){
            val textToSpeak = "felicidades terminaste quieres volver a jugar"
            speakOut(textToSpeak)
            alertTerminado()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_sopaletras_basico)

        tts = TextToSpeech(this, this)

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(config.NAMEJuegoPrueba)

        txtimage = findViewById(R.id.txtimg)
        GridBoton = findViewById(R.id.botones)

        recyclear = findViewById(R.id.gridView)
        recyclear.layoutManager = LinearLayoutManager(this)

        datos()

    }

    fun datos(){

        Fuel.get("${config.url}admin/preg-sopaletras-simples-all/${config.IDJuegoPrueba}").responseString{ result ->
            result.fold(
                success = { data ->
                    println("Sopa de letras simples los datos $data")
                    jsonArrayDatos = JSONArray(data)
                    println("Sopa de letras simples los datos ${jsonArrayDatos.length()}")

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

    fun dividirPalabrasEnLetras(palabras: List<String>): MutableList<Char> {
        return palabras.flatMap { it.toList()}.toMutableList()
    }

    fun inicio(){
        if (jsonArrayDatos.length() > completo){

            val palabras = mutableListOf<String>()

            for (i in 0 until jsonArrayDatos.length()) {
                val jsonObject = jsonArrayDatos.getJSONObject(i)
                val palabra = jsonObject.getString("palabra")
                palabras.add(palabra)
            }

            val letras = dividirPalabrasEnLetras(palabras)

            letras.shuffle()
            println("Todas las letras juntas: $letras")


            val adapter = AdapterJuegoSopaLetras(this, jsonArrayDatos, this@PruebaJuego_sopaletras_basico)
            recyclear.adapter = adapter



            val columnas = if (letras.size < 2) 2 else 7
            GridBoton.columnCount = columnas

            for (palabra in letras) {
                val button = Button(this)
                button.tag = "boton_$contador"
                button.text = palabra.toString()
                button.layoutParams = LinearLayout.LayoutParams(
                    dpToPx(50),
                    dpToPx(50)
                ).apply {
                    setMargins(5, 5, 5, 5)
                }
                button.setOnClickListener {
                    letraSelect = palabra.toString()
                    letraTag = button.tag.toString()
                    println(letraTag)
                    button.setBackgroundResource(R.drawable.fondo_vocal_select)

                }
                button.setBackgroundResource(R.drawable.fondo_boton_sopa_letras)
                //button.typeface = ResourcesCompat.getFont(this, R.font.digitalt)
                button.setTypeface(null, Typeface.BOLD)
                button.isAllCaps = true
                GridBoton.addView(button)
                contador++
            }


        }
        else{
            val textToSpeak = "felicidades terminaste quieres volver a jugar"
            speakOut(textToSpeak)
            alertTerminado()
        }

    }




    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics
        ).toInt()
    }






    fun retroceder(view: View){

        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.titleText = "ESTAS SEGURO!"
        sweetAlertDialog.contentText = "QUE QUIERES SALIR"
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