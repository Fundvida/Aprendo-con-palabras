package com.example.fundacion.user

import com.example.fundacion.admin.game.PruebaJuego_abecedario
import com.example.fundacion.admin.game.PruebaJuego_construir_oraciones_simples
import com.example.fundacion.admin.game.PruebaJuego_deletreo_simple
import com.example.fundacion.admin.game.PruebaJuego_sopaletras_basico
import com.example.fundacion.user.torneo_estud.Game_torneo_silabassimples_estud
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
            "EL ABECEDARIO" to PruebaJuego_abecedario::class.java,
            "LAS SILABAS SIMPLES" to Game_torneo_silabassimples_estud::class.java,
            "EL DELETREO DE PALABRAS HASTA TRES SILABAS" to PruebaJuego_deletreo_simple::class.java,
            "SOPA DE LETRAS DE ALIMENTOS, MUEBLES DE LA CASA, CIUDADES DE BOLIVIA" to PruebaJuego_sopaletras_basico::class.java,
            "CONSTRUIR ORACIONES CORTAS ORDENANDO PALABRAS" to PruebaJuego_construir_oraciones_simples::class.java
        )
    }
}