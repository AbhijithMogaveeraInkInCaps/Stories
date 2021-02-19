package com.abhijith.stories.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.abhijith.stories.gesture.RotateGestureDetector
import com.abhijith.stories.R
import com.abhijith.stories.colorList


@SuppressLint("ClickableViewAccessibility")
class MyStories(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs){

    var iv: ImageView
    var tv: ZoomTextView
    var bgColor: Int = Color.BLACK
    var cl: ConstraintLayout

    init {
        inflate(context, R.layout.layout_stories, this)
        iv = findViewById(R.id.iv)
        tv = findViewById(R.id.rET)
        cl = findViewById(R.id.root)
    }

    fun readOnly(boolean: Boolean) {

    }

    fun setImage(bitmap: Uri) {
        iv.setImageURI(bitmap)
    }


    //background related
    fun setBackgroundColors(color: Int) {
        cl.setBackgroundColor(color)
        bgColor = color
    }


    private var colorChoice = 0
    fun setRandomColors() {
        val i = colorList[colorChoice]
        cl.setBackgroundColor(ContextCompat.getColor(context, i))
        bgColor = i
        colorChoice++
        if (colorChoice == colorList.size - 1)
            colorChoice = 0
    }
    //Rotation
    inner class RotationListener(context: Context) : RotateGestureDetector(context, object : OnRotateGestureListener {
        var f = 0f
        override fun onRotate(detector: RotateGestureDetector?): Boolean {
            Log.e("Caps","${detector?.rotationDegreesDelta}")
            f -= detector?.rotationDegreesDelta!!
            currentView.rotation = f
            return true
        }

        override fun onRotateBegin(detector: RotateGestureDetector?): Boolean {
            return true
        }

        override fun onRotateEnd(detector: RotateGestureDetector?) {
        }
    })
    val rot = RotationListener(context)

    //scaling
    private var mScaleFactor = 1.5f

    lateinit var currentView: View

    fun getScreenInBitmap(): Bitmap {

        val b = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        layout(left, top, right, bottom)
        draw(c)
        return b
    }

}



