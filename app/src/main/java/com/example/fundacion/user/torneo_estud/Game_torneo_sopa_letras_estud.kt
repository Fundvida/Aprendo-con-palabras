package com.example.fundacion.user.torneo_estud

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.fundacion.admin.game.AdapterJuegoSopaLetras
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.user.ESTUDdatos
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class Game_torneo_sopa_letras_estud : BaseActivity(), TextToSpeech.OnInitListener,
    RefreshGameSopaLetrasPrueba {

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

    override fun puntajes(x: String, y: String) {


        val puntos = x.toInt()*y.toInt()
        pts_ganados = pts_ganados + puntos!!.toInt()
        puntaje.setText(pts_ganados.toString())

        ESTUDdatos.correctas++
        correctas++
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

        pts_fallados = pts_fallados - 2
        ESTUDdatos.errores++
        incorrectas++
    }

    override fun noselect() {
        println("no seleccionado")
        val textToSpeak = "selecciona una letra"
        speakOut(textToSpeak)
    }

    override fun terminado() {
        botonclick++

        if (botonclick == contador){
            alertTerminado()
        }

    }


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
        setContentView(R.layout.activity_game_torneo_sopa_letras_estud)



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

        val tema : TextView = findViewById(R.id.tema)
        tema.setText(ESTUDdatos.datos[index!!].nameGAME)

        txtimage = findViewById(R.id.txtimg)
        GridBoton = findViewById(R.id.botones)

        recyclear = findViewById(R.id.gridView)
        recyclear.layoutManager = LinearLayoutManager(this)

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

        Fuel.get("${config.url}admin/preg-sopaletras-simples-all/${ESTUDdatos.datos[index!!].idGAME}").responseString{ result ->
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


            val adapter = AdapterJuegoSopaLetras(this, jsonArrayDatos, this@Game_torneo_sopa_letras_estud)
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