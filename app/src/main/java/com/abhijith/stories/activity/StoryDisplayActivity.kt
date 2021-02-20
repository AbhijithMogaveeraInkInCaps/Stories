package com.abhijith.stories.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.abhijith.stories.R
import com.abhijith.stories.databinding.EmptyLayoutBinding
import com.abhijith.stories.extension.setAddress
import com.abhijith.stories.extension.toFileUri
import com.abhijith.stories.model.StoryModel
import com.abhijith.stories.storyscreeen.StoryMain
import com.abhijith.stories.storyscreeen.callback.StoriesCallback
import com.abhijith.stories.storyscreeen.data.StoriesView
import com.abhijith.stories.ui.view.StickerView

class StoryDisplayActivity : AppCompatActivity(), StoriesCallback {

    private lateinit var binding: EmptyLayoutBinding
    private val storyList: MutableList<StoriesView> = mutableListOf()

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EmptyLayoutBinding.inflate(layoutInflater)

        binding.apply {
            setContentView(root)
        }

        StoryModel.getAllStory().forEach { storyBase ->

            val storyView = when (storyBase) {

                is StoryModel.StoryDataImage -> {
                    //totd: addAllStickers
                    val cl = ConstraintLayout(this)
                    val view = ImageView(this).apply {
                        setImageURI(storyBase.path.toFileUri())
                        layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                    }
                    cl.addView(view)

                    storyBase.stickerList.forEach {
                        val stickerView = ImageView(this).apply {
                            setImageDrawable(resources.getDrawable(StoryModel.getStickerList()[it.id]))
                            Toast.makeText(this@StoryDisplayActivity, "bro ${it.id}", Toast.LENGTH_SHORT)
                                .show()
                            setAddress(it.viewAddress)
                        }
//                        Toast.makeText(this, "x = ${stickerView.x} y = ${stickerView.y}", Toast.LENGTH_SHORT).show()
//                        val stickerView = Button(this)
                        cl.addView(stickerView)
                    }
                    StoriesView(cl, 6, storyBase.link)
                }

                is StoryModel.TextStoryData -> {
                    val view =
                        LayoutInflater.from(this).inflate(R.layout.dummy_text_view, null).apply {
                            this?.let {
                                findViewById<ConstraintLayout>(R.id.root).also {
                                    it.setBackgroundColor(resources.getColor(storyBase.bgColor))
                                }
                                findViewById<TextView>(R.id.tv).text = storyBase.text
                            }
                        }
                    StoriesView(view, 6)
                }
                else -> {
                    throw Exception("Unknown data")
                }
            }

            storyList.add(storyView)

        }

        StoryMain(this, storyList, binding.root, this, onSwipe = {}).start()
    }

    override fun onNextCalled(view: View, storyMain: StoryMain, index: Int) {

    }

    override fun done() {
        finish()
    }

    companion object {
        fun startActivity(context: Context) {
            Intent(context, StoryDisplayActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

}
