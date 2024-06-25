package com.drubico.pokeapi.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


object ImageUtils {
    fun downloadAndSaveImage(context: Context, url: String, filename: String): String {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImage(context, resource, filename)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        return context.getFileStreamPath(filename).absolutePath
    }

    private fun saveImage(context: Context, bitmap: Bitmap, filename: String): String {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return context.filesDir.absolutePath + "/" + filename
    }

    fun displayImageFromPath(imageView: ImageView, path: String) {
        val bitmap = BitmapFactory.decodeFile(path)
        imageView.setImageBitmap(bitmap)
    }
}
