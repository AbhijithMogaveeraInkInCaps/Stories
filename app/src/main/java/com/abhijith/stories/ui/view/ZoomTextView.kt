package com.abhijith.stories.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.abhijith.stories.gesture.RotateGestureDetector
import com.abhijith.stories.dialog.DataInputDialog

class ZoomTextView : AppCompatTextView {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1f
    private var defaultSize = 0f
    private var zoomLimit = 10.0f


    //draging
    var dX: Float = 0f
    var dY = 0f
    var lastAction = 0
    var floatingLayout: LinearLayout? = null

    constructor(context: Context?) : super(context!!) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        defaultSize = textSize
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    fun setZoomLimit(zoomLimit: Float) {
        this.zoomLimit = zoomLimit
    }
    inner class RotationListener(context: Context) : RotateGestureDetector(context, object :
        OnRotateGestureListener {
        var f = 0f
        override fun onRotate(detector: RotateGestureDetector?): Boolean {
            Log.e("Caps","${detector?.rotationDegreesDelta}")
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        mScaleDetector!!.onTouchEvent(event)
        if(event.pointerCount==2)
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
                    onTextClicked()
                }
            else ->
                return false
        }
        return true
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, zoomLimit))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * mScaleFactor)
            Log.e(TAG, mScaleFactor.toString())
            return true
        }
    }

    companion object {
        private const val TAG = "ZoomTextView"
    }

    fun onTextClicked() {
        DataInputDialog(
            context, {
                text = it
            }, {}).show()
    }
}