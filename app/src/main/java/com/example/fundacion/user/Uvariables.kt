package com.example.fundacion.user

import org.json.JSONArray

data class Tarea(
    val id: Int,
    val tema: Int,
    val tarea: String,
    val tiempo: Int,
    val estado: Int
)

data class ETorneo(
    val id: String,
    val nombre: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val tiempo: String,
    val intentos: String,
    val estado: String
)

data class ETorneoEstudiante(
    val id: String,
    val estudiante: String,
    val torneo: ETorneo,
    val estado: String
)

data class EGamess(
    val id: String,
    val tema: String,
    val tarea: String,
    val tiempo: String,
    val estado: String,
    val total_puntaje: String,
    val juegos : String,
    val tema_actual : String,
    val puntaje_max : String
)

data class ETorneo_Game(
    val id: String,
    val torneo: String,
    val tareas: String,
    val estado: String,
    val games: EGamess
)

data class EGAME_torneo_list(
    val idTEMA : String,
    val nameTEMA: String,
    val idGAME: String,
    val nameGAME : String

)




data class ActivitySequence(val activities: List<Class<*>>)

data class Game_LIST(
    val id: Int,
    val tema: Int,
    val tarea: String,
    val tiempo: Int,
    val estado: Int,
    val totalPuntaje: String,
    val juegos: Int,
    val temaActual: String
)


