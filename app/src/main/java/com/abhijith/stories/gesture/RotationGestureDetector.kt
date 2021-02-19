package com.abhijith.stories.gesture

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2

class RotationGestureDetector(private val mListener: OnRotationGestureListener) {
    private var fX = 0f
    private var fY = 0f
    private var sX = 0f
    private var sY = 0f
    private var ptrID1: Int
    private var ptrID2: Int
    var angle = 0f
        private set

    fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> ptrID1 = event.getPointerId(
                event.actionIndex
            )
            MotionEvent.ACTION_POINTER_DOWN -> {
                ptrID2 = event.getPointerId(event.actionIndex)
                sX = event.getX(event.findPointerIndex(ptrID1))
                sY = event.getY(event.findPointerIndex(ptrID1))
                fX = event.getX(event.findPointerIndex(ptrID2))
                fY = event.getY(event.findPointerIndex(ptrID2))
            }
            MotionEvent.ACTION_MOVE -> if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                val nsX: Float = event.getX(event.findPointerIndex(ptrID1))
                val nsY: Float = event.getY(event.findPointerIndex(ptrID1))
                val nfX: Float = event.getX(event.findPointerIndex(ptrID2))
                val nfY: Float = event.getY(event.findPointerIndex(ptrID2))
                angle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                mListener?.OnRotation(this)
            }
            MotionEvent.ACTION_UP -> ptrID1 = INVALID_POINTER_ID
            MotionEvent.ACTION_POINTER_UP -> ptrID2 = INVALID_POINTER_ID
            MotionEvent.ACTION_CANCEL -> {
                ptrID1 = INVALID_POINTER_ID
                ptrID2 = INVALID_POINTER_ID
            }
        }
        return true
    }


    private fun angleBetweenLines(
        fX: Float,
        fY: Float,
        sX: Float,
        sY: Float,
        nfX: Float,
        nfY: Float,
        nsX: Float,
        nsY: Float
    ): Float {
        val angle1 = Math.atan2((fY - sY).toDouble(), (fX - sX).toDouble())
            .toFloat()
        val angle2 = Math.atan2((nfY - nsY).toDouble(), (nfX - nsX).toDouble())
            .toFloat()
        var angle = Math.toDegrees((angle1 - angle2).toDouble())
            .toFloat() % 360
//        if (angle < -180f) angle += 360.0f
//        if (angle > 180f) angle -= 360.0f
        return angle
    }

    var currentView: View? = null
    fun onTouchEvent(motionEvent: MotionEvent?, view: View?) {
        currentView = view
        onTouchEvent(motionEvent)
    }

    interface OnRotationGestureListener {
        fun OnRotation(rotationDetector: RotationGestureDetector?)
    }

    companion object {
        fun rotation(event: MotionEvent): Float {
            val delta_x = (event.getX(0) - event.getX(1)).toDouble()
            val delta_y = (event.getY(0) - event.getY(1)).toDouble()
            val radians = atan2(delta_y, delta_x)
            return Math.toDegrees(radians).toFloat()
        }
        fun spacing(event: MotionEvent): Float {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            return Math.sqrt((x * x + y * y).toDouble()).toFloat()
        }
        fun midPoint(point: PointF, event: MotionEvent) {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            point[x / 2] = y / 2
        }
        private const val INVALID_POINTER_ID = -1
    }

    init {
        ptrID1 = INVALID_POINTER_ID
        ptrID2 = INVALID_POINTER_ID
    }
}