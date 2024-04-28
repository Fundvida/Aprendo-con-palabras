package com.example.fundacion.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.fundacion.R

class LetterAdapter(private val context: Context, private val letters: List<List<Char>>) : BaseAdapter() {

    override fun getCount(): Int {
        return letters.size
    }

    override fun getItem(position: Int): Any {
        return letters[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(context)
        val word = letters[position].joinToString("") // Convertir la lista de caracteres a una cadena
        textView.text = word
        return textView
    }
}