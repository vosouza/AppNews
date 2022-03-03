package com.evosouza.news.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.io.ByteArrayOutputStream


fun TextInputLayout.setErrorResId(context: Context, resId: Int?) {
    error = if (resId != null && resId != 0) context.getString(resId) else null
}

fun Bitmap.bitmapToString(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT) ?: ""
}

fun String.stringBase64ToBitmap(): Bitmap?{
    return try {
        val encodeByte: ByteArray = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String.toStars(): String{
    var password = ""
    for (i in 0..this.count()){
        password += "*"
    }
    return  password
}

fun Bitmap.resizeImage(widthToResize: Float = 250f, heightToResize: Float = 250f ): Bitmap {
    val width: Int = this.width
    val height: Int = this.height
    val scaleWidth: Float = widthToResize / width
    val scaleHeight: Float = heightToResize / height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    val resizedBitmap: Bitmap = Bitmap.createBitmap(
        this, 0, 0, width, height, matrix, false)
    this.recycle()
    return resizedBitmap
}

