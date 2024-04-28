package com.example.fundacion.admin.game


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.fundacion.R
import es.dmoral.toasty.Toasty

class juego_vocal_A(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val cellSize = 100 // Tamaño de cada celda
    private val numRows = 10 // Número de filas
    private val numCols = 5 // Número de columnas
    private val images = listOf(
        R.drawable.vocal_cma,
        R.drawable.vocal_cmi,
        R.drawable.vocal_cmo,
        R.drawable.vocal_cmu,
        R.drawable.vocal_cme
    )

    private val grid = Array(numRows) { IntArray(numCols) }
    private val selectedCells = Array(numRows) { BooleanArray(numCols) }

    init {
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                grid[i][j] = (0 until images.size).random()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            for (i in 0 until numRows) {
                for (j in 0 until numCols) {
                    val left = j * cellSize
                    val top = i * cellSize
                    val right = (j + 1) * cellSize
                    val bottom = (i + 1) * cellSize

                    if (selectedCells[i][j]) {
                        drawColor(Color.RED) // Cambia el fondo de la celda a rojo cuando está seleccionada
                    }

                    val imageIndex = grid[i][j]
                    loadImage(images[imageIndex], Rect(left, top, right, bottom), this)
                }
            }
        }
    }

    private fun loadImage(imageResId: Int, destinationRect: Rect, canvas: Canvas) {
        Glide.with(context)
            .asBitmap()
            .load(imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(cellSize, cellSize) // Ajusta el tamaño de la imagen según la celda
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    canvas.drawBitmap(resource, null, destinationRect, Paint())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // No es necesario hacer nada en este caso
                }
            })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    val col = (it.x / cellSize).toInt().coerceIn(0, numCols - 1)
                    val row = (it.y / cellSize).toInt().coerceIn(0, numRows - 1)
                    selectedCells[row][col] = !selectedCells[row][col]
                    invalidate()
                }
            }
        }
        return true
    }
}
