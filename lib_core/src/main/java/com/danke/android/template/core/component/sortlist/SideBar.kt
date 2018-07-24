package com.danke.android.template.core.component.sortlist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.danke.android.template.core.R

/**
 * @author danke
 * @date 2018/7/20
 */
class SideBar : View {

    companion object {
        @JvmStatic
        val BAR_VALUE = arrayOf(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")
    }

    private var mOnTouchingLetterChangedListener: OnTouchingLetterChangedListener? = null
    private var mTextDialog: TextView? = null
    private var mChoose = -1

    private val mPaint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setTextView(textDialog: TextView) {
        this.mTextDialog = textDialog
    }

    fun setOnTouchingLetterChangedListener(onTouchingLetterChangedListener: OnTouchingLetterChangedListener) {
        this.mOnTouchingLetterChangedListener = onTouchingLetterChangedListener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = height
        val width = width
        val singleHeight = height / BAR_VALUE.size

        for (i in BAR_VALUE.indices) {
            mPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
            mPaint.typeface = Typeface.DEFAULT_BOLD
            mPaint.isAntiAlias = true
            mPaint.textSize = resources.getDimensionPixelSize(R.dimen.sidebar_text).toFloat()

            if (i == mChoose) {
                mPaint.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                mPaint.isFakeBoldText = true
            }

            val xPos = width / 2 - mPaint.measureText(BAR_VALUE[i]) / 2
            val yPos = (singleHeight * i + singleHeight).toFloat()
            canvas.drawText(BAR_VALUE[i], xPos, yPos, mPaint)
            mPaint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y
        val oldChoose = mChoose
        val listener = mOnTouchingLetterChangedListener
        val c = (y / height * BAR_VALUE.size).toInt()

        when (action) {
            MotionEvent.ACTION_UP -> {
                mChoose = -1
                invalidate()
                mTextDialog?.let {
                    it.visibility = View.INVISIBLE
                }
            }
            else -> {
                // judge if chosen letter changed
                if (oldChoose != c) {
                    if (c >= 0 && c < BAR_VALUE.size) {
                        listener?.onTouchingLetterChanged(BAR_VALUE[c])
                        mTextDialog?.let {
                            it.text = BAR_VALUE[c]
                            it.visibility = View.VISIBLE
                        }

                        mChoose = c
                        invalidate()
                    }
                }
            }
        }

        return true
    }
}