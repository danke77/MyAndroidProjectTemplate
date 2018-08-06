package com.danke.android.template.core.component.recycler

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * @author danke
 * @date 2018/8/6
 */
class SmoothScrollLayoutManager : LinearLayoutManager {

    private val mContext: Context?

    constructor(context: Context?) : super(context) {
        mContext = context
    }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        mContext = context
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        mContext = context
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(mContext) {

            override fun calculateTimeForScrolling(dx: Int): Int {
                return super.calculateTimeForScrolling(if (dx > 1000) 1000 else dx)
            }

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@SmoothScrollLayoutManager.computeScrollVectorForPosition(targetPosition)
            }
        }

        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }
}
