package com.abhijith.stories.storyscreeen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.abhijith.stories.R
import com.abhijith.stories.gesture.OnSwipeListener
import com.abhijith.stories.storyscreeen.callback.ProgressTimeWatcher
import com.abhijith.stories.storyscreeen.callback.StoriesCallback
import com.abhijith.stories.storyscreeen.data.StoriesView


@SuppressLint("ViewConstructor")
class StoryMain(
    context: Context,
    val storiesViewList: MutableList<StoriesView>,
    private val passedInContainerView: ViewGroup,
    private var storiesCallback: StoriesCallback,
    @DrawableRes
    private var mProgressDrawable: Int = R.drawable.green_lightgrey_drawable,
    val onSwipe: (direction: OnSwipeListener.Direction) -> Unit
) : ConstraintLayout(context) {

    private var currentlyShownIndex = 0
    private lateinit var currentView: View
    private var libSliderViewList = mutableListOf<MyProgressBar>()
    private lateinit var view: View
    private var pausedState: Boolean = false
    lateinit var gestureDetector: GestureDetector
    lateinit var swipeDetector: GestureDetector

    init {
        initView()
        initProgressBar()
        initSelf()
    }

    private fun initProgressBar() {
        storiesViewList.forEachIndexed { index, sliderView ->
            val myProgressBar = MyProgressBar(
                context,
                index,
                sliderView.durationInSeconds,
                object : ProgressTimeWatcher {
                    override fun onEnd(indexFinished: Int) {
                        currentlyShownIndex = indexFinished + 1
                        next()
                    }
                },
                mProgressDrawable
            )
            libSliderViewList.add(myProgressBar)
            view.findViewById<LinearLayout>(R.id.linearProgressIndicatorLay).addView(myProgressBar)
        }
        //start()
    }

    fun callPause(pause: Boolean) {
        try {
            if (pause) {
                if (!pausedState) {
                    this.pausedState = !pausedState
                    pause(false)
                }
            } else {
                if (pausedState) {
                    this.pausedState = !pausedState
                    resume()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        view = View.inflate(context, R.layout.linear_progress_layout, this)

        gestureDetector = GestureDetector(context, SingleTapConfirm())
        val onSwipeListener = object : OnSwipeListener() {
            override fun onSwipe(direction: Direction?): Boolean {
                direction?.let {
                    this@StoryMain.onSwipe(direction)
                    var link = storiesViewList[currentlyShownIndex].link
                    if (direction == Direction.up && link.isNotEmpty()) {
                        if (!link.startsWith("http://") && !link.startsWith("https://"))
                            link = "http://$link";
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(link)
                        )
                        context.startActivity(browserIntent)
                    }
                }
                return true
            }
        }

        swipeDetector = GestureDetector(context, onSwipeListener)

        val touchListener = object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                swipeDetector.onTouchEvent(event)

                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    if (v?.id == findViewById<View>(R.id.rightLay).id) {
                        Log.e("Ink", "onNext()")
                        next()
                    }
                    if (v?.id == findViewById<View>(R.id.leftLay).id) {
                        Log.e("Ink", "onPrev()")
                        prev()
                    }
                    return true
                } else {
                    // your code for move and drag
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            callPause(true)
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            callPause(false)
                            return true
                        }
                        else ->
                            return false
                    }
                }
            }
        }
        view.findViewById<FrameLayout>(R.id.leftLay).setOnTouchListener(touchListener)
        view.findViewById<FrameLayout>(R.id.rightLay).setOnTouchListener(touchListener)
        view.findViewById<FrameLayout>(R.id.swipeDetector).setOnTouchListener(touchListener)
        showOpenLinkOption()

    }

    private fun showOpenLinkOption() {
        if (storiesViewList[currentlyShownIndex].link.isNotEmpty()) {
            view.findViewById<ImageView>(R.id.ivOpenLink).visibility = VISIBLE
        }else{
            view.findViewById<ImageView>(R.id.ivOpenLink).visibility = INVISIBLE
        }
    }

    private fun initSelf() {
        this.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        passedInContainerView.addView(this)
    }

    fun show() {

        view.findViewById<ProgressBar>(R.id.loaderProgressbar).visibility = View.GONE
        if (currentlyShownIndex != 0) {
            for (i in 0..Math.max(0, currentlyShownIndex - 1)) {
                libSliderViewList[i].progress = 100
                libSliderViewList[i].cancelProgress()
            }
        }

        if (currentlyShownIndex != libSliderViewList.size - 1) {
            for (i in currentlyShownIndex + 1 until libSliderViewList.size) {
                libSliderViewList[i].progress = 0
                libSliderViewList[i].cancelProgress()
            }
        }

        currentView = storiesViewList[currentlyShownIndex].view

        libSliderViewList[currentlyShownIndex].startProgress()

        storiesCallback.onNextCalled(currentView, this, currentlyShownIndex)

        view.findViewById<LinearLayout>(R.id.currentlyDisplayedView).removeAllViews()
        view.findViewById<LinearLayout>(R.id.currentlyDisplayedView).addView(currentView)
        val params = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1f
        )
        //params.gravity = Gravity.CENTER_VERTICAL
        if (currentView is ImageView) {
            (currentView as ImageView).scaleType = ImageView.ScaleType.FIT_CENTER
            (currentView as ImageView).adjustViewBounds = true
        }
        currentView.layoutParams = params
    }

    fun start() {
        show()
    }

    fun editDurationAndResume(index: Int, newDurationInSecons: Int) {
        view.findViewById<ProgressBar>(R.id.loaderProgressbar).visibility = View.GONE
        libSliderViewList[index].editDurationAndResume(newDurationInSecons)
    }

    fun pause(withLoader: Boolean) {
        if (withLoader) {
            view.findViewById<ProgressBar>(R.id.loaderProgressbar).visibility = View.VISIBLE
        }
        libSliderViewList[currentlyShownIndex].pauseProgress()
        if (storiesViewList[currentlyShownIndex].view is VideoView) {
            (storiesViewList[currentlyShownIndex].view as VideoView).pause()
        }
    }

    fun resume() {
        view.findViewById<ProgressBar>(R.id.loaderProgressbar).visibility = View.GONE
        libSliderViewList[currentlyShownIndex].resumeProgress()
        if (storiesViewList[currentlyShownIndex].view is VideoView) {
            (storiesViewList[currentlyShownIndex].view as VideoView).start()
        }
        showOpenLinkOption()
    }

    private fun stop() {

    }

    fun next() {
        try {
            if (currentView == storiesViewList[currentlyShownIndex].view) {
                currentlyShownIndex++
                if (storiesViewList.size <= currentlyShownIndex) {
                    finish()
                    return
                }
                showOpenLinkOption()
            }
            show()
        } catch (e: IndexOutOfBoundsException) {
            finish()
        }
    }

    private fun finish() {
        storiesCallback.done()
        for (progressBar in libSliderViewList) {
            progressBar.cancelProgress()
            progressBar.progress = 100
        }
    }

    fun prev() {
        try {
            if (currentView == storiesViewList[currentlyShownIndex].view) {
                currentlyShownIndex--
                if (0 > currentlyShownIndex) {
                    currentlyShownIndex = 0
                }
                showOpenLinkOption()
            }
        } catch (e: IndexOutOfBoundsException) {
            currentlyShownIndex -= 2
        } finally {
            show()
        }
    }

    private inner class SingleTapConfirm : SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }


}