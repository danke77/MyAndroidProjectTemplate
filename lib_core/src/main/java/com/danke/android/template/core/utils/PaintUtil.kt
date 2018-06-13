package com.danke.android.template.core.utils

import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter

import java.util.Stack

/**
 * @author danke
 * @date 2018/6/13
 */
object PaintUtil {

    private val sPaintCache = Stack<Paint>()

    val sDrawFilter = PaintFlagsDrawFilter(
            0, Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    fun obtainPaint(): Paint {
        var paint: Paint

        try {
            paint = sPaintCache.pop()
        } catch (e: Exception) {
            paint = Paint()
        }

        paint.reset()
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isSubpixelText = true
        paint.shader = null
        paint.hinting = Paint.HINTING_ON
        paint.isDither = true

        return paint
    }

    fun obtainPaint(textSize: Float): Paint {
        val paint = obtainPaint()
        paint.textSize = textSize
        return paint
    }

    fun recyclePaint(paint: Paint) {
        sPaintCache.push(paint)
    }
}
