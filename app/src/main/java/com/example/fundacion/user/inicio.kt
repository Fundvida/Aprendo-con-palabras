package com.example.fundacion.user

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R

class inicio : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


    }

    fun whatsapp(view: View){
        val phoneNumber = "59174025156"
        val message = "¡Hola! ¿Cómo estás?" // El mensaje que deseas enviar

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra("jid", "$phoneNumber@s.whatsapp.net")
        startActivity(intent)

    }

    fun instagram(view: View){
        val username = "fundeducarparalavida"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.instagram.com/$username/")
        startActivity(intent)
    }

    fun facebook(view: View){
        val facebookPageId = "100063510101095"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.facebook.com/$facebookPageId")
        startActivity(intent)
    }
}