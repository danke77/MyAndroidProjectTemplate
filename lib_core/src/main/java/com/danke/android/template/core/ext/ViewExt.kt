package com.danke.android.template.core.ext

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @author danke
 * @date 2018/6/13
 */

fun View.isVisible() = visibility == View.VISIBLE

fun View.throttleFirst(onNext: Consumer<Any>): Disposable {
    return throttleFirst(500, onNext)
}

fun View.throttleFirst(duration: Long,
                       onNext: Consumer<Any>): Disposable {
    return RxView.clicks(this)
            .throttleFirst(duration, TimeUnit.MILLISECONDS)
            .subscribe(onNext)
}

fun View.setSize(height: Int, width: Int) {
    this.requestLayout()
    this.layoutParams.height = height
    this.layoutParams.width = width
}

fun TextView.setDrawableLeft(@DrawableRes resId: Int) {
    setDrawableLeft(ContextCompat.getDrawable(context, resId))
}

fun TextView.setDrawableLeft(drawableLeft: Drawable?) {
    val drawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawables[1], drawables[2], drawables[3])
}

fun TextView.setDrawableTop(@DrawableRes resId: Int) {
    setDrawableTop(ContextCompat.getDrawable(context, resId))
}

fun TextView.setDrawableTop(drawableTop: Drawable?) {
    val drawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawableTop, drawables[2], drawables[3])
}

fun TextView.setDrawableRight(@DrawableRes resId: Int) {
    setDrawableRight(ContextCompat.getDrawable(context, resId))
}

fun TextView.setDrawableRight(drawableRight: Drawable?) {
    val drawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableRight, drawables[3])
}

fun TextView.setDrawableBottom(@DrawableRes resId: Int) {
    setDrawableBottom(ContextCompat.getDrawable(context, resId))
}

fun TextView.setDrawableBottom(drawableBottom: Drawable?) {
    val drawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawableBottom)
}