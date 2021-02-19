package com.abhijith.stories

import android.view.View
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
    var viewPositionX: Int,
    var viewPositionY: Int,
)

fun View.getLocationDetails(): ViewAddress {
    val point = IntArray(2)
    getLocationInWindow(point)
    return ViewAddress(width,height,point[0],point[1])
}

//fun View.setLocationDetails(): ViewAddress {
//    val point = IntArray(2)
//    getLocationInWindow(point)
//    return ViewAddress(width,height,point[0],point[1])
//}