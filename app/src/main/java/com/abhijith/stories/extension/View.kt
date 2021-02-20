package com.abhijith.stories.extension

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.abhijith.stories.ViewAddress

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.setXY(X:Float,Y:Float){
    x = X
    y = Y
    invalidate()
}

fun View.getLocationDetails(): ViewAddress {
    return ViewAddress(width, height, x, y, rotation)
}

fun View.setAddress(viewAddress: ViewAddress){
    layoutParams = ConstraintLayout.LayoutParams(viewAddress.viewWidth, viewAddress.viewHeight)
    translationX = viewAddress.x
    translationY = viewAddress.y
    rotation = viewAddress.rotationFactor
    invalidate()
}