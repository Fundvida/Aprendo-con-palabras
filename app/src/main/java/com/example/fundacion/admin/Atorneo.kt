package com.example.fundacion.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.fundacion.R
import com.example.fundacion.admin.adapter.LetterAdapter
import com.example.fundacion.admin.fragment.CanvasDrawingView
import es.dmoral.toasty.Toasty



class Atorneo : AppCompatActivity() {


    val letters = listOf(
        listOf('c', 'o', 'c', 'h', 'a', 'b', 'a', 'm', 'b', 'a'),
        listOf('j', 'u', 'g', 'a', 'r', 'a', 'c', 'i', 't', 'a'),
        listOf('a', 'm', 'i', 'n', 'o', 'c', 'a', 'm', 'i', 'n'),
        listOf('s', 'a', 'c', 'o', 'n', 't', 'e', 'l', 'e', 'v'),
        listOf('a', 'a', 's', 'a', 'c', 'a', 's', 'a', 'i', 'a'),
        listOf('p', 'u', 'e', 'r', 't', 'a', 'p', 'c', 'o', 'm'),
        listOf('u', 't', 'e', 'l', 'e', 'v', 'i', 's', 'o', 'r'),
        listOf('e', 'l', 'a', 'd', 'o', 'c', 'o', 'm', 'i', 'd'),
        listOf('r', 'i', 'j', 'a', 'p', 'c', 'o', 'c', 'a', 's')

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atorneo)

        val gridView: GridView = findViewById(R.id.gridView)
        val adapter = LetterAdapter(this, letters)
        gridView.adapter = adapter
/*
        gridView.setOnItemClickListener { _, view, position, _ ->
            val letter = view.findViewById<TextView>(R.id.gridItemLetter).text.toString()
            // Implementa la lógica para buscar la palabra en la sopa de letras
            searchWord(letter)
        }*/

    }


    private fun searchWord(letter: String) {
        // Implementa la lógica para buscar la palabra en la sopa de letras
        // Puedes usar la matriz de letras y las palabras a buscar aquí

        val wordsToFind = listOf("jugar", "casa", "plato", "cuchara", "Cochabamba")

        if (wordsToFind.contains(letter)) {
            // La palabra fue encontrada
            Log.d("WordSearch", "Palabra encontrada: $letter")
        } else {
            // La palabra no fue encontrada
            Log.d("WordSearch", "Palabra no encontrada: $letter")
        }
    }

}