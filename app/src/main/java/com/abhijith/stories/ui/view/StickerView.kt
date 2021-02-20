package com.abhijith.stories.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import com.abhijith.stories.gesture.RotateGestureDetector


class StickerView : AppCompatImageView {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1f
    private var defaultSize = 0f
    private var zoomLimit = 5.0f
    var loc: Int = 0

    //draging
    var dX: Float = 0f
    var dY = 0f
    var lastAction = 0

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    inner class RotationListener(context: Context) : RotateGestureDetector(context, object :
        OnRotateGestureListener {
        var f = 0f
        override fun onRotate(detector: RotateGestureDetector?): Boolean {
            Log.e("Caps", "${detector?.rotationDegreesDelta}")
            f -= detector?.rotationDegreesDelta!!
            rotation = f
            invalidate()
            return true
        }

        override fun onRotateBegin(detector: RotateGestureDetector?): Boolean {
            return true
        }

        override fun onRotateEnd(detector: RotateGestureDetector?) {
        }
    })

    val rot = RotationListener(context)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    init {
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        mScaleDetector!!.onTouchEvent(event)
        if (event.pointerCount == 2)
            rot.onTouchEvent(event)
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                dX = x - event.rawX
                dY = y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
            }

            MotionEvent.ACTION_MOVE -> {
                y = event.rawY + dY
                x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }

            MotionEvent.ACTION_UP ->
                if (lastAction == MotionEvent.ACTION_DOWN) {
                }
            else ->
                return false
        }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 3f))
            scaleX = mScaleFactor
            scaleY = mScaleFactor
            return true
        }
    }
}