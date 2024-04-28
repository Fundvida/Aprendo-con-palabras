package com.example.fundacion.admin.fragment

/*
override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    canvas?.apply {

        val desiredWidth = 200 // Ancho deseado
        val desiredHeight = 100 // Alto deseado
        var topOffset = 0
        for (bitmap in imagesListAll) {
            val scaleRatio = desiredWidth.toFloat() / bitmap.width.toFloat() // Calcular la proporción de escala
            val scaledHeight = (bitmap.height * scaleRatio).toInt() // Calcular la altura escalada
            val left = (width - desiredWidth) / 2
            val top = topOffset
            val rect = Rect(left, top, left + desiredWidth, top + scaledHeight)
            drawBitmap(bitmap, null, rect, null)
            topOffset += scaledHeight + 10 // Espacio entre las imágenes
        }

        println( "este es ==="+imagesListAll)
        /*
        imageBitmap?.let {
            val desiredWidth = 200 // Ancho deseado
            val desiredHeight = 100 // Alto deseado
            val left = (width - desiredWidth) / 2
            val top = height - desiredHeight
            val rect = Rect(left, top, left + desiredWidth, top + desiredHeight)
            drawBitmap(it, null, rect, null)

            val lineY = top + desiredHeight/2 // Altura en la que se dibujarán las líneas
            drawLine(rect.right.toFloat(), lineY.toFloat(), rect.right.toFloat() + 100f, lineY.toFloat(), linePaint) // Línea 1
        }
*/

    }
}
*/



/*

//esto hace que se muevan las imagenes pero se agrupan en un solo lugar
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            for (imageModel in imagesListAll) {
                drawBitmap(imageModel.bitmap, imageModel.left, imageModel.top, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectedImageIndex = findSelectedImage(x, y)
                    selectedImageIndex?.let {
                        offsetX = x - imagesListAll[it].left
                        offsetY = y - imagesListAll[it].top
                        isDragging = true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    selectedImageIndex?.let {
                        if (isDragging) {
                            imagesListAll[it].left = x - offsetX
                            imagesListAll[it].top = y - offsetY
                            invalidate()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    selectedImageIndex = null
                    isDragging = false
                }else ->{

                }
            }
        }
        return true
    }

*/
/*
    inner class LoadImageTask : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            return try {
                val url = URL(params[0])
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            imageBitmap = result
            invalidate()
        }
    }

    inner class LoadImageTaskAll : AsyncTask<List<String>, Void, List<Bitmap>>() {
        override fun doInBackground(vararg params: List<String>): List<Bitmap>? {
            val images = mutableListOf<Bitmap>()
            for (url in params[0]) {
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream = connection.inputStream
                    images.add(Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return images
        }

        override fun onPostExecute(result: List<Bitmap>?) {
            super.onPostExecute(result)
            result?.let {
                imagesListAll.addAll(it)
                invalidate()
            }
        }
    }
*/



