package com.example.fundacion
import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.widget.Toast

class CustomDialog(context: Context) : Dialog(context) {

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event != null && event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    // Acción para el botón de subir volumen
                    Toast.makeText(context, "Volume Up Pressed in Dialog", Toast.LENGTH_SHORT).show()
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    // Acción para el botón de bajar volumen
                    Toast.makeText(context, "Volume Down Pressed in Dialog", Toast.LENGTH_SHORT).show()
                    return true
                }
                else -> return super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event!!)
    }
}
