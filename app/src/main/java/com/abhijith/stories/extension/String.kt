package com.abhijith.stories.extension

import android.net.Uri
import java.io.File

fun String.toFIle():File{
    return File(this)
}

fun String.toFileUri():Uri{
    return Uri.fromFile(toFIle())
}