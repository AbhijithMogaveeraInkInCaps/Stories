package com.abhijith.stories.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.atan2

class ZoomableImageView(context: Context?, attrs: AttributeSet?) :
    AppCompatImageView(context!!, attrs) {

    // These matrices will be used to move and zoom image
    var myMatrix = Matrix()
    var savedMatrix = Matrix()
    var mode = NONE

    // Remember some things for zooming
    var start = PointF()
    var mid = PointF()
    var oldDist = 1f
    var d = 0f
    var lastEvent: FloatArray? = FloatArray(4)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        scaleType = ScaleType.MATRIX
        val scale: Float
        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(myMatrix)
                start[event.x] = event.y
                mode = DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    savedMatrix.set(myMatrix)
                    midPoint(mid, event)
                    mode = ZOOM
                }
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
                d = rotation(event)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }


            MotionEvent.ACTION_MOVE ->
                if (mode == DRAG) {
                    myMatrix.set(savedMatrix)
                    myMatrix.postTranslate(
                        event.x - start.x,
                        event.y - start.y
                    )
                } else if (mode == ZOOM && event.pointerCount == 2) {
                    val newDist = spacing(event)
                    myMatrix.set(savedMatrix)
                    if (newDist > 10f) {
                        scale = newDist / oldDist
                        myMatrix.postScale(scale, scale, mid.x, mid.y)
                    }
                    if (lastEvent != null) {
                        val newRot = rotation(event)
                        val r = newRot - d
                        myMatrix.postRotate(
                            r, measuredWidth / 2f,
                            measuredHeight / 2f
                        )
                    }
                }
        }
        // Perform the transformation
        imageMatrix = myMatrix
        return true // indicate event was handled
    }

    private fun rotation(event: MotionEvent): Float {
        val delta_x = (event.getX(0) - event.getX(1)).toDouble()
        val delta_y = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(delta_y, delta_x)
        return Math.toDegrees(radians).toFloat()
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    companion object {
        // We can be in one of these 3 states
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
    }
}