/*
//backup de imgenes
package com.example.fundacion.admin.fragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.content.Context
import android.graphics.*
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.fundacion.R
import com.example.fundacion.admin.ImageModel
import com.example.fundacion.config
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var imageBitmap: Bitmap? = null
    private val linePaint = Paint()
    //private var imagesListAll = mutableListOf<Bitmap>()
    private val imagesListAll = mutableListOf<ImageModel>()

    private var selectedImageIndex: Int? = null
    private var offsetX = 0f
    private var offsetY = 0f
    private var isDragging = false

    var left : Float = 50f
    var top : Float = 50f
    var rights : Float = 50f

    private fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


    private val imageMargin = 10

    private var selectedImage: ImageModel? = null // Imagen seleccionada
    private var squareRect: RectF? = null // Rectángulo para el cuadrado en la parte inferior
    private val squarePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private var originalPosition: Pair<Float, Float>? = null // Posición original de la imagen seleccionada


    init {
        //LoadImageTask().execute("${config.url}admin/preg-images/vocal_CMU.png")
        linePaint.color = context.getColor(R.color.rojo) // Color de las líneas
        linePaint.strokeWidth = 5f // Grosor de las líneas
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {

            squareRect?.let {
                drawRect(it, squarePaint)
            }

            selectedImage?.let {
                drawBitmap(it.bitmap, squareRect!!.left, squareRect!!.top, null)
            }

            for (imageModel in imagesListAll) {
                drawBitmap(imageModel.bitmap, imageModel.left, imageModel.top, null)
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectedImageIndex = findSelectedImage(x, y)
                    selectedImageIndex?.let {
                        offsetX = x - imagesListAll[it].left
                        offsetY = y - imagesListAll[it].top
                        isDragging = true

                        originalPosition = Pair(imagesListAll[it].left, imagesListAll[it].top)
                        // Configurar el cuadrado en la parte inferior
                        setBottomSquare(selectedImageIndex?.let { imagesListAll[it] })
                    }
                    // Configurar el cuadrado en la parte inferior
                    setBottomSquare(selectedImageIndex?.let { imagesListAll[it] })
                }
                MotionEvent.ACTION_MOVE -> {
                    selectedImageIndex?.let {
                        if (isDragging) {
                            imagesListAll[it].left = x - offsetX
                            imagesListAll[it].top = y - offsetY
                            invalidate()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    selectedImageIndex = null
                    isDragging = false
                }else ->{}
            }
        }
        return true
    }



    private fun findSelectedImage(x: Float, y: Float): Int? {
        for ((index, imageModel) in imagesListAll.withIndex()) {
            if (x >= imageModel.left && x <= imageModel.right &&
                y >= imageModel.top && y <= imageModel.bottom) {
                return index
            }
        }
        return null
    }




    inner class LoadImageTask : AsyncTask<List<String>, Void, List<Bitmap>>() {
        override fun doInBackground(vararg params: List<String>): List<Bitmap>? {
            val images = mutableListOf<Bitmap>()
            for (url in params[0]) {
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream = connection.inputStream
                    images.add(Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return images
        }

        override fun onPostExecute(result: List<Bitmap>?) {
            super.onPostExecute(result)
            result?.let {
                for (bitmap in it) {
                    val resizedBitmap = resizeBitmap(bitmap, 200, 200) // Redimensionar a 200x200
                    imagesListAll.add(ImageModel(resizedBitmap, 0f, 0f)) // Agregar a la lista
                }
                arrangeImages()
            }
        }
    }


    private fun arrangeImages() {
        val screenWidth = width // Ancho del Canvas
        val margin = imageMargin * 2 // Margen total (izquierda y derecha)
        val imageWidth = imagesListAll.firstOrNull()?.bitmap?.width ?: 0 // Ancho de la primera imagen (si hay alguna)
        val numImagesPerRow = (screenWidth - margin) / (imageWidth + imageMargin) // Número de imágenes por fila

        var currentX = imageMargin.toFloat()
        var currentY = imageMargin.toFloat()
        for ((index, imageModel) in imagesListAll.withIndex()) {
            val newX = currentX + imageWidth + imageMargin
            if (newX > screenWidth) {
                // Cambiar de fila
                currentX = imageMargin.toFloat()
                currentY += imageModel.bitmap.height + imageMargin
            }
            imageModel.left = currentX
            imageModel.top = currentY
            currentX = newX
        }
        invalidate()
    }

    private fun setBottomSquare(imageModel: ImageModel?) {
        selectedImage = imageModel
        selectedImage?.let {
            val squareSize = 150 // Tamaño del cuadrado en la parte inferior
            val screenWidth = width // Ancho del Canvas
            val squareLeft = (screenWidth - squareSize) / 2f // Posición izquierda del cuadrado
            val squareTop = height - squareSize.toFloat() // Posición superior del cuadrado
            squareRect = RectF(squareLeft, squareTop, squareLeft + squareSize, squareTop + squareSize)
            invalidate()
        }
    }

}
*/



/*

//el backup dos de don se sujeta la imagen dentro de la linea
package com.example.fundacion.admin.fragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.content.Context
import android.graphics.*
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.fundacion.R
import com.example.fundacion.admin.ImageModel
import com.example.fundacion.config
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL





class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var imageBitmap: Bitmap? = null
    private val linePaint = Paint()
    //private var imagesListAll = mutableListOf<Bitmap>()
    private val imagesListAll = mutableListOf<ImageModel>()

    private var selectedImageIndex: Int? = null
    private var offsetX = 0f
    private var offsetY = 0f
    private var isDragging = false

    var left : Float = 50f
    var top : Float = 50f
    var rights : Float = 50f

    private fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


    private val imageMargin = 10

    private var selectedImage: ImageModel? = null // Imagen seleccionada
    private var squareRect: RectF? = null // Rectángulo para el cuadrado en la parte inferior
    private val squarePaint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
    }

    private var originalPosition: Pair<Float, Float>? = null // Posición original de la imagen seleccionada


    init {
        //LoadImageTask().execute("${config.url}admin/preg-images/vocal_CMU.png")
        linePaint.color = context.getColor(R.color.rojo) // Color de las líneas
        linePaint.strokeWidth = 5f // Grosor de las líneas
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {

            val linePaint = Paint().apply {
                color = Color.WHITE // Color de la línea
                strokeWidth = 50f // Grosor de la línea en píxeles
            }

            val desiredWidth = 200 // Ancho deseado
            val desiredHeight = 0 // Alto deseado

            val left = (width - desiredWidth) / 2
            val topp = height - desiredHeight
            val right = left + desiredWidth
            val bottom = topp + desiredHeight

            canvas?.drawLine(left.toFloat(), topp.toFloat(), right.toFloat(), bottom.toFloat(), linePaint)


            for (imageModel in imagesListAll) {
                drawBitmap(imageModel.bitmap, imageModel.left, imageModel.top, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectedImageIndex = findSelectedImage(x, y)
                    selectedImageIndex?.let {
                        offsetX = x - imagesListAll[it].left
                        offsetY = y - imagesListAll[it].top
                        isDragging = true
                        originalPosition = Pair(imagesListAll[it].left, imagesListAll[it].top)
                        // Configurar el cuadrado en la parte inferior
                        setBottomSquare(selectedImageIndex?.let { imagesListAll[it] })
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    selectedImageIndex?.let {
                        if (isDragging) {
                            if (selectedImage == imagesListAll[it] && !isInsideSquare(x, y)) {
                                imagesListAll[it].left = x - offsetX
                                imagesListAll[it].top = y - offsetY
                                invalidate()
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    selectedImageIndex?.let {
                        if (isDragging) {
                            if (!isInsideSquare(x, y)) {
                                // Si la imagen se soltó fuera del cuadrado, restablecer la posición original
                                imagesListAll[it].left = originalPosition?.first ?: imagesListAll[it].left
                                imagesListAll[it].top = originalPosition?.second ?: imagesListAll[it].top
                                invalidate()
                            }
                        }
                    }
                    selectedImageIndex = null
                    isDragging = false
                }else ->{}
            }
        }
        return true
    }

    private fun isInsideSquare(x: Float, y: Float): Boolean {
        return squareRect?.contains(x, y) ?: false
    }

    private fun findSelectedImage(x: Float, y: Float): Int? {
        for ((index, imageModel) in imagesListAll.withIndex()) {
            if (x >= imageModel.left && x <= imageModel.right &&
                y >= imageModel.top && y <= imageModel.bottom) {
                return index
            }
        }
        return null
    }

    inner class LoadImageTask : AsyncTask<List<String>, Void, List<Bitmap>>() {
        override fun doInBackground(vararg params: List<String>): List<Bitmap>? {
            val images = mutableListOf<Bitmap>()
            for (url in params[0]) {
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream = connection.inputStream
                    images.add(Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return images
        }

        override fun onPostExecute(result: List<Bitmap>?) {
            super.onPostExecute(result)
            result?.let {
                for (bitmap in it) {
                    val resizedBitmap = resizeBitmap(bitmap, 200, 200) // Redimensionar a 200x200
                    imagesListAll.add(ImageModel(resizedBitmap, 0f, 0f)) // Agregar a la lista
                }
                arrangeImages()
            }
        }
    }

    private fun arrangeImages() {
        val screenWidth = width // Ancho del Canvas
        val margin = imageMargin * 2 // Margen total (izquierda y derecha)
        val imageWidth = imagesListAll.firstOrNull()?.bitmap?.width ?: 0 // Ancho de la primera imagen (si hay alguna)
        val numImagesPerRow = (screenWidth - margin) / (imageWidth + imageMargin) // Número de imágenes por fila

        var currentX = imageMargin.toFloat()
        var currentY = imageMargin.toFloat()
        for ((index, imageModel) in imagesListAll.withIndex()) {
            val newX = currentX + imageWidth + imageMargin
            if (newX > screenWidth) {
                // Cambiar de fila
                currentX = imageMargin.toFloat()
                currentY += imageModel.bitmap.height + imageMargin
            }
            imageModel.left = currentX
            imageModel.top = currentY
            currentX = newX
        }
        invalidate()
    }

    private fun setBottomSquare(imageModel: ImageModel?) {
        selectedImage = imageModel
        selectedImage?.let {
            val squareSize = 100 // Tamaño del cuadrado en la parte inferior
            val screenWidth = width // Ancho del Canvas
            val squareLeft = (screenWidth - squareSize) / 2f // Posición izquierda del cuadrado
            val squareTop = height - squareSize.toFloat() // Posición superior del cuadrado
            squareRect = RectF(squareLeft, squareTop+50, squareLeft + squareSize, squareTop + squareSize)
            println("este es el square"+squareRect)
            invalidate()
        }
    }

}


 */