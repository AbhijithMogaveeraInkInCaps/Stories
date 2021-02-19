package com.abhijith.stories.storyscreeen.callback

import android.view.View
import com.abhijith.stories.storyscreeen.StoryMain

interface StoriesCallback{
    fun done()
    fun onNextCalled(view: View, storyMain : StoryMain, index: Int)
}