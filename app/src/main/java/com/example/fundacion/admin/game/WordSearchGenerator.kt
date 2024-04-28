package com.example.fundacion.admin.game

import org.json.JSONArray
import org.json.JSONObject

class WordSearchGenerator(jsonString: String) {
    private val palabras: MutableList<Char> = mutableListOf()

    init {
        obtenerLetrasDesdeJSON(jsonString)
    }

    // Función para obtener todas las letras de las palabras en el JSON
    private fun obtenerLetrasDesdeJSON(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        val datosArray = jsonObject.getJSONArray("datos")
        for (i in 0 until datosArray.length()) {
            val palabra = datosArray.getJSONObject(i).getString("palabra")
            palabras.addAll(palabra.toCharArray().toList())
        }
    }

    // Función para mezclar aleatoriamente las letras
    private fun mezclarLetras(): List<Char> {
        val letrasMezcladas = palabras.shuffled().toMutableList()
        // Agregar más letras aleatorias para llenar el espacio
        while (letrasMezcladas.size < 40) {
            letrasMezcladas.add(('a'..'z').random())
        }
        return letrasMezcladas
    }

    // Función para crear la matriz de la sopa de letras
    fun crearSopaDeLetras(): Array<Array<Char>> {
        val letrasMezcladas = mezclarLetras()
        val matriz = Array(6) { Array(10) { ' ' } }
        var index = 0
        for (i in 0 until 6) {
            for (j in 0 until 10) {
                matriz[i][j] = letrasMezcladas[index]
                index++
            }
        }
        return matriz
    }
}
