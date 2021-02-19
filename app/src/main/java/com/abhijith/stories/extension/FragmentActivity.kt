package com.abhijith.stories.extension

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.abhijith.stories.helper.SELECT_PICTURE

fun FragmentActivity.startImagePickingActivity():Int{
    startActivityForResult(Intent.createChooser(
        IntentCollection.getImagePickingIntent(),
        "Select Picture"), SELECT_PICTURE)
    return SELECT_PICTURE
}