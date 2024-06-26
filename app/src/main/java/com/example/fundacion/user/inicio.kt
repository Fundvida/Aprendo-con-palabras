package com.example.fundacion.user

import TareaAdapter
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class inicio : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

    }

    fun retroceder(view: View){
        finish()
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


    private lateinit var recyy : RecyclerView
    fun gamelist(int: Int){
        Fuel.get("${config.url}user/game/$int")
            .responseString{ _, _, result ->
                result.fold(
                    success = { d ->
                        println(d)

                        val tareaList: List<Tarea> = parseJsonToTareaList(d)

                        recyy.adapter = TareaAdapter(tareaList)
                    },
                    failure = {error ->

                        println("Error en la solicitud: $error")
                    }
                )
            }

        val dialog = Dialog(this, R.style.TransparentDialog)
        dialog.setContentView(R.layout.uuu_model_inicio_list)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        recyy = dialog.findViewById(R.id.recy)
        recyy.layoutManager = LinearLayoutManager(this)

        val close = dialog.findViewById<ImageButton>(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }





        dialog.show()
    }


    private fun parseJsonToTareaList(jsonString: String): List<Tarea> {
        val gson = Gson()
        val type = object : TypeToken<List<Tarea>>() {}.type
        return gson.fromJson(jsonString, type)
    }



    fun vocales(view: View){
        gamelist(1)
    }

    fun abecedario(view: View){
        gamelist(2)
    }

    fun silabas(view: View){
        gamelist(3)
    }

    fun deletreo(view: View){
        gamelist(4)
    }

    fun sopaletras(view: View){
        gamelist(5)
    }

    fun oraciones(view: View){
        gamelist(6)
    }


    fun torneo(view: View){
    }

}