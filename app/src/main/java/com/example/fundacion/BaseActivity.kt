package com.example.fundacion

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavegation()

        /*
        //cambiar el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = resources.getColor(R.color.azul, theme)
        } else {
            // En versiones anteriores a Android Oreo, el cambio de color de fondo de la barra de navegación no es posible
        }
        */

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
                /*
                //visible
                Handler(Looper.getMainLooper()).postDelayed({
                    hideNavegation()
                }, 2000)
                */
                //invisible
                Thread.sleep(2000)
                hideNavegation()

            }
        }

    }
    private fun hideNavegation() {
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }
}