package com.danke.android.template.core.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * @author danke
 * @date 2018/6/13
 */
object WindowUtil {

    /**
     * 设置是否全屏
     *
     * @param window
     * @param fullscreen
     */
    @JvmStatic
    fun toggleFullscreen(window: Window, fullscreen: Boolean) {
        val attrs = window.attributes
        if (fullscreen) {
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }

        window.attributes = attrs
    }

    /**
     * Enables regular immersive mode.
     * For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
     * Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
     *
     * @param window
     */
    @JvmStatic
    @JvmOverloads
    fun hideSystemUI(window: Window, sticky: Boolean = true) {
        val decorView = window.decorView
        decorView.systemUiVisibility = ((if (sticky) View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else View.SYSTEM_UI_FLAG_IMMERSIVE)
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    /**
     * Shows the system bars by removing all the flags
     * except for the ones that make the content appear under the system bars.
     *
     * @param window
     */
    @JvmStatic
    fun showSystemUI(window: Window) {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return wm.defaultDisplay.height
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return wm.defaultDisplay.width
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    @JvmStatic
    fun getStatusBarHeight(activity: Activity): Int {
        //屏幕
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        //应用区域
        val outRect1 = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(outRect1)
        return dm.heightPixels - outRect1.height()  //状态栏高度=屏幕高度-应用区域高度
    }
}

