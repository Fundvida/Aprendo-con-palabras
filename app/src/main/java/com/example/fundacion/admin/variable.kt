package com.example.fundacion.admin

import android.graphics.Bitmap

data class lusuarios (
    val id: String,
    val nombres: String,
    val apellidos: String,
    val ci: String,
    val celular: String,
    val codigo: String,
    val nacimiento: String,
    val pais: String,
    val ciudad: String,
    val email: String,
    val user: String,
    val pass: String,
    val rol: String,
    val estado: String

    )

data class lusuariosAn (
    val id: String,
    val nombres: String,
    val apellidos: String,
    val ci: String,
    val celular: String,
    val codigo: String,
    val nacimiento: String,
    val pais: String,
    val ciudad: String,
    val email: String,
    val user: String,
    val pass: String,
    val rol: String,
    val estado: String

    )

data class lgames (
    val id: String,
    val tema: lTema,
    val tarea: String,
    val tiempo: String,
    val estado: String

    )
data class lTema(
    val id: Int,
    val nivel: lNivel,
    val tema: String,
    val estado: String
)

data class lNivel(
    val id: Int,
    val tipo: String,
    val estado: String
)

data class ArrayNivel(val id: Int, val tipo: String, val estado: Int)
data class ArrayTema(val id: Int, val nivel: Int, val tema: String, val estado: Int)



data class CGame(
    val id: Int,
    val tipo: String,
    val estado: Int,
    val temas: List<Topic>
)
data class Topic(
    val id: Int,
    val nivel: Int,
    val tema: String,
    val estado: Int
)
data class Tarea(
    val id: Int,
    val tareas: Int,
    val palabra: String,
    val respuesta: String,
    val img: String,
    val puntaje: Int?,
    val estado: Int
)

data class Datos(
    val datos: List<Tarea>
)

data class ImageModel(var bitmap: Bitmap, var left: Float, var top: Float) {
    val right: Float
        get() = left + bitmap.width
    val bottom: Float
        get() = top + bitmap.height
}

data class lpreguntas(
    val id: String,
    val tarea: String,
    val palabra: String,
    val respuesta: String,
    val img: String,
    val puntaje: String,
    val estado: String,
)