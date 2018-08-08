package com.danke.android.template.core.component.swipe

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * @author danke
 * @date 2018/8/8
 */
class VpSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var startY: Float = 0.toFloat()
    private var startX: Float = 0.toFloat()
    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    // 记录viewPager是否拖拽的标记
    private var isViewPagerDragging: Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                startY = ev.y
                startX = ev.x
                // 初始化标记
                isViewPagerDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false
                if (isViewPagerDragging) {
                    return false
                }

                // 获取当前手指位置
                val endY = ev.y
                val endX = ev.x
                val distanceX = Math.abs(endX - startX)
                val distanceY = Math.abs(endY - startY)
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理
                if (distanceX > touchSlop && distanceX > distanceY) {
                    isViewPagerDragging = true
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                // 初始化标记
                isViewPagerDragging = false
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理
        return super.onInterceptTouchEvent(ev)
    }
}