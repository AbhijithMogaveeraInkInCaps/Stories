package com.abhijith.stories.ui.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.abhijith.stories.R
import com.abhijith.stories.StoryModel
import com.abhijith.stories.databinding.EmptyLayoutBinding
import com.abhijith.stories.storyview.StoryMain
import com.abhijith.stories.storyview.callback.StoriesCallback
import com.abhijith.stories.storyview.data.StoriesView
import java.io.File

class TestActivity : AppCompatActivity(), StoriesCallback {

    lateinit var binding: EmptyLayoutBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EmptyLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val storyList: MutableList<StoriesView> = mutableListOf()
        StoryModel.getAllStory().forEach { storyBase ->
            if (storyBase is StoryModel.StoryDataImage) {
                storyList.add(StoriesView(ImageView(this).apply {
                    setImageURI(Uri.fromFile(File(storyBase.path)))
                }, 6,storyBase.link))
            } else if (storyBase is StoryModel.TextStoryData) {
                storyList.add(
                    StoriesView(
                        LayoutInflater.from(this).inflate(R.layout.dummy_text_view, null).apply {
                            this?.let {
                                findViewById<ConstraintLayout>(R.id.root).also {
                                    it.setBackgroundColor(resources.getColor(storyBase.bgColor))
                                }
                                findViewById<TextView>(R.id.tv).text = storyBase.text
                            }
                        }, 6
                    )
                )
            }
        }
        StoryMain(this, storyList, binding.root, this,onSwipe = {

        }).start()
    }

    override fun onNextCalled(view: View, storyMain: StoryMain, index: Int) {

    }

    override fun done() {
        Toast.makeText(this@TestActivity, "Finished!", Toast.LENGTH_LONG).show()
        finish()
    }

}
