package com.abhijith.stories.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.abhijith.stories.StoryModel
import com.abhijith.stories.colorList
import com.abhijith.stories.databinding.LayoutTextStoriesBinding
import com.abhijith.stories.ui.view.AutoResizeEditText

class TextStatusActivity : AppCompatActivity() {
    private lateinit var binding: LayoutTextStoriesBinding
    private var colorChoice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutTextStoriesBinding.inflate(layoutInflater)

        binding.btnChangeColor.setOnClickListener {
            colorChoice++
            val i = colorList[colorChoice]
            binding.root.setBackgroundColor(ContextCompat.getColor(this, i))
            if (colorChoice == colorList.size - 1)
                colorChoice = 0
        }

        setContentView(binding.root)
        binding.rET.apply {
            isEnabled = true
            isFocusable = true
            isFocusableInTouchMode = true
        }
        binding.btnSaveStatus.setOnClickListener {
            binding.rET.textSize
            StoryModel.insertTextStory(
                StoryModel.TextStoryData(
                    System.currentTimeMillis(),
                    binding.rET.textSize,
                    binding.rET.text.toString(),
                    colorList[colorChoice]
                )
            )
            finish()
        }
//        setupUI(findViewById(R.id.rlRoot), binding.rET)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(view: View, aText: AutoResizeEditText) {

        if (view !is AutoResizeEditText) {
            view.setOnTouchListener(OnTouchListener { _, _ ->
//                hideSoftKeyboard()
                Log.d("TXTS", "Text Size = " + aText.textSize)
                if (aText.textSize < 50f) {
                    aText.setText(aText.text.toString().replace("(?m)^[ \t]*\r?\n".toRegex(), ""))
                }
                false
            })
        }

        // If a layout container, iterate over children and seed recursion.
//        if (view is ViewGroup) {
//            for (i in 0 until view.childCount) {
//                val innerView: View = view.getChildAt(i)
//                setupUI(innerView, aText)
//            }
//        }
    }

//    private fun hideSoftKeyboard() {
//        val inputMethodManager: InputMethodManager = this
//            .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        if (this.currentFocus != null
//            && this.currentFocus!!.windowToken != null
//        ) inputMethodManager.hideSoftInputFromWindow(
//            this
//                .currentFocus!!.windowToken, 0
//        )
//    }
}