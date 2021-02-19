package com.abhijith.stories.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText

class AutoResizeEditText
    @JvmOverloads
    constructor(
    context: Context?, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatEditText(context!!, attrs, defStyle) {

    private val mAvailableSpaceRect = RectF()
    private val mTextCachedSizes = SparseIntArray()
    private val mSizeTester: SizeTester
    private var mMaxTextSize: Float
    private var mSpacingMulti = 1.0f
    private var mSpacingAdd = 0.0f
    private var mMinTextSize: Float
    private var mWidthLimit = 0
    private var mMaxLines = 0
    private var mEnableSizeCache = true
    private var mInitialized = false
    private var mTextPaint: TextPaint? = null

    private val sizeTester = object : SizeTester {

        val textRect = RectF()

        override fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int {

            mTextPaint!!.textSize = suggestedSize.toFloat()
            val text = text.toString()
            val singleline = maxLines == 1

            if (singleline) {

                textRect.bottom = mTextPaint!!.fontSpacing
                textRect.right = mTextPaint!!.measureText(text)

            } else {

                val layout = StaticLayout(
                    text, mTextPaint,
                    mWidthLimit, Layout.Alignment.ALIGN_NORMAL, mSpacingMulti,
                    mSpacingAdd, true
                )
                if (maxLines != NO_LINE_LIMIT
                    && layout.lineCount > maxLines
                ) return 1
                textRect.bottom = layout.height.toFloat()
                var maxWidth = -1
                for (i in 0 until layout.lineCount) if (maxWidth < layout.getLineWidth(i)) maxWidth =
                    layout.getLineWidth(i)
                        .toInt()
                textRect.right = maxWidth.toFloat()
            }
            textRect.offsetTo(0f, 0f)
            return if (availableSpace.contains(textRect)) -1 else 1
        }
    }

    init {

        mMinTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
        mMaxTextSize = textSize
        if (mMaxLines == 0) mMaxLines = NO_LINE_LIMIT

        mSizeTester = sizeTester
        mInitialized = true
    }

    interface SizeTester {
        fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int
    }

    override fun setTypeface(tf: Typeface?) {
        if (mTextPaint == null)
            mTextPaint = TextPaint(paint)
        mTextPaint!!.typeface = tf
        super.setTypeface(tf)
    }

    override fun setTextSize(size: Float) {
        mMaxTextSize = size
        mTextCachedSizes.clear()
        adjustTextSize()
    }

    override fun setMaxLines(maxlines: Int) {
        super.setMaxLines(maxlines)
        mMaxLines = maxlines
        reAdjust()
    }

    override fun getMaxLines(): Int {
        return mMaxLines
    }

    override fun setSingleLine() {
        super.setSingleLine()
        mMaxLines = 1
        reAdjust()
    }

    override fun setSingleLine(singleLine: Boolean) {
        super.setSingleLine(singleLine)
        mMaxLines = if (singleLine) 1 else NO_LINE_LIMIT
        reAdjust()
    }

    override fun setLines(lines: Int) {
        super.setLines(lines)
        mMaxLines = lines
        reAdjust()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val c = context
        val r: Resources
        r = if (c == null) Resources.getSystem() else c.resources
        mMaxTextSize = TypedValue.applyDimension(
            unit, size,
            r.displayMetrics
        )
        mTextCachedSizes.clear()
        adjustTextSize()
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        super.setLineSpacing(add, mult)
        mSpacingMulti = mult
        mSpacingAdd = add
    }

    fun setMinTextSize(minTextSize: Float) {
        mMinTextSize = minTextSize
        reAdjust()
    }

    private fun reAdjust() {
        adjustTextSize()
    }

    private fun adjustTextSize() {
        if (!mInitialized)
            return
        val startSize = mMinTextSize.toInt()

        val heightLimit = (measuredHeight - compoundPaddingBottom - compoundPaddingTop)

        mWidthLimit = (measuredWidth - compoundPaddingLeft - compoundPaddingRight)

        if (mWidthLimit <= 0)
            return

        mAvailableSpaceRect.right = mWidthLimit.toFloat()

        mAvailableSpaceRect.bottom = heightLimit.toFloat()

        val size = efficientTextSizeSearch(
            startSize, mMaxTextSize.toInt(),
            mSizeTester, mAvailableSpaceRect
        ).toFloat()

        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }


    fun setEnableSizeCache(enable: Boolean) {
        mEnableSizeCache = enable
        mTextCachedSizes.clear()
        adjustTextSize()
    }

    private fun efficientTextSizeSearch(
        start: Int,
        end: Int,
        sizeTester: SizeTester,
        availableSpace: RectF
    ): Int {
        if (!mEnableSizeCache)
            return binarySearch(start, end, sizeTester, availableSpace)
        val text = text.toString()
        val key = text.length
        var size = mTextCachedSizes[key]
        if (size != 0)
            return size
        size = binarySearch(start, end, sizeTester, availableSpace)
        mTextCachedSizes.put(key, size)
        return size
    }

    private fun binarySearch(
        start: Int, end: Int,
        sizeTester: SizeTester, availableSpace: RectF
    ): Int {
        var lastBest = start
        var lo = start
        var hi = end - 1
        var mid = 0
        while (lo <= hi) {
            mid = lo + hi ushr 1
            val midValCmp = sizeTester.onTestSize(mid, availableSpace)
            when {
                midValCmp < 0 -> {
                    lastBest = lo
                    lo = mid + 1
                }
                midValCmp > 0 -> {
                    hi = mid - 1
                    lastBest = hi
                }
                else ->
                    return mid
            }
        }
        return lastBest
    }

    override fun onTextChanged(
        text: CharSequence, start: Int,
        before: Int, after: Int
    ) {
        super.onTextChanged(text, start, before, after)
        reAdjust()
    }

    override fun onSizeChanged(
        width: Int, height: Int,
        oldwidth: Int, oldheight: Int
    ) {
        mTextCachedSizes.clear()
        super.onSizeChanged(width, height, oldwidth, oldheight)
        if (width != oldwidth || height != oldheight) reAdjust()
    }

    companion object {
        private const val NO_LINE_LIMIT = -1
    }

}