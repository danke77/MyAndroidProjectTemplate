package com.danke.android.template.core.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author danke
 * @date 2018/6/13
 */
object ViewUtil {

    @JvmStatic
    fun <LIMIT> createView(cls: Class<out LIMIT>, context: Context): LIMIT? {
        try {
            return cls.getDeclaredConstructor(Context::class.java).newInstance(context)
        } catch (e: Exception) {
        }

        return null
    }

    @JvmStatic
    fun replace(activity: Activity, @IdRes holder: Int, @LayoutRes layout: Int) {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val parent = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup?
        if (parent != null) {
            val viewHolder = activity.findViewById<View>(holder)
            val replace = inflater.inflate(layout, parent, false)
            replace(parent, viewHolder, replace)
        }
    }

    @JvmStatic
    fun replace(activity: Activity, @IdRes holder: Int, replace: View) {
        val parent = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup?
        if (parent != null) {
            val viewHolder = activity.findViewById<View>(holder)
            replace(parent, viewHolder, replace)
        }
    }

    @JvmStatic
    fun replace(parent: ViewGroup, holder: View, replace: View) {
        val index = parent.indexOfChild(holder)
        if (index == -1) {
            return
        }

        parent.removeViewInLayout(holder)

        if (holder.id != View.NO_ID) {
            replace.id = holder.id
        }
        val layoutParams = holder.layoutParams

        if (layoutParams != null) {
            parent.addView(replace, index, layoutParams)
        } else {
            parent.addView(replace, index)
        }
    }

    /**
     * 获取默认的 actionBarSize
     *
     * @param context context
     */
    @JvmStatic
    fun getActionBarSize(context: Context): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarSize = typedArray.getDimension(0, 0f).toInt()
        typedArray.recycle()

        return actionBarSize
    }

    @JvmStatic
    fun of(view: View): Builder {
        return Builder(view)
    }

    class Builder(view: View) {

        private val resources: Resources = view.context.resources
        private var layoutParams: ViewGroup.LayoutParams? = null
        private var view: View? = null
        private var bitsChanged = 0

        init {
            this.view = view
            this.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        fun leftMarginResId(@DimenRes resId: Int): Builder {
            if (layoutParams != null && layoutParams is ViewGroup.MarginLayoutParams) {
                (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = resources.getDimensionPixelSize(resId)
                setBit(BIT_MARGIN_LEFT)
            }
            return this
        }

        fun topMarginResId(@DimenRes resId: Int): Builder {
            if (layoutParams != null && layoutParams is ViewGroup.MarginLayoutParams) {
                (layoutParams as ViewGroup.MarginLayoutParams).topMargin = resources.getDimensionPixelSize(resId)
                setBit(BIT_MARGIN_TOP)
            }
            return this
        }

        fun rightMarginResId(@DimenRes resId: Int): Builder {
            if (layoutParams != null && layoutParams is ViewGroup.MarginLayoutParams) {
                (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = resources.getDimensionPixelSize(resId)
                setBit(BIT_MARGIN_RIGHT)
            }
            return this
        }

        fun bottomMarginResId(@DimenRes resId: Int): Builder {
            if (layoutParams != null && layoutParams is ViewGroup.MarginLayoutParams) {
                (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = resources.getDimensionPixelSize(resId)
                setBit(BIT_MARGIN_BOTTOM)
            }
            return this
        }

        fun verticalMarginResId(@DimenRes resId: Int): Builder {
            if (layoutParams != null && layoutParams is ViewGroup.MarginLayoutParams) {
                val margin = resources.getDimensionPixelSize(resId)
                (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = margin
                (layoutParams as ViewGroup.MarginLayoutParams).topMargin = margin
                setBit(BIT_MARGIN_TOP or BIT_MARGIN_BOTTOM)
            }
            return this
        }

        fun height(height: Int): Builder {
            if (layoutParams != null) {
                layoutParams!!.height = height
                setBit(BIT_HEIGHT)
            }
            return this
        }

        fun width(width: Int): Builder {
            if (layoutParams != null) {
                layoutParams!!.width = width
                setBit(BIT_WIDTH)
            }
            return this
        }

        fun commit() {
            if (view != null && layoutParams != null) {
                val params = view!!.layoutParams
                if (params != null) {
                    // View已经attach到parentView，直接将修改后的LayoutParams设置回去。
                    applyChangedBits(params)
                    view!!.layoutParams = params
                } else {
                    // View还没有attach到parentView，添加一个Listener，等View attach以后再设置。
                    view!!.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                        override fun onViewAttachedToWindow(v: View) {
                            val params = v.layoutParams
                            applyChangedBits(params)
                            v.layoutParams = params
                            v.removeOnAttachStateChangeListener(this)
                        }

                        override fun onViewDetachedFromWindow(v: View) {}
                    })
                }
            }
        }

        private fun hasBit(bit: Int): Boolean {
            return bitsChanged and bit != 0
        }

        private fun setBit(bit: Int) {
            bitsChanged = bitsChanged or bit
        }

        private fun applyChangedBits(params: ViewGroup.LayoutParams) {
            if (hasBit(BIT_WIDTH)) {
                params.width = layoutParams!!.width
            }

            if (hasBit(BIT_HEIGHT)) {
                params.height = layoutParams!!.height
            }

            if (params is ViewGroup.MarginLayoutParams && layoutParams is ViewGroup.MarginLayoutParams) {
                if (hasBit(BIT_MARGIN_LEFT)) {
                    params.leftMargin = (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
                }
                if (hasBit(BIT_MARGIN_TOP)) {
                    params.topMargin = (layoutParams as ViewGroup.MarginLayoutParams).topMargin
                }
                if (hasBit(BIT_MARGIN_RIGHT)) {
                    params.rightMargin = (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
                }
                if (hasBit(BIT_MARGIN_BOTTOM)) {
                    params.bottomMargin = (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
                }
            }
        }

        companion object {
            private const val BIT_WIDTH = 1
            private const val BIT_HEIGHT = 1 shl 1
            private const val BIT_MARGIN_LEFT = 1 shl 2
            private const val BIT_MARGIN_TOP = 1 shl 3
            private const val BIT_MARGIN_RIGHT = 1 shl 4
            private const val BIT_MARGIN_BOTTOM = 1 shl 5
        }
    }
}
