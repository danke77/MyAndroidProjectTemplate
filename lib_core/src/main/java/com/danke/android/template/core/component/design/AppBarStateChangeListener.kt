package com.danke.android.template.core.component.design

import android.support.design.widget.AppBarLayout

/**
 * @author danke
 * @date 2018/8/13
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            }
            Math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}