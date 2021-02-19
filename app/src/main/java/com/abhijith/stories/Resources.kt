package com.abhijith.stories

import android.view.View
import android.view.ViewGroup

val colorList = listOf(
    R.color.story_blue,
    R.color.story_dark_green,
    R.color.story_dark_pink,
    R.color.story_light_blue,
    R.color.story_light_green,
    R.color.story_purple,
    R.color.story_yellow,
)
data class ViewAddress(
    var viewWidth: Int,
    var viewHeight: Int,
    var x: Float,
    var y: Float,
    var rotationFactor: Float
)




//fun View.setLocationDetails(): ViewAddress {
//    val point = IntArray(2)
//    getLocationInWindow(point)
//    return ViewAddress(width,height,point[0],point[1])
//}