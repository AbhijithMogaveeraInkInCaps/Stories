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
    translationX = viewAddress.x+300
    translationY = viewAddress.y+300
    layoutParams = ConstraintLayout.LayoutParams(viewAddress.viewWidth, viewAddress.viewHeight)
    rotation = viewAddress.rotationFactor
}