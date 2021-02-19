package com.abhijith.stories.storyview.callback

import android.view.View
import com.abhijith.stories.storyview.StoryMain

interface StoriesCallback{
    fun done()
    fun onNextCalled(view: View, storyMain : StoryMain, index: Int)
}