package com.example.fundacion.admin.game

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.fundacion.BaseActivity
import com.example.fundacion.R

class PruebaJuego_deletreo_simple : BaseActivity() {
    private val SPEECH_REQUEST_CODE = 123
    private val palabrasDeletrear = listOf("fundacion", "educar", "vida", "camino", "sueño")
    private var palabraActualIndex = 0

    private lateinit var palabraActualTextView: TextView
    private lateinit var escuchada: TextView
    private lateinit var palabraActualList: LinearLayout

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Deletrea la palabra ${palabrasDeletrear[palabraActualIndex]}")
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Reconocimiento de voz no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_deletreo_simple)

        palabraActualList = findViewById(R.id.letras)
        palabraActualTextView = findViewById(R.id.palabraActualTextView)
        escuchada = findViewById(R.id.escuchada)

        actualizarPalabraActual()

        val botonDeletreo = findViewById<Button>(R.id.speechToTextButton)
        botonDeletreo.setOnClickListener {
            startSpeechToText()
        }

        val siguiente = findViewById<Button>(R.id.siguiente)
        siguiente.setOnClickListener {
            if (palabraActualIndex < palabrasDeletrear.size - 1) {
                palabraActualIndex++
                actualizarPalabraActual()
            } else {
                // Todas las palabras han sido deletreadas
                Toast.makeText(this, "Todas las palabras han sido deletreadas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarPalabraActual() {
        palabraActualList.removeAllViews()
        palabraActualTextView.text = palabrasDeletrear[palabraActualIndex]
        println(palabrasDeletrear[palabraActualIndex])

        val palabra = palabrasDeletrear[palabraActualIndex]

        for ((index, letra) in palabra.withIndex()) {
            val textView = TextView(this)
            textView.text = letra.toString()
            textView.textSize = 24f // Tamaño de texto ajustable según tu preferencia
            textView.setPadding(10, 0, 10, 0) // Ajusta el padding según tu diseño
            textView.id = index // Asignar un ID único a cada TextView
            textView.isAllCaps = true
            palabraActualList.addView(textView)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                val palabraDeletreada = result[0]
                val textoVerdadero = obtenerTextoSinEspacios(palabraDeletreada)

                deletrearLetras(textoVerdadero)
            }
        }
    }

    private fun obtenerValorTextView(id: Int): String? {
        val textView = findViewById<TextView>(id)
        return textView?.text?.toString()
    }

    private fun obtenerTextoSinEspacios(texto: String): String {
        return texto.replace("\\s".toRegex(), "")
    }




    private fun deletrearLetras(palabra: String) {
        val palabraCorrecta = palabrasDeletrear[palabraActualIndex]


        val count = palabraActualList.childCount
        for (i in 0 until count) {
            val textView = palabraActualList.getChildAt(i) as? TextView
            textView?.let {

                if (palabra.length > i ){

                    if (palabra[i].toString().lowercase() == textView.text.toString().lowercase()){
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bien, 0, 0, 0)
                    }else
                    {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.error, 0, 0, 0)
                    }
                }else{
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alert, 0, 0, 0)
                }

            }
        }

        escuchada.setText(palabra)
        /*
        if (palabra.toLowerCase() == palabraCorrecta.toLowerCase()) {
            // Palabra correcta, puedes hacer lo que necesites aquí
            Toast.makeText(this, "Palabra correcta", Toast.LENGTH_SHORT).show()
            // Pasar a la siguiente palabra si no es la última
            if (palabraActualIndex < palabrasDeletrear.size - 1) {
                palabraActualIndex++
 //               actualizarPalabraActual()
            } else {
                // Todas las palabras han sido deletreadas
                Toast.makeText(this, "Todas las palabras han sido deletreadas", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Palabra incorrecta, puedes hacer lo que necesites aquí
            Toast.makeText(this, "Palabra incorrecta", Toast.LENGTH_SHORT).show()
        }*/
    }
}