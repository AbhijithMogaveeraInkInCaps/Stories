package com.abhijith.stories.gesture

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast

open class RotateGestureDetector(
    context: Context?,
    private val mListener: OnRotateGestureListener
) : TwoFingerGestureDetector(context) {
    interface OnRotateGestureListener {
        fun onRotate(detector: RotateGestureDetector?): Boolean
        fun onRotateBegin(detector: RotateGestureDetector?): Boolean
        fun onRotateEnd(detector: RotateGestureDetector?)
    }

    class SimpleOnRotateGestureListener : OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector?): Boolean {
            return false
        }

        override fun onRotateBegin(detector: RotateGestureDetector?): Boolean {
            return true
        }

        override fun onRotateEnd(detector: RotateGestureDetector?) {
            // Do nothing, overridden implementation may be used
        }
    }

    private var mSloppyGesture = false
    private var thresholds = 25
    override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                // At least the second finger is on screen now
                resetState() // In case we missed an UP/CANCEL event
                mPrevEvent = MotionEvent.obtain(event)
                mTimeDelta = 0
                updateStateByEvent(event)

                // See if we have a sloppy gesture
                mSloppyGesture = isSloppyGesture(event)
                if (mSloppyGesture) {
                    // No, start gesture now
                    mGestureInProgress = mListener.onRotateBegin(this)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mSloppyGesture) {

                }

                // See if we still have a sloppy gesture
                mSloppyGesture = isSloppyGesture(event)
                if (!isSloppyGesture(event))
                    Toast.makeText(mContext, "bro", Toast.LENGTH_SHORT).show()
                if (!mSloppyGesture||true) {
                    // No, start normal gesture now
                    mGestureInProgress = mListener.onRotateBegin(this)}
            }
            MotionEvent.ACTION_POINTER_UP -> if (!mSloppyGesture) {

            }
        }
    }

    override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_POINTER_UP -> {
                // Gesture ended but
                updateStateByEvent(event)
                if (!mSloppyGesture) {
                    mListener.onRotateEnd(this)
                }
                resetState()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!mSloppyGesture) {
                    mListener.onRotateEnd(this)
                }
                resetState()
            }
            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)
                if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
                    val updatePrevious = mListener.onRotate(this)
                    if (updatePrevious) {
                        mPrevEvent.recycle()
                        mPrevEvent = MotionEvent.obtain(event)
                    }
                }
            }
        }
    }

    override fun resetState() {
        super.resetState()
        mSloppyGesture = false
    }
    val rotationDegreesDelta: Float
        get() {
            val diffRadians =
                Math.atan2(mPrevFingerDiffY.toDouble(), mPrevFingerDiffX.toDouble()) - Math.atan2(
                    mCurrFingerDiffY.toDouble(),
                    mCurrFingerDiffX.toDouble()
                )
            return (diffRadians * 180 / Math.PI).toFloat()
        }
}