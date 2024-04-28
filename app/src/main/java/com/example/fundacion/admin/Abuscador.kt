package com.example.fundacion.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.CanvasView
import com.example.fundacion.admin.game.CustomCanvasDrawingView
import com.example.fundacion.config
import com.google.gson.Gson
import es.dmoral.toasty.Toasty

class Abuscador : BaseActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abuscador)

        val canvasView = findViewById<CanvasView>(R.id.canvasView)
        val imagenesURL = listOf(
            "${config.url}admin/preg-images/PA.png",
            "${config.url}admin/preg-images/LA.png",
            "${config.url}admin/preg-images/SA.png"
        )
        canvasView.LoadImageTask().execute(imagenesURL)

        val veri = findViewById<Button>(R.id.verificar)
        val imagen : ImageView = findViewById(R.id.imageView)
        val text : TextView = findViewById(R.id.textView)
        val imagen2 : ImageView = findViewById(R.id.imageView2)

        Glide.with(this)
            .load("${config.url}admin/preg-images/CA.png")
            .into(imagen)

        veri.setOnClickListener {
            val indexSelect = canvasView.verificar().toList()
            if (indexSelect.isEmpty()){
                Toasty.warning(this,"Necesitas colocar una imagen en el guion", Toasty.LENGTH_SHORT).show()
            }else if (indexSelect.size > 1){
                Toasty.error(this, "Solo debes tener una imagen en el guion", Toasty.LENGTH_SHORT).show()
            }else{
                println("Quien es aqui")
                text.visibility = View.GONE
                imagen2.visibility = View.VISIBLE
                Glide.with(this)
                    .load(imagenesURL[indexSelect[0].toInt()])
                    .into(imagen2)
            }


            //println("este es el indexSElect =="+indexSelect[0])
            println(imagenesURL)
         //   println("este es el image ==="+imagenesURL[indexSelect[0].toInt()])

            //val imagess = canvasView.refresh()
           /* if (imagess.isEmpty()){
                println(imagess)





                /*canvasView.LoadImageTask().execute(
                    listOf(
                        "${config.url}admin/preg-images/PA.png",
                        "${config.url}admin/preg-images/LA.png",
                        "${config.url}admin/preg-images/SA.png",
                        "${config.url}admin/preg-images/ZO.png",
                        "${config.url}admin/preg-images/RRO.png",
                        "${config.url}admin/preg-images/ME.png"
                    )
                )*/
            }*/

        }
    }

}

