package com.example.fundacion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavegation()

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {

                Handler(Looper.getMainLooper()).postDelayed({
                    hideNavegation()
                }, 2000)
                hideNavegation()

            }
        }

/*
        // Configura el OnKeyListener para interceptar eventos de volumen
        findViewById<View>(R.id.someView).setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_VOLUME_UP -> {
                        // Acción para el botón de subir volumen
                        Toast.makeText(this, "Volume Up Pressed", Toast.LENGTH_SHORT).show()
                        true
                    }
                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
                        // Acción para el botón de bajar volumen
                        Toast.makeText(this, "Volume Down Pressed", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }*/

    }
    private fun hideNavegation() {
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }


    override fun onBackPressed() {
        // No hacer nada para deshabilitar el botón atrás
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        /*if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            // Ignorar el botón home y el botón de aplicaciones recientes
            return false
        }
        return super.onKeyDown(keyCode, event)*/


        if (event != null && event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    // Acción para el botón de subir volumen
                  //  Toast.makeText(this, "Volume Up Pressed", Toast.LENGTH_SHORT).show()
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    // Acción para el botón de bajar volumen
                 //   Toast.makeText(this, "Volume Down Pressed", Toast.LENGTH_SHORT).show()
                    return true
                }
                else -> return super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            // Ignorar el botón home y el botón de aplicaciones recientes
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

}