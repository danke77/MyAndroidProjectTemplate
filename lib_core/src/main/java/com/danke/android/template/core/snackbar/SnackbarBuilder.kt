package com.danke.android.template.core.snackbar

import android.app.Activity
import android.support.annotation.StringRes
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.view.View

/**
 * @author danke
 * @date 2018/6/13
 */
class SnackbarBuilder {

    private class SnackbarParams internal constructor(internal val view: View) {

        internal var message: CharSequence = ""

        @BaseTransientBottomBar.Duration
        internal var duration: Int = 0

        init {
            duration = Snackbar.LENGTH_LONG
        }
    }

    private val P: SnackbarParams

    constructor(activity: Activity) {
        P = SnackbarParams(activity.findViewById(android.R.id.content))
    }

    constructor(view: View) {
        P = SnackbarParams(view)
    }

    fun setMessage(text: CharSequence): SnackbarBuilder {
        P.message = text
        return this
    }

    fun setMessage(@StringRes resId: Int): SnackbarBuilder {
        P.message = P.view.resources.getText(resId)
        return this
    }

    fun setDuration(@BaseTransientBottomBar.Duration duration: Int): SnackbarBuilder {
        P.duration = duration
        return this
    }

    fun build(): Snackbar {
        return Snackbar.make(P.view, P.message, P.duration)
    }

    fun show(): Snackbar {
        val snackbar = build()
        snackbar.show()
        return snackbar
    }
}
