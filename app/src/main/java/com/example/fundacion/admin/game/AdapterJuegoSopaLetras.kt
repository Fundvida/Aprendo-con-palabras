package com.example.fundacion.admin.game

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundacion.R
import com.example.fundacion.admin.RefreshGameSopaLetrasPrueba
import com.example.fundacion.config
import org.json.JSONArray
import org.json.JSONObject

class AdapterJuegoSopaLetras (
    private val context: Context,
    private val dataArray: JSONArray,
    private val letraClick: RefreshGameSopaLetrasPrueba
    ) : RecyclerView.Adapter<AdapterJuegoSopaLetras.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imagenview)
        val squaresContainer: LinearLayout = view.findViewById(R.id.squaresContainer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.aaa_view_sopaletras_game, parent, false)
        return ViewHolder(view)
    }

    var json = JSONObject()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataArray.getJSONObject(position)


        val imageUrl = item.getString("img")
        Glide.with(context)
            .load("${config.url}admin/preg-sopaletras-simples-imagen/$imageUrl")
            .into(holder.imageView)

        holder.squaresContainer.visibility = View.VISIBLE


        val palabra = item.getString("palabra")
        val puntos = item.getString("puntaje")
        val texto = palabra.toList()

        holder.squaresContainer.removeAllViews()

        for (i in 1..item.getString("palabra").length) {

            val valor = i-1
            val texto = item.getString("palabra").toList()

            Log.e("twxt", "$texto")

            val text = TextView(context)
            text.layoutParams = LinearLayout.LayoutParams(
                dpToPx(30),
                dpToPx(30)
            ).apply {
                setMargins(4, 0, 4, 0) // Optional: margins
            }

            text.setBackgroundResource(R.drawable.fondo_sopa_letras_simples)
            text.gravity = Gravity.CENTER

            text.setOnClickListener {
                letraClick.letraSelect { result, tag ->

                    if (result == "default"){

                        letraClick.noselect()

                    }else if (texto[valor].toString() == result){

                        text.setOnClickListener(null)

                        text.isAllCaps = true
                        text.setText(result)
                        text.textSize = 20f
                        text.setTypeface(null, Typeface.BOLD)
                        text.setBackgroundResource(R.drawable.fondo_sopa_letras_simples)
                        text.setTextColor(context.resources.getColor(R.color.white))
                        text.gravity = Gravity.CENTER

                        letraClick.botonSelect(tag)
                        letraClick.terminado()
                        if (json.has("$position")){
                            println("si existe")
                            val new = json.getJSONObject("$position")
                            new.put("$i", result)

                        }else{
                            println("no existe")
                            json.put("$position", JSONObject().apply {
                                put("$i", result)
                            })

                        }

                        if (json.getJSONObject("$position").length() == palabra.length) {
                            letraClick.puntajes(palabra.length.toString(), "$puntos")
                        }


                    }
                    else
                    {
                        println("no es la vocal correcta")
                        letraClick.letraincorrecto(tag)
                    }


                }


            }

            holder.squaresContainer.addView(text)
        }


    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
        ).toInt()
    }


}