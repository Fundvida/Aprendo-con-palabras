package com.example.fundacion.admin.game

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.Ainicio
import com.example.fundacion.admin.Game_Vocal
import com.example.fundacion.admin.adapter.ImageAdapter
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import java.util.Locale

class PruebaJuego_vocal : BaseActivity(), Game_Vocal, TextToSpeech.OnInitListener {

    private lateinit var gridView: GridView
    private lateinit var imageAdapter : Adapter_vocales_gridview
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
            val textToSpeak = "busquemos la vocal $Tvocal"
            speakOut(textToSpeak)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_vocal)

        tts = TextToSpeech(this, this)

        etotal = findViewById(R.id.total)
        efaltante = findViewById(R.id.faltante)
        eecontradas = findViewById(R.id.encontradas)
        txtimage = findViewById(R.id.txtimg)

        val tema = findViewById<TextView>(R.id.tema)
        tema.setText(config.NAMEJuegoPrueba)


        val imageUrls = mutableListOf<String>()

        Fuel.get("${config.url}admin/preg-vocal-all/${config.IDJuegoPrueba}").responseString{ result ->
            result.fold(
                success = { data ->
                    println(data)
                    val jsonArrayDatos = JSONArray(data)
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


    fun siguiente(imageUrls: MutableList<String>) {
        val selectedIds = HashSet<Long>()
        gridView = findViewById(R.id.grid_view)

        val totalItem = 48
        imageAdapter = Adapter_vocales_gridview(this, imageUrls, totalItem, this@PruebaJuego_vocal)
        val copiIMG = imageUrls.toMutableList().shuffled() as MutableList<String>
        imageAdapter.reset(copiIMG[0])
        gridView.adapter = imageAdapter
        gridView.numColumns = 12


        Glide.with(this)
            .load("${config.url}admin/preg-vocal-imagen/${copiIMG[0]}")
            .into(txtimage)

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (!selectedIds.contains(id)) {
                val vocal = parent.adapter.getItem(position)

                if (vocal.toString() == copiIMG[0]){
                    val textToSpeak = "vocal correcta"
                    speakOut(textToSpeak)
                    //view.setBackgroundColor(Color.parseColor("#6F8B9F"))
                    view.background = getDrawable(R.drawable.fondo_vocal_select)
                    selectedIds.add(id)
                    faltantes--
                    encontradas++
                }else{
                    val textToSpeak = "vocal incorrecta"
                    speakOut(textToSpeak)
                }
            }else{
                val textToSpeak = "la vocal ya fue seleccionada!"
                speakOut(textToSpeak)
            }
            efaltante.setText(faltantes.toString())
            eecontradas.setText(encontradas.toString())

            Glide.with(this)
                .load("${config.url}admin/preg-vocal-imagen/${copiIMG[0]}")
                .into(txtimage)

            if (faltantes == 0){
                copiIMG.removeFirstOrNull()
                if (copiIMG.isEmpty()){
                    gridView.isEnabled = false

                    val textToSpeak = "felicidades terminaste quieres volver a jugar"
                    speakOut(textToSpeak)
                    alertTerminado()

                }else{
                    val textToSpeak = "bien ya encontramos todas las vocales $Tvocal, ahora sigamos y"
                    speakOut(textToSpeak)
                    Handler().postDelayed({
                        imageAdapter.reset(copiIMG[0])
                        faltantes = total
                        selectedIds.clear()
                    }, 5000)
                    Glide.with(this)
                        .load("${config.url}admin/preg-vocal-imagen/${copiIMG[0]}")
                        .into(txtimage)
                }
            }
        }
    }

    fun alertTerminado(){
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        sweetAlertDialog.titleText = "FELICIDADES TERMINASTE!"
        sweetAlertDialog.contentText = "QUIERES VOLVER A JUGAR"
        sweetAlertDialog.confirmText = "si"
        sweetAlertDialog.cancelText = "no"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmClickListener {
            recreate()

            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.setCancelClickListener {
            finish()
            sweetAlertDialog.dismissWithAnimation()
        }
        sweetAlertDialog.show()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::tts.isInitialized && tts.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE) {
            val textToSpeak = "busquemos la vocal $Tvocal"
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