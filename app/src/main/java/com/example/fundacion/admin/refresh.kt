package com.example.fundacion.admin

import androidx.fragment.app.Fragment

interface Refresh {
    fun refresha()
}
interface RefreshGame {
    fun refresh()
    fun verlist(fragment: Fragment)
    fun retro()
}
interface Fragmentt{
    fun datos(data: String)
}

interface Game {
    fun test(a: Int, b: Int)
}

interface Game_Vocal{
    fun inicial(img: String, name: String)
}

interface RefreshGamePreguntasVocal{
    fun refresh()
    fun modalEditVocales(position: Int)
}

interface RefreshGamePreguntasSilabasSimples{
    fun refresh()
    fun modalEditSilabas(position: Int)

}
interface RefreshGamePreguntas{
    fun refresh()
    fun modalEdit(position: Int)

}
interface RefreshGameSopaLetras{
    fun refresh()
    fun modalEdit(position: Int)

}
interface RefreshGameSopaLetrasPrueba{
    fun letraSelect(callback: (String, String) -> Unit)

    fun botonSelect(text: String)
    fun letraincorrecto(text: String)
    fun noselect()
    fun terminado()

}
