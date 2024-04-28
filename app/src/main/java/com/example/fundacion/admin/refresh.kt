package com.example.fundacion.admin

import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.fundacion.admin.adapter.AdapterGames

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

