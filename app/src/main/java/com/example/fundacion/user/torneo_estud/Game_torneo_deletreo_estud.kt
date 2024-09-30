package com.example.fundacion.user.torneo_estud

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.user.ESTUDdatos
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class Game_torneo_deletreo_estud : BaseActivity() {

    lateinit var jsonArrayDatos : JSONArray
    var palabrasDeletrear = mutableListOf<Pair<String, String>>()


    private val SPEECH_REQUEST_CODE = 123
    //private val palabrasDeletrear = listOf("fundacion", "educar", "vida", "camino", "sueño")
    private var palabraActualIndex = 0

    private lateinit var palabraActualList: LinearLayout


    var index : Int? = null


    lateinit var txt_time : TextView
    lateinit var txt_timerest : TextView
    private var countDownTimer: CountDownTimer? = null
    private var elapsedMillis = 0L

    lateinit var puntaje : TextView
    var position = 0
    var pts_ganados = 0
    var pts_fallados = 0
    var correctas = 0
    var incorrectas = 0


    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Deletrea la palabra ${palabrasDeletrear[palabraActualIndex].first}")
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Reconocimiento de voz no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    fun datos(){

        Fuel.get("${config.url}admin/preg-deletreo-simples-all/${ESTUDdatos.datos[index!!].idGAME}").responseString{ result ->
            result.fold(
                success = { data ->

                    jsonArrayDatos = JSONArray(data)

                    for (i in 0 until jsonArrayDatos.length()) {
                        val jsonObject = jsonArrayDatos.getJSONObject(i)
                        val palabra = jsonObject.getString("palabra")
                        val puntaje = jsonObject.getString("puntaje")
                        palabrasDeletrear.add(Pair(palabra,puntaje))
                    }

                    actualizarPalabraActual()

                },
                failure = { error ->
                    Log.e("Upload", "Error: ${error.exception.message}")
                }
            )
        }

    }
    lateinit var botonDeletreo : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_torneo_deletreo_estud)


        txt_time = findViewById(R.id.time_total)
        txt_timerest = findViewById(R.id.time_rest)

        timer()

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


        val tema : TextView = findViewById(R.id.tema)
        tema.setText(ESTUDdatos.datos[index!!].nameGAME)

        palabraActualList = findViewById(R.id.letras)


        botonDeletreo = findViewById(R.id.speechToTextButton)
        botonDeletreo.setOnClickListener {
            startSpeechToText()
            botonDeletreo.isEnabled = false
        }



        datos()
    }


    fun siguiente()
    {
        if (palabraActualIndex < palabrasDeletrear.size - 1) {
            palabraActualIndex++
            actualizarPalabraActual()
            botonDeletreo.isEnabled = true

        } else {
            alertTerminado()
        }
    }
    fun timer()
    {

        val millis: Long = ESTUDdatos.GameTime // Milisegundos

        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)

        txt_time.text = "00:00"
        txt_timerest.text = timeFormatted

        val totalTimeInMillis = ESTUDdatos.GameTime

        // Crear el contador regresivo
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                txt_timerest.text = timeFormatted


                // Actualizar tiempo transcurrido
                elapsedMillis += 1000
                val minutesElapsed = elapsedMillis / 1000 / 60
                val secondsElapsed = elapsedMillis / 1000 % 60
                val elapsedFormatted = String.format("%02d:%02d", minutesElapsed, secondsElapsed)
                txt_time.text = elapsedFormatted
            }

            override fun onFinish() {
                txt_timerest.text = "00:00"
                txt_time.text = String.format(
                    "%02d:%02d",
                    totalTimeInMillis / 1000 / 60,
                    totalTimeInMillis / 1000 % 60
                )
                txt_time.text = (totalTimeInMillis / 1000).toString()

                Log.e("timer", "timer terminado")
                alertTimEnd()
            }
        }

        // Iniciar el contador
        countDownTimer?.start()

    }


    fun alertTerminado(){


        countDownTimer?.cancel()

        val timeText = txt_timerest.text

        val parts = timeText.split(":")

        val minutes = parts[0].toLong() // "14" -> 14
        val seconds = parts[1].toLong() // "52" -> 52

        val totalMilliseconds = (minutes * 60 + seconds) * 1000

        ESTUDdatos.GameTime = totalMilliseconds

        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_game_torneo_siguiente_sumpunts)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.findViewById<TextView>(R.id.cant_correctas).text = correctas.toString()
        dialog.findViewById<TextView>(R.id.cant_falladas).text = incorrectas.toString()
        dialog.findViewById<TextView>(R.id.pts_correctas).text = pts_ganados.toString()
        dialog.findViewById<TextView>(R.id.pts_falladas).text = pts_fallados.toString()
        val sumar = pts_ganados+pts_fallados
        ESTUDdatos.puntaje_total = ESTUDdatos.puntaje_total + sumar
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
                finish()
            }
        } else {
            startActivity(Intent(this, Tabla_final_puntos::class.java))
            finish()
        }

    }


    fun alertTimEnd()
    {
        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_modal_game_torneo_time_end)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.findViewById<TextView>(R.id.cant_correctas).text = correctas.toString()
        dialog.findViewById<TextView>(R.id.cant_falladas).text = incorrectas.toString()
        dialog.findViewById<TextView>(R.id.pts_correctas).text = pts_ganados.toString()
        dialog.findViewById<TextView>(R.id.pts_falladas).text = pts_fallados.toString()
        val sumar = pts_ganados+pts_fallados
        dialog.findViewById<TextView>(R.id.total_pts).text = sumar.toString()

        dialog.findViewById<Button>(R.id.btn_siguiente).setOnClickListener {
            startActivity(Intent(this, Tabla_final_puntos::class.java))
        }


        dialog.show()
        configurefullScreen_fullview(dialog)


    }

    private fun actualizarPalabraActual() {
        palabraActualList.removeAllViews()
        println(palabrasDeletrear[palabraActualIndex])

        val palabra = palabrasDeletrear[palabraActualIndex].first
        Log.e("deletreo", "$palabra")

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

        val puntos = palabrasDeletrear[palabraActualIndex].second

        val count = palabraActualList.childCount
        for (i in 0 until count) {
            val textView = palabraActualList.getChildAt(i) as? TextView
            textView?.let {

                println(palabra)

                if (palabra.length > i ){

                    if (palabra[i].toString().lowercase() == textView.text.toString().lowercase()){
                        textView.setBackgroundResource(R.drawable.fondo_deletreo_bien)
                        println(palabra[i])

                        pts_ganados = pts_ganados + puntos.toInt()
                        puntaje.setText(pts_ganados.toString())
                        ESTUDdatos.correctas++
                        correctas++



                    }else
                    {
                        textView.setBackgroundResource(R.drawable.fondo_deletreo_mal)

                        pts_fallados = pts_fallados - 2
                        ESTUDdatos.errores++
                        incorrectas++



                    }
                }else{
                    textView.setBackgroundResource(R.drawable.fondo_deletreo_alert)
                    pts_fallados = pts_fallados - 2
                    ESTUDdatos.errores++
                    incorrectas++
                }


            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main) {

                siguiente()

            }
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
}