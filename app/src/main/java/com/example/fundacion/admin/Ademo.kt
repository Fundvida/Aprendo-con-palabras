package com.example.fundacion.admin




import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.CanvasDrawingView
import com.example.fundacion.admin.game.cartamay
import com.example.fundacion.admin.game.cartamin
import com.example.fundacion.admin.game.juego_vocal_A
import com.example.fundacion.admin.game.minImprenta
import java.util.Locale

class Ademo : BaseActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeak: Button

    private lateinit var btn: LinearLayout
    private lateinit var basico: LinearLayout
    private lateinit var A: LinearLayout
    private lateinit var a: LinearLayout
    private lateinit var ac: LinearLayout
    private lateinit var Ac: LinearLayout

    private var canvasView: juego_vocal_A? = null
    private var canvasViewA: minImprenta? = null
    private var canvasViewb: cartamin? = null
    private var canvasViewB: cartamay? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ademo)


        btn = findViewById(R.id.btn)
        basico = findViewById(R.id.basico)
        A = findViewById(R.id.A)
        a = findViewById(R.id.a)
        Ac = findViewById(R.id.Ac)
        ac = findViewById(R.id.ac)
        tts = TextToSpeech(this, this)

        canvasView = findViewById(R.id.Mayu_imp)
        canvasViewA = findViewById(R.id.Min_imp)
        canvasViewb = findViewById(R.id.Min_cart)
        canvasViewB = findViewById(R.id.May_cart)

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
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
    }


    fun basico(view: View){
        btn.visibility = View.GONE
        a.visibility = View.GONE
        ac.visibility = View.GONE
        A.visibility = View.GONE
        Ac.visibility = View.GONE
        basico.visibility = View.VISIBLE
    }
    fun lectura(view: View){
        btn.visibility = View.GONE
        basico.visibility = View.GONE
        a.visibility = View.GONE
        ac.visibility = View.GONE

        A.visibility = View.VISIBLE
        Ac.visibility = View.GONE
    }
    fun escritura(view: View){
        btn.visibility = View.GONE
        basico.visibility = View.GONE
        a.visibility = View.VISIBLE
        ac.visibility = View.GONE

        A.visibility = View.GONE
        Ac.visibility = View.GONE
    }
    fun vm(view: View){
        btn.visibility = View.GONE
        basico.visibility = View.GONE
        a.visibility = View.GONE
        ac.visibility = View.VISIBLE

        A.visibility = View.GONE
        Ac.visibility = View.GONE

    }
    fun vma(view: View){
        btn.visibility = View.GONE
        basico.visibility = View.GONE
        a.visibility = View.GONE
        ac.visibility = View.GONE

        A.visibility = View.GONE
        Ac.visibility = View.VISIBLE
    }









    fun aa(view: android.view.View){
        /*canvasView?.let { canvas ->
            // Verifica si aún quedan letras por buscar
            if (canvas.remainingLetters.isNotEmpty()) {
                // Obtiene la próxima letra a buscar
                val nextLetter = canvas.remainingLetters.removeAt(0)
                // Actualiza la letra actual en CanvasDrawingView
               // canvas.setCurrentLetter(nextLetter)

                canvas.shuffleGrid()
                // Refresca la vista para iniciar la búsqueda de la nueva letra
                //canvas.invalidate()
                // Muestra un mensaje indicando la letra que se está buscando
                Toast.makeText(this, "Buscando la letra $nextLetter", Toast.LENGTH_SHORT).show()
            } else {
                // Si no quedan letras por buscar, muestra un mensaje de finalización
                Toast.makeText(this, "Se han buscado todas las letras", Toast.LENGTH_SHORT).show()
            }
        }*/
    }
    fun aaa(view: android.view.View){
        canvasViewA?.let { canvas ->
            // Verifica si aún quedan letras por buscar
            if (canvas.remainingLetters.isNotEmpty()) {
                // Obtiene la próxima letra a buscar
                val nextLetter = canvas.remainingLetters.removeAt(0)
                // Actualiza la letra actual en CanvasDrawingView
                canvas.setCurrentLetter(nextLetter)

                canvas.shuffleGrid()
                // Refresca la vista para iniciar la búsqueda de la nueva letra
                //canvas.invalidate()
                // Muestra un mensaje indicando la letra que se está buscando
                Toast.makeText(this, "Buscando la letra $nextLetter", Toast.LENGTH_SHORT).show()
            } else {
                // Si no quedan letras por buscar, muestra un mensaje de finalización
                Toast.makeText(this, "Se han buscado todas las letras", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun bb(view: android.view.View){
        canvasViewB?.let { canvas ->
            // Verifica si aún quedan letras por buscar
            if (canvas.remainingLetters.isNotEmpty()) {
                // Obtiene la próxima letra a buscar
                val nextLetter = canvas.remainingLetters.removeAt(0)
                // Actualiza la letra actual en CanvasDrawingView
                canvas.setCurrentLetter(nextLetter)

                canvas.shuffleGrid()
                // Refresca la vista para iniciar la búsqueda de la nueva letra
                //canvas.invalidate()
                // Muestra un mensaje indicando la letra que se está buscando
                Toast.makeText(this, "Buscando la letra $nextLetter", Toast.LENGTH_SHORT).show()
            } else {
                // Si no quedan letras por buscar, muestra un mensaje de finalización
                Toast.makeText(this, "Se han buscado todas las letras", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun bbb(view: android.view.View){
        canvasViewb?.let { canvas ->
            // Verifica si aún quedan letras por buscar
            if (canvas.remainingLetters.isNotEmpty()) {
                // Obtiene la próxima letra a buscar
                val nextLetter = canvas.remainingLetters.removeAt(0)
                // Actualiza la letra actual en CanvasDrawingView
                canvas.setCurrentLetter(nextLetter)

                canvas.shuffleGrid()
                // Refresca la vista para iniciar la búsqueda de la nueva letra
                //canvas.invalidate()
                // Muestra un mensaje indicando la letra que se está buscando
                Toast.makeText(this, "Buscando la letra $nextLetter", Toast.LENGTH_SHORT).show()
            } else {
                // Si no quedan letras por buscar, muestra un mensaje de finalización
                Toast.makeText(this, "Se han buscado todas las letras", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun i(view: View){
        val textToSpeak = "incorrecto es la vocal i"
        speakOut(textToSpeak)
    }
    fun o(view: View){
        val textToSpeak = "incorrecto es la vocal o"

        speakOut(textToSpeak)
    }
    fun u(view: View){
        val textToSpeak = "incorrecto es la vocal u"

        speakOut(textToSpeak)
    }
}