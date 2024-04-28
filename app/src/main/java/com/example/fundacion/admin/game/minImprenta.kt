package com.example.fundacion.admin.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.GnssAntennaInfo.Listener
import android.provider.CalendarContract.Colors
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fundacion.R
import com.example.fundacion.admin.Game
import com.example.fundacion.admin.RefreshGame
import es.dmoral.toasty.Toasty

class minImprenta(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var clickGame: Game? = null
    private val cellSize = 100 // Tamaño de cada celda
    private val gridSize = 6 // Tamaño de la cuadrícula (5x5 en este ejemplo)
    private val vowels = listOf('a', 'e', 'i', 'o', 'u') // Lista de vocales en mayúsculas
    private val grid = Array(gridSize) { CharArray(gridSize) }
    private val selectedCells = Array(gridSize) { BooleanArray(gridSize) }

    private var totalLetter = 0 // Contador de letras 'A' en total
    private var selectedLetter = 0 // Contador de letras 'A' seleccionadas
    private var remainingLetter = 0 // Contador de letras 'A' restantes por seleccionar
    private var currentLetter = 'a'

    var remainingLetters = vowels.toMutableList()

    fun setGame(listener: Game){
        clickGame =listener
    }

    init {
        /*
        // Inicializa la matriz de letras con vocales aleatorias en mayúsculas
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                grid[i][j] = vowels.random()
                if (grid[i][j] == currentLetter) {
                    totalLetter++ // Incrementa el contador de letras 'A' en total
                }
            }
        }
        remainingLetter = totalLetter // Al inicio, todas las letras 'A' están restantes por seleccionar
        */
/*
        clickGame?.test(selectedLetter, remainingLetter)

        val next = remainingLetters.removeAt(0)
        setCurrentLetter(next)
        totalLetter=0
        */

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // Calcula el tamaño necesario para mostrar la matriz de celdas
        val desiredWidth = paddingLeft + paddingRight + gridSize * cellSize
        val desiredHeight = paddingTop + paddingBottom + gridSize * cellSize

        // Establece las dimensiones de la vista según las especificaciones proporcionadas
        val finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> desiredWidth.coerceAtMost(widthSize)
            else -> desiredWidth
        }

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val paint = Paint().apply {
                textSize = 40f // Tamaño de texto
                textAlign = Paint.Align.CENTER
            }

            // Dibuja la cuadrícula de letras
            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val cellText = grid[i][j].toString()
                    val xPos = (j * cellSize + cellSize / 2).toFloat()
                    //val yPos = (i * cellSize + cellSize / 2).toFloat()
                    val yPos = (i * cellSize + cellSize / 2 + (paint.descent() - paint.ascent()) / 2 - paint.descent()).toFloat()




                    // Cambia el color si la celda está seleccionada
                    if (selectedCells[i][j]) {
                        paint.color = Color.RED // Cambia el color a rojo (puedes usar otro color)

                        val background = ContextCompat.getColor(context, R.color.adminDM)

                        val startX = (j * cellSize).toFloat() // X inicial del rectángulo
                        val startY = (i * cellSize).toFloat() // Y inicial del rectángulo
                        val endX = ((j + 1) * cellSize).toFloat() // X final del rectángulo
                        val endY = ((i + 1) * cellSize).toFloat()
                        drawRect(startX, startY, endX, endY, Paint().apply {
                            color = background // Color de fondo de las celdas seleccionadas (puedes cambiarlo según tu preferencia) })
                        })

                        drawLine(startX, startY, endX, endY, paint)
                        drawLine(endX, startY, startX, endY, paint)
                    } else {
                        paint.color = Color.BLACK // Color normal
                    }

                    drawText(cellText, xPos, yPos, paint)
                }
            }
        }



    }

    fun shuffleGrid() {
        Toast.makeText(context, "$selectedLetter", Toast.LENGTH_SHORT).show()
        // Desordena la matriz de letras con vocales aleatorias en mayúsculas
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                grid[i][j] = vowels.random()
                selectedCells[i][j] = false
                /*if (grid[i][j] == currentLetter){
                    totalLetter++
                }*/
            }
        }
        invalidate() // Vuelve a dibujar la vista para reflejar el cambio
        totalLetter = grid.sumBy { row -> row.count { it == currentLetter} }

        remainingLetter = totalLetter
        Toasty.error(context, "$remainingLetter", Toasty.LENGTH_SHORT).show()
        clickGame?.test(selectedLetter, remainingLetter)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {

                MotionEvent.ACTION_DOWN -> {
                    Toasty.success(context, "$remainingLetter", Toasty.LENGTH_SHORT).show()

                    // Calcula la posición de la celda seleccionada
                    val col = (it.x / cellSize).toInt().coerceIn(0, gridSize - 1)
                    val row = (it.y / cellSize).toInt().coerceIn(0, gridSize - 1)

                    // Marca la celda como seleccionada si contiene la letra 'A'
                    if (grid[row][col] == currentLetter  && !selectedCells[row][col]) {
                        selectedCells[row][col] = true
                        selectedLetter++ // Incrementa el contador de letras 'A' seleccionadas
                        remainingLetter-- // Decrementa el contador de letras 'A' restantes por seleccionar
                        clickGame?.test(selectedLetter, remainingLetter)
                        invalidate() // Vuelve a dibujar la vista


                    }
                }
            }
        }
        return true
    }

    fun setCurrentLetter(letter: Char) {
        currentLetter = letter
        selectedLetter = 0
        remainingLetter = 0
        totalLetter = 0
    }


    fun checkAllLettersSelected(): Boolean {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (grid[i][j] == 'A' && !selectedCells[i][j]) {
                    return false // No todas las letras 'A' están seleccionadas
                }
            }
        }
        return true // Todas las letras 'A' están seleccionadas
    }


}


