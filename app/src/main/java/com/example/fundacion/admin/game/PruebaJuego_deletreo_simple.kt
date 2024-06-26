package com.example.fundacion.admin.game

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray

class PruebaJuego_deletreo_simple : BaseActivity() {

    lateinit var jsonArrayDatos : JSONArray
    var palabrasDeletrear = mutableListOf<String>()


    private val SPEECH_REQUEST_CODE = 123
    //private val palabrasDeletrear = listOf("fundacion", "educar", "vida", "camino", "sueño")
    private var palabraActualIndex = 0

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


    fun datos(){

        Fuel.get("${config.url}admin/preg-deletreo-simples-all/${config.IDJuegoPrueba}").responseString{ result ->
            result.fold(
                success = { data ->
                    println("los datos son ${config.IDJuegoPrueba} ===>  "+data)


                    val jsonArray = JSONArray(data)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val palabra = jsonObject.getString("palabra")
                        palabrasDeletrear.add(palabra)
                    }

                    actualizarPalabraActual()

                },
                failure = { error ->
                    Log.e("Upload", "Error: ${error.exception.message}")
                }
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_deletreo_simple)


        val tema : TextView = findViewById(R.id.tema)
        tema.setText(config.NAMEJuegoPrueba)

        palabraActualList = findViewById(R.id.letras)


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

        datos()

    }

    private fun actualizarPalabraActual() {
        palabraActualList.removeAllViews()
        println(palabrasDeletrear[palabraActualIndex])

        val palabra = palabrasDeletrear[palabraActualIndex]

        for ((index, letra) in palabra.withIndex()) {
            val textView = TextView(this)
            textView.text = letra.toString()

            textView.textSize = 50f // Tamaño de texto ajustable según tu preferencia
            textView.setPadding(0, 0, 0, 0) // Ajusta el padding según tu diseño
            textView.id = index // Asignar un ID único a cada TextView
            textView.isAllCaps = true
            textView.setBackgroundResource(R.drawable.fondo_deletreo_normal)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))


            textView.layoutParams = LinearLayout.LayoutParams(
                dpToPx(60),
                dpToPx(60)
            ).apply {
                setMargins(15, 5, 15, 5)
                gravity = Gravity.CENTER
            }

            palabraActualList.addView(textView)
        }


    }



    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics
        ).toInt()
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
                        textView.setBackgroundResource(R.drawable.fondo_deletreo_bien)
                    }else
                    {
                        textView.setBackgroundResource(R.drawable.fondo_deletreo_mal)
                    }
                }else{
                    textView.setBackgroundResource(R.drawable.fondo_deletreo_alert)
                }

            }
        }

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
}