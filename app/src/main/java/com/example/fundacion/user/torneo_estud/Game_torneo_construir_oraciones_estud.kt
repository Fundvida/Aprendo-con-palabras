package com.example.fundacion.user.torneo_estud

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.fundacion.BarButtonOFF
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.user.ESTUDdatos
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class Game_torneo_construir_oraciones_estud : BarButtonOFF(), TextToSpeech.OnInitListener {

    lateinit var jsonArrayDatos : JSONArray
    var position = 0
    var botonclick = 0
    var oracionV : String? = null
    var puntosV : String? = null

    lateinit var txtimage : ImageView
    lateinit var GridBoton : GridLayout
    lateinit var txtoracion : TextView


    private lateinit var tts: TextToSpeech


    var index : Int? = null


    lateinit var txt_time : TextView
    lateinit var txt_timerest : TextView
    private var countDownTimer: CountDownTimer? = null
    private var elapsedMillis = 0L

    var pts_ganados = 0
    var pts_fallados = 0
    var correctas = 0
    var incorrectas = 0
    lateinit var puntaje : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_torneo_construir_oraciones_estud)



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




        tts = TextToSpeech(this, this)

        Handler(Looper.getMainLooper()).postDelayed({
            speakOut("ordena la oracion")
        }, 2000)

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(ESTUDdatos.datos[index!!].nameGAME)

        txtimage = findViewById(R.id.txtimg)
        GridBoton = findViewById(R.id.botones)
        txtoracion = findViewById(R.id.textoracion)
        datos()

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

    fun datos(){

        Fuel.get("${config.url}admin/preg-oraciones-simples-all/${ESTUDdatos.datos[index!!].idGAME}").responseString{ result ->
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
            puntosV = item.getString("puntaje").toString()


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

    fun siguiente(){
        if(txtoracion.text == oracionV){
            println("la oracion es correcta")
            GridBoton.removeAllViews()
            GridBoton.invalidate()
            alertCorrecto()

            pts_ganados = pts_ganados + puntosV!!.toInt()
            puntaje.setText(pts_ganados.toString())

            ESTUDdatos.correctas++
            correctas++

            val textToSpeak = "la oracion es correcta"
            speakOut(textToSpeak)
        }else{
            println("la oracion era $oracionV")
            GridBoton.removeAllViews()
            GridBoton.invalidate()
            alertIncorrecto()

            pts_fallados = pts_fallados - 2
            ESTUDdatos.errores++
            incorrectas++

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