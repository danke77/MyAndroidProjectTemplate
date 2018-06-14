package com.danke.android.template.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.FloatRange
import android.support.annotation.RequiresApi
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

import java.util.regex.Pattern

/**
 * @author danke
 * @date 2018/6/13
 */
object StatusBarUtil {

    private val DEFAULT_COLOR = 0
    private val DEFAULT_ALPHA = 0f // Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.2f : 0.3f;
    private val MIN_API = Build.VERSION_CODES.KITKAT

    /**
     * 判断是否Flyme4以上
     */
    val isFlyme4Later: Boolean
        @JvmStatic
        get() = (Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find())

    /**
     * 判断是否为MIUI6以上
     */
    val isMIUI6Later: Boolean
        @JvmStatic
        get() {
            return try {
                @SuppressLint("PrivateApi") val clz = Class.forName("android.os.SystemProperties")
                val mtd = clz.getMethod("get", String::class.java)
                var `val` = mtd.invoke(null, "ro.miui.ui.version.name") as String
                `val` = `val`.replace("[vV]".toRegex(), "")
                val version = Integer.parseInt(`val`)
                version >= 6
            } catch (e: Exception) {
                false
            }

        }

    //<editor-fold desc="沉侵">
    @JvmStatic
    @JvmOverloads
    fun immersive(activity: Activity, color: Int = DEFAULT_COLOR,
                  @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA) {
        immersive(activity.window, color, alpha)
    }

    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    @JvmOverloads
    fun immersive(window: Window, color: Int = DEFAULT_COLOR,
                  @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = mixtureColor(color, alpha)

            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(window.decorView as ViewGroup, color, alpha)
        }
    }
    //</editor-fold>

    //<editor-fold desc="DarkMode">
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun darkMode(activity: Activity, dark: Boolean) {
        when {
            isFlyme4Later -> darkModeForFlyme4(activity.window, dark)
            isMIUI6Later -> darkModeForMIUI6(activity.window, dark)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> darkModeForM(activity.window, dark)
        }
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    @JvmStatic
    @JvmOverloads
    fun darkMode(activity: Activity, color: Int = DEFAULT_COLOR,
                 @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA) {
        darkMode(activity.window, color, alpha)
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    @JvmOverloads
    fun darkMode(window: Window, color: Int = DEFAULT_COLOR,
                 @FloatRange(from = 0.0, to = 1.0) alpha: Float = DEFAULT_ALPHA) {
        when {
            isFlyme4Later -> {
                darkModeForFlyme4(window, true)
                immersive(window, color, alpha)
            }
            isMIUI6Later -> {
                darkModeForMIUI6(window, true)
                immersive(window, color, alpha)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                darkModeForM(window, true)
                immersive(window, color, alpha)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                setTranslucentView(window.decorView as ViewGroup, color, alpha)
            }
            else -> immersive(window, color, alpha)
        }
    }
    //</editor-fold>

    /**
     * android 6.0设置字体颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @JvmStatic
    private fun darkModeForM(window: Window, dark: Boolean) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (dark) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    @JvmStatic
    fun darkModeForFlyme4(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val e = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(e)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }

                meizuFlags.setInt(e, value)
                window.attributes = e
                result = true
            } catch (var8: Exception) {
            }

        }

        return result
    }

    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     */
    @JvmStatic
    fun darkModeForMIUI6(window: Window, darkmode: Boolean): Boolean {
        val clazz = window.javaClass
        return try {
            val darkModeFlag: Int
            @SuppressLint("PrivateApi") val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.invoke(window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     */
    @JvmStatic
    fun setPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(view.paddingLeft, view.paddingTop + getStatusBarHeight(context),
                    view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     */
    @JvmStatic
    fun setPaddingSmart(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context)//增高
            }
            view.setPadding(view.paddingLeft, view.paddingTop + getStatusBarHeight(context),
                    view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    @JvmStatic
    fun setHeightAndPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            lp.height += getStatusBarHeight(context)//增高
            view.setPadding(view.paddingLeft, view.paddingTop + getStatusBarHeight(context),
                    view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     */
    @JvmStatic
    fun setMargin(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            if (lp is ViewGroup.MarginLayoutParams) {
                lp.topMargin += getStatusBarHeight(context)//增高
            }
            view.layoutParams = lp
        }
    }

    /**
     * 创建假的透明栏
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun setTranslucentView(container: ViewGroup, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mixtureColor = mixtureColor(color, alpha)
            var translucentView: View? = container.findViewById(android.R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = android.R.id.custom
                val lp = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.context))
                container.addView(translucentView, lp)
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(mixtureColor)
            }
        }
    }

    @JvmStatic
    fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color.ushr(24)
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result.toFloat(), Resources.getSystem().displayMetrics).toInt()
        }
        return result
    }

}
