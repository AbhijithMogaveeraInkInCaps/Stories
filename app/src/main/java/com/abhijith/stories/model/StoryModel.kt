package com.abhijith.stories.model

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import com.abhijith.stories.ViewAddress

object StoryModel {

    val liveData: MutableLiveData<Unit> = MutableLiveData()
    private val storyList = mutableListOf<StoryBase>()

    open class StoryBase(
        val id:Long,
        val dataType: MediaType
    )

    data class StoryDataImage(
        val ID: Long,
        val path: String,
        val link:String="",
        val stickerList:List<StoryDataSticker> = listOf()
    ) : StoryBase(ID, MediaType.IMAGE)

    data class StoryDataSticker(
        @DrawableRes
        val id:Int,
        val viewAddress: ViewAddress
    )

    data class TextStoryData(
        val ID: Long,
        val textSize: Float,
        val text: String,
        val bgColor:Int
    ) : StoryBase(ID, MediaType.TEXT)

    enum class MediaType {
        IMAGE, VIDEO, TEXT
    }

    fun insertImageStory(storyDataImage: StoryDataImage) {
        storyList.add(storyDataImage)
        liveData.postValue(Unit)
    }

    fun insertTextStory(textStoryData: TextStoryData) {
        storyList.add(textStoryData)
        liveData.postValue(Unit)
    }

    fun deleteStory(storyDataImage: StoryDataImage) {
        storyList.remove(storyDataImage)
    }

    fun getAllStory() = storyList
}