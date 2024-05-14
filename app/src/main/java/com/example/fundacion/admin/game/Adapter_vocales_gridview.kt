package com.example.fundacion.admin.game

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.fundacion.R
import com.example.fundacion.admin.Game_Vocal
import com.example.fundacion.config
import java.net.URL

class Adapter_vocales_gridview(
    context: Context,
    private val imageUrls: List<String>,
    private val totalItems: Int,
    private val refre : Game_Vocal
    ) : ArrayAdapter<String>(context, 0, imageUrls) {

    private var shuffledUrls: List<String> = emptyList()
    private var url1Count = 0
    private var vocal :String? = null

    init {
        refresh()
    }

    private fun refresh(){
        val shuffledList = mutableListOf<String>()
        val repeatCount = totalItems / imageUrls.size
        val remainingCount = totalItems % imageUrls.size

        for (i in 1..repeatCount) {
            shuffledList.addAll(imageUrls.shuffled())
        }
        shuffledList.addAll(imageUrls.shuffled().take(remainingCount))

        shuffledUrls = shuffledList.shuffled()
        url1Count = shuffledUrls.count { it == vocal }
        refre.inicial(url1Count.toString(), vocal.toString())
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView = convertView as? ImageView ?: createImageView(parent.context)

        val imageUrl = config.url+"admin/preg-vocal-imagen/"+getItem(position)
        Glide.with(context)
            .load(imageUrl)
            .error(R.drawable.baseline_home_24)
            .override(100)
            .into(imageView)

        val sizeInDp = 50
        val density = context.resources.displayMetrics.density
        val sizeInPixels = (sizeInDp * density).toInt()


        val layoutParams = ViewGroup.MarginLayoutParams(sizeInPixels, sizeInPixels)
        val marginTopInDp = 5
        val marginTopInPixels = (marginTopInDp * density).toInt()
        layoutParams.setMargins(0, 0, 0, 10)

       // imageView.layoutParams = ViewGroup.LayoutParams(sizeInPixels, sizeInPixels)
        //imageView.setBackgroundColor(Color.TRANSPARENT)
        imageView.layoutParams = layoutParams
        imageView.background = ContextCompat.getDrawable(context, R.drawable.fondo_vocal_noselect)


        return imageView
    }

    private fun createImageView(context: Context): ImageView {
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageView
    }

    override fun getItem(position: Int): String? {
        return shuffledUrls.getOrNull(position)
    }

    override fun getCount(): Int {
        return shuffledUrls.size
    }

    fun reset(gvocal : String){
        vocal =gvocal
        refresh()
    }
}