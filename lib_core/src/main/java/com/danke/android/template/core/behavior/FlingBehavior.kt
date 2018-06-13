package com.danke.android.template.core.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * @author danke
 * @date 2018/6/13
 */
class FlingBehavior : AppBarLayout.Behavior {

    companion object {
        private const val TOP_CHILD_FLING_THRESHOLD = 3
    }

    private var isPositive: Boolean = false

    constructor() {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout,
                               child: AppBarLayout,
                               target: View,
                               velocityX: Float,
                               velocityY: Float,
                               consumed: Boolean): Boolean {
        var targetValue = target
        var velocityYValue = velocityY
        var consumedValue = consumed

        if (velocityYValue > 0 && !isPositive || velocityYValue < 0 && isPositive) {
            velocityYValue *= -1
        }

        if (targetValue is SwipeRefreshLayout && velocityYValue < 0) {
            targetValue = targetValue.getChildAt(0)
        }

        if (targetValue is RecyclerView && velocityY < 0) {
            val recyclerView = targetValue
            val firstChild = recyclerView.getChildAt(0)
            val childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild)
            consumedValue = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
        }
        return super.onNestedFling(coordinatorLayout, child, targetValue, velocityX, velocityYValue, consumedValue)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: AppBarLayout,
                                   target: View,
                                   dx: Int, dy: Int, consumed: IntArray,
                                   @ViewCompat.NestedScrollType type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        isPositive = dy > 0
    }
}
