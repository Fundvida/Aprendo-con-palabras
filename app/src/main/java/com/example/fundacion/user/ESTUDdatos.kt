package com.example.fundacion.user

import com.example.fundacion.user.torneo_estud.Game_torneo_abecedario_estud
import com.example.fundacion.user.torneo_estud.Game_torneo_construir_oraciones_estud
import com.example.fundacion.user.torneo_estud.Game_torneo_deletreo_estud
import com.example.fundacion.user.torneo_estud.Game_torneo_silabassimples_estud
import com.example.fundacion.user.torneo_estud.Game_torneo_sopa_letras_estud
import com.example.fundacion.user.torneo_estud.Game_torneo_vocal_estud

class ESTUDdatos {

    companion object {

        var datos : MutableList<EGAME_torneo_list> = mutableListOf()

        var indexActivity = 0

        var puntaje_total = 0
        var errores = 0
        var correctas = 0



        var GameTime = 0L
        var GameTimeGlobal = 0L
        var GameIntentos = 0
        var GameFechaInicio = ""
        var GameFechaFin = ""
        var GameID = 0
        var GameUserIntento = 0
        var Games = 0

        var temaActivityMap = mapOf(
            "LAS VOCALES" to Game_torneo_vocal_estud::class.java,
            "EL ABECEDARIO" to Game_torneo_abecedario_estud::class.java,
            "LAS SILABAS SIMPLES" to Game_torneo_silabassimples_estud::class.java,
            "EL DELETREO DE PALABRAS HASTA TRES SILABAS" to Game_torneo_deletreo_estud::class.java,
            "SOPA DE LETRAS DE ALIMENTOS, MUEBLES DE LA CASA, CIUDADES DE BOLIVIA" to Game_torneo_sopa_letras_estud::class.java,
            "CONSTRUIR ORACIONES CORTAS ORDENANDO PALABRAS" to Game_torneo_construir_oraciones_estud::class.java
        )
    }
}