package com.example.fundacion.admin.game

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fundacion.R
import java.util.regex.Pattern


class PruebaJuego_silabas_CTI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_juego_silabas_cti)
    }



    val regexStep1 = mutableListOf<Pattern>()
    val cutPositionsStep1 = mutableListOf<Int>()

    val regexStep2 = mutableListOf<Pattern>()
    val cutPositionsStep2 = mutableListOf<Int>()

    fun initializeRegex() {
        // STEP 1
        regexStep1.add(Pattern.compile("^[aeiouáéíóúü]+([^aeiouáéíóúü]|$)"))
        cutPositionsStep1.add(0)

        regexStep1.add(Pattern.compile("^[^aeiouáéíóúü][aeiouáéíóúü]+([^aeiouáéíóúü]|$)"))
        cutPositionsStep1.add(0)

        regexStep1.add(Pattern.compile("^[^aeiouáéíóúü][^aeiouáéíóúü][aeiouáéíóúü]+([^aeiouáéíóúü]|$)"))
        cutPositionsStep1.add(1)

        regexStep1.add(Pattern.compile("^[^aeiouáéíóúü][^aeiouáéíóúü][^aeiouáéíóúü][aeiouáéíóúü]+([^aeiouáéíóúü]|$)"))
        cutPositionsStep1.add(2)

        regexStep1.add(Pattern.compile("^[^aeiouáéíóúü][^aeiouáéíóúü][^aeiouáéíóúü][^aeiouáéíóúü][aeiouáéíóúü]+([^aeiouáéíóúü]|$)"))
        cutPositionsStep1.add(3)

        regexStep1.add(Pattern.compile("^[aeiouáéíóúü]+$"))
        cutPositionsStep1.add(-1)

        regexStep1.add(Pattern.compile("^[^aeiouáéíóúü]+$"))
        cutPositionsStep1.add(-1)

        // STEP 2
        regexStep2.add(Pattern.compile("[aeoáéó][aeoáéó]"))
        cutPositionsStep2.add(1)

        regexStep2.add(Pattern.compile("[íú][aeo]"))
        cutPositionsStep2.add(1)

        regexStep2.add(Pattern.compile("[aeo][íú]"))
        cutPositionsStep2.add(1)

        regexStep2.add(Pattern.compile("[iuüíú][aeoáéó][aeoáéó]"))
        cutPositionsStep2.add(2)

        regexStep2.add(Pattern.compile("[aeoáéó][aeoáéó][iuüíú]"))
        cutPositionsStep2.add(1)

        regexStep2.add(Pattern.compile(".*"))
        cutPositionsStep2.add(-1)
    }

    fun divide(word: String): List<String> {
        initializeRegex()
        var word = word.toLowerCase().trim()
        var cutPosition: Int
        val syllables = mutableListOf<String>()
        val finalSyllables = mutableListOf<String>()
        var head = ""
        var securityBreak = 0
        var end = false

        while (!end) {
            securityBreak++
            if (securityBreak > 20) {
                throw Exception("Error processing word")
            }
            var matched = false
            for (i in regexStep1.indices) {
                val match = regexStep1[i].matcher(word)
                if (match.find()) {
                    val m = match.group()
                    cutPosition = cutPositionsStep1[i]
                    if (cutPosition < 0) {
                        syllables.add(head + m)
                        end = true
                    } else {
                        var cutChar = m[cutPosition]
                        if (cutChar == 'r' || cutChar == 'l' || cutChar == 'h') {
                            cutPosition--
                        }
                        if (cutPosition < 0) cutPosition = 0
                        syllables.add(head + m.substring(0, cutPosition))
                        head = m.substring(cutPosition)
                    }
                    word = word.substring(m.length)
                    matched = true
                    break
                }
            }
            if (!matched) {
                throw Exception("Error processing word")
            }
        }

        for (s in syllables) {
            if (s.isEmpty()) continue

            for (i in regexStep2.indices) {
                val match = regexStep2[i].matcher(s)
                if (match.find()) {
                    if (cutPositionsStep2[i] < 0) {
                        finalSyllables.add(s)
                    } else {
                        val cutPosition = match.start() + cutPositionsStep2[i]
                        finalSyllables.add(s.substring(0, cutPosition))
                        finalSyllables.add(s.substring(cutPosition))
                    }
                    break
                }
            }
        }
        return finalSyllables
    }

    fun stress(syllables: List<String>): Int {
        if (syllables.size == 1) {
            return 0
        }

        for (i in syllables.indices) {
            if (syllables[i].contains(Regex("[áéíóú]"))) {
                return i
            }
        }

        return if (syllables.last().contains(Regex(".*[nsaeiou]$"))) {
            syllables.size - 2
        } else {
            syllables.size - 1
        }
    }

    fun difficulty(syllables: List<String>): List<Int> {
        return syllables.map { difficultySyllable(it) }
    }

    fun difficultySyllable(text: String): Int {
        var text = text.replace("ll", "y")
            .replace("rr", "r")
            .replace("que", "qe")
            .replace("qui", "qi")
            .replace("qué", "qe")
            .replace("quí", "qi")
            .replace("gü", "g")

        var value = 0

        if (Regex("[bcdfghjklmnñpqrstvwxyz]l").containsMatchIn(text)) {
            value += 1
        }
        if (Regex("[bcdfghjklmnñpqrstvwxyz]n").containsMatchIn(text)) {
            value += 1
        }
        if (Regex("[bcdfghjklmnñpqrstvwxyz]h").containsMatchIn(text)) {
            value += 1
        }
        if (Regex("[bcdfghjklmnñpqrstvwxyz]s").containsMatchIn(text)) {
            value += 1
        }

        text = text.replace("h", "")

        val consonantsHard = Regex("[kñwxy]").findAll(text)
        value += consonantsHard.count() * 3

        val consonants = Regex("[bcdfghjlmnpqrstvz]").findAll(text)
        value += consonants.count() * 2

        val vowels = Regex("[aeiouáéíóú]").findAll(text)
        value += vowels.count()

        return value
    }

    fun main() {
        val word = "casa"
        try {
            val syllables = divide(word)
            println("Syllables: $syllables")
            println("Stress position: ${stress(syllables)}")
            println("Difficulty: ${difficulty(syllables)}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }



    fun veri(view: View){
        main()

    }

}