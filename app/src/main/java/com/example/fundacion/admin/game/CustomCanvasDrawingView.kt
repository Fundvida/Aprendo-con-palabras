package com.example.fundacion.admin.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.fundacion.R
import com.example.fundacion.admin.fragment.CanvasDrawingView



class CustomCanvasDrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val numRows = 5
    private val numCols = 5
    private var cellWidth: Float = 0f
    private var cellHeight: Float = 0f
    private val images: MutableList<Bitmap?> = mutableListOf()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun setImages(urls: List<String>) {
        urls.forEachIndexed { index, url ->
            Glide.with(context)
                .asBitmap()
                .load(url)
                .override(50)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        images.add(resource)
                        // Redraw the view when all images are loaded
                        if (images.size == urls.size) {
                            calculateCellSize()
                            invalidate()
                        }
                    }

                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                        // Not needed for this example
                    }
                })
        }
    }

    private fun calculateCellSize() {
        cellWidth = width / numCols.toFloat()
        cellHeight = height / numRows.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var index = 0

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                if (index < images.size && images[index] != null) {
                    val bitmap = images[index]!!
                    val x = col * cellWidth
                    val y = row * cellHeight
                    canvas.drawBitmap(bitmap, x, y, paint)
                }
                index++
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateCellSize()
        val size = measuredWidth.coerceAtMost(measuredHeight)
        setMeasuredDimension(size, size)
    }
}