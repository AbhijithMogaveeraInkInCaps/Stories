package com.abhijith.stories.extension

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toJpegFile(name: String = System.currentTimeMillis().toString()): File {
    val sdCard: File = Environment.getExternalStorageDirectory()
    val dir = File(sdCard.absolutePath + "/story")
    dir.mkdirs()
    val fileName = "$name.jpeg"
    val file = File(dir, fileName)
    val fOut = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG, 100, fOut)
    fOut.close()
    return file
}