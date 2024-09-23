package com.example.fundacion

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.media.AudioManager
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton

fun configurefullScreen (dialog: Dialog){
    dialog.window?.let { window ->

        // Oculta la barra de navegación y la barra de estado
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

    }
}
fun configurefullScreen_fullview (dialog: Dialog){
    dialog.window?.let { window ->
        // Configura el tamaño del diálogo para que ocupe toda la pantalla
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        // Oculta la barra de navegación y la barra de estado
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

fun applyAnimationG_P(imageButton: ImageButton) {
    val scaleX = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.75f, 1f)
    val scaleY = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.75f, 1f)

    scaleX.duration = 500
    scaleX.repeatCount = ObjectAnimator.INFINITE
    scaleX.repeatMode = ObjectAnimator.REVERSE

    scaleY.duration = 500
    scaleY.repeatCount = ObjectAnimator.INFINITE
    scaleY.repeatMode = ObjectAnimator.REVERSE

    scaleX.start()
    scaleY.start()
}

fun applyAnimationG_BTN(imageButton: ImageButton) {
    val scaleX = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.9f, 1f)
    val scaleY = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.9f, 1f)

    scaleX.duration = 350
    scaleX.repeatCount = ObjectAnimator.INFINITE
    scaleX.repeatMode = ObjectAnimator.REVERSE

    scaleY.duration = 450
    scaleY.repeatCount = ObjectAnimator.INFINITE
    scaleY.repeatMode = ObjectAnimator.REVERSE

    scaleX.start()
    scaleY.start()
}

fun muteSound(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(
        AudioManager.STREAM_MUSIC,
        AudioManager.ADJUST_MUTE,
        0
    )
}

fun unmuteSound(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(
        AudioManager.STREAM_MUSIC,
        AudioManager.ADJUST_UNMUTE,
        0
    )
}

fun SubirVolumen(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(
        AudioManager.STREAM_MUSIC,
        AudioManager.ADJUST_RAISE,
        //AudioManager.FLAG_SHOW_UI
    0
    )
}

fun BajarVolumen(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(
        AudioManager.STREAM_MUSIC,
        AudioManager.ADJUST_LOWER,
        //AudioManager.FLAG_SHOW_UI
    0
    )
}
