package com.example.fundacion.user

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.TokenManager

class Ueligirnivel : BaseActivity() {


    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ueligirnivel)

        // Crear una instancia de TokenManager
        val tokenManager = TokenManager(this)

        // Obtener el token guardado
        val token = tokenManager.getToken()
        println("Token: $token")

        // Obtener otros datos del usuario
        val userData = tokenManager.getUserData()
        println("User Data: $userData")

        // Puedes acceder a datos específicos como
        val id = userData["id"]
        val nombres = userData["nombres"]
        val apellidos = userData["apellidos"]



        mediaPlayer = MediaPlayer.create(this, R.raw.select_nivel)

        mediaPlayer.isLooping = true
        mediaPlayer.start()


    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

    fun facil(view: View){
        val intent = Intent(this, inicio::class.java)
        startActivity(intent)
        finish()
    }
}