package com.abhijith.stories.extension

import android.content.Intent
import com.abhijith.stories.helper.SELECT_PICTURE

object IntentCollection{
    fun getImagePickingIntent():Intent {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        return Intent.createChooser(i, "Select Picture")
    }
}
