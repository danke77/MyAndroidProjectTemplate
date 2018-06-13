package com.danke.android.template.core.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat

/**
 * @author danke
 * @date 2018/6/13
 */


fun Drawable.tint(@ColorInt color: Int): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(this)
    DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color))

    return this
}