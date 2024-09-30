package com.example.fundacion.admin

import android.graphics.Bitmap

data class lestudiantes (
    val id: String,
    val docente: String,
    val nombres: String,
    val apellidos: String,
    val carnet: String,
    val celular: String,
    val nacimiento: String,
    val pais: String,
    val ciudad: String,
    val email: String,
    val user: String,
    val pass: String,
    val edad: String,
    val puntos: String,
    val estado: String,
    val docentes: ldocentes
    )


data class ldocentes (
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

    val silaba_estatico: String,
    val silaba_pregunta: String,
)

data class Juego(
    val id: String,
    val tema: String,
    val tarea: String,
    val tiempo: String,
    val estado: String
)


data class Torneo(
    val id: String,
    val nombre: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val tiempo: String,
    val intentos: String,
    val estado: String
)


data class Gamess(
    val id: String,
    val tema: lTema,
    val tarea: String,
    val tiempo: String,
    val estado: String
)

data class Torneo_Game(
    val id: String,
    val torneo: Torneo,
    val tareas: String,
    val estado: String,
    val games: Gamess
)

data class Torneo_estudiante(
    val id: String,
    val estudiante: String,
    val torneo: String,
    val estado: String,
    val estu: lestudiantes
)


data class ESTA_datos(
    val id : String,
    val nombre : String,
    val apellido : String,
    val puntaje : String,
    val tiempo : String,
    val intento : String
)
data class Estadistica(
    val id: String,
    val nombre: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val tiempo: String,
    val intentos: String,
    val estado: String,
    val torneo: List<ESTA_datos>
)

data class Est_play(
    val id: Int,
    val estudiante: Int,
    val torneo: Int,
    val intentos: Int,
    val puntaje: Int,
    val tiempo: String,
    val erroes: Int,
    val correctas: Int,
    val total: Int,
    val estado: Int,
    val estu: lestudiantes
)