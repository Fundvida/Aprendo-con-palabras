package com.example.fundacion.user.torneo_estud

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.admin.Game_Vocal
import com.example.fundacion.admin.game.Adapter_abecedario_gridview
import com.example.fundacion.config
import com.example.fundacion.configurefullScreen
import com.example.fundacion.configurefullScreen_fullview
import com.example.fundacion.user.ESTUDdatos
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class Game_torneo_abecedario_estud : BaseActivity(), Game_Vocal,  TextToSpeech.OnInitListener {


    private lateinit var gridView: GridView
    private lateinit var imageAdapter : Adapter_abecedario_gridview
    var total = 0
    var encontradas = 0
    var faltantes = 0

    private lateinit var etotal : TextView
    private lateinit var efaltante : TextView
    private lateinit var eecontradas : TextView

    private lateinit var tts: TextToSpeech
    var Tvocal : String? = null

    private val datosList = mutableListOf<Pair<String, String>>()

    private lateinit var  txtimage : ImageView

    override fun inicial(img: String, getvocal: String) {
        if (img.toInt() == 0){

        }else{
            total = img.toInt()
            faltantes = img.toInt()
            encontradas = 0
            etotal.setText(total.toString())
            efaltante.setText(faltantes.toString())
            eecontradas.setText(encontradas.toString())
            Tvocal = datosList.find { it.first == getvocal }?.second
            val textToSpeak = "busquemos la letra $Tvocal"
            speakOut(textToSpeak)

        }
    }



    var index : Int? = null
    lateinit var puntaje : TextView
    var pts_ganados = 0
    var pts_fallados = 0
    var correctas = 0
    var incorrectas = 0

    private lateinit var jsonArrayDatos: JSONArray


    var position = 0
    lateinit var txt_time : TextView
    lateinit var txt_timerest : TextView
    private var countDownTimer: CountDownTimer? = null
    private var elapsedMillis = 0L



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_torneo_abecedario_estud)

        tts = TextToSpeech(this, this)

        etotal = findViewById(R.id.total)
        efaltante = findViewById(R.id.faltante)
        eecontradas = findViewById(R.id.encontradas)
        txtimage = findViewById(R.id.txtimg)


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




        val tema = findViewById<TextView>(R.id.tema)
        tema.setText(ESTUDdatos.datos[index!!].nameGAME)



        val imageUrls = mutableListOf<String>()

        Fuel.get("${config.url}admin/preg-abecedario-all/${ESTUDdatos.datos[index!!].idGAME}").responseString{ result ->
            result.fold(
                success = { data ->
                    println(data)
                    jsonArrayDatos = JSONArray(data)
                    if (jsonArrayDatos.length() > 0){
                        for (i in 0 until jsonArrayDatos.length()) {
                            val item = jsonArrayDatos.getJSONObject(i)
                            val imageUrl = item.getString("img")
                            val palabra = item.getString("palabra")
                            imageUrls.add(imageUrl)
                            datosList.add(Pair(imageUrl, palabra))
                        }

                        siguiente(imageUrls)
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

                Log.e("timer", "timer terminado")
                alertTimEnd()
            }
        }

        // Iniciar el contador
        countDownTimer?.start()

    }

    fun alertTimEnd()
    {
        val timeText = txt_timerest.text

        val parts = timeText.split(":")

        val minutes = parts[0].toLong() // "14" -> 14
        val seconds = parts[1].toLong() // "52" -> 52

        val totalMilliseconds = (minutes * 60 + seconds) * 1000

        ESTUDdatos.GameTime = totalMilliseconds

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

        ESTUDdatos.correctas = correctas
        ESTUDdatos.errores = incorrectas

        ESTUDdatos.puntaje_total = ESTUDdatos.puntaje_total + sumar


        dialog.findViewById<Button>(R.id.btn_siguiente).setOnClickListener {
            startActivity(Intent(this, Tabla_final_puntos::class.java))
            finish()
        }


        dialog.show()
        configurefullScreen_fullview(dialog)


    }
    private fun buscarpuntaje(img: String): String? {
        for (i in 0 until jsonArrayDatos.length()) {
            val item = jsonArrayDatos.getJSONObject(i)
            if (item.getString("img") == img) {
                return item.getString("puntaje")
            }
        }
        return null
    }


    fun siguiente(imageUrls: MutableList<String>) {
        val selectedIds = HashSet<Long>()
        gridView = findViewById(R.id.grid_view)

        val totalItem = 4
        imageAdapter = Adapter_abecedario_gridview(this, imageUrls, totalItem, this@Game_torneo_abecedario_estud)
        val copiIMG = imageUrls.toMutableList().shuffled() as MutableList<String>
        imageAdapter.reset(copiIMG[0])
        gridView.adapter = imageAdapter
        gridView.numColumns = 9


        Glide.with(this)
            .load("${config.url}admin/preg-abecedario-imagen/${copiIMG[0]}")
            .into(txtimage)

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (!selectedIds.contains(id)) {
                val vocal = parent.adapter.getItem(position)

                if (vocal.toString() == copiIMG[0]){
                    val textToSpeak = "letra correcta"
                    speakOut(textToSpeak)
                    //view.setBackgroundColor(Color.parseColor("#6F8B9F"))
                    view.background = getDrawable(R.drawable.fondo_vocal_select)
                    selectedIds.add(id)
                    faltantes--
                    encontradas++

                    val valor = buscarpuntaje(copiIMG[0])

                    Log.e("id de vocal", "$valor")

                    pts_ganados = pts_ganados + valor!!.toInt()
                    puntaje.setText(pts_ganados.toString())
                    correctas++
                }else{
                    val textToSpeak = "letra incorrecta"
                    speakOut(textToSpeak)
                    incorrectas++
                    pts_fallados = pts_fallados - 2
                }
            }else{
                val textToSpeak = "la letra ya fue seleccionada!"
                speakOut(textToSpeak)
            }
            efaltante.setText(faltantes.toString())
            eecontradas.setText(encontradas.toString())

            Glide.with(this)
                .load("${config.url}admin/preg-abecedario-imagen/${copiIMG[0]}")
                .into(txtimage)

            if (faltantes == 0){
                copiIMG.removeFirstOrNull()
                if (copiIMG.isEmpty()){
                    gridView.isEnabled = false

                    alertTerminado()

                }else{

                    Handler().postDelayed({
                        imageAdapter.reset(copiIMG[0])
                        faltantes = total
                        selectedIds.clear()
                    }, 200)
                    Glide.with(this)
                        .load("${config.url}admin/preg-abecedario-imagen/${copiIMG[0]}")
                        .into(txtimage)
                }
            }
        }
    }








    fun alertTerminado(){


        countDownTimer?.cancel()

        val timeText = txt_timerest.text

        val parts = timeText.split(":")

        val minutes = parts[0].toLong() // "14" -> 14
        val seconds = parts[1].toLong() // "52" -> 52

        val totalMilliseconds = (minutes * 60 + seconds) * 1000

        Log.e("timer final", "$totalMilliseconds")
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

        ESTUDdatos.correctas = correctas
        ESTUDdatos.errores = incorrectas

        dialog.findViewById<Button>(R.id.btn_salir).setOnClickListener { alertSALIR() }
        dialog.findViewById<Button>(R.id.btn_siguiente).setOnClickListener { Next_Activity() }


        dialog.show()
        configurefullScreen(dialog)

    }


    fun alertFAIL(){
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        sweetAlertDialog.titleText = "NO HAY DATOS"
        sweetAlertDialog.cancelText = "volver"
        sweetAlertDialog.confirmText = "ir inicio"
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::tts.isInitialized && tts.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE) {
            val textToSpeak = "busquemos la letra $Tvocal"
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "AUDIO_AFTER_LOADING")
        }
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