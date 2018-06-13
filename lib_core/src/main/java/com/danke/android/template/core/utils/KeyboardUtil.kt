package com.danke.android.template.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author danke
 * @date 2018/6/13
 */
object KeyboardUtil {

    /**
     * 输入法是否显示
     *
     * @param context context
     */
    fun isActive(context: Context): Boolean {
        val imm = getInputMethodManager(context)
        return imm != null && imm.isActive
    }

    /**
     * 强制显示输入法
     */
    fun show(window: Window) {
        show(window.currentFocus)
    }

    fun show(view: View) {
        val imm = getInputMethodManager(view) ?: return
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    @SuppressLint("CheckResult")
    fun show(view: View, milliseconds: Long) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { show(view) }
    }

    /**
     * 强制关闭输入法
     */
    fun hide(window: Window) {
        hide(window.currentFocus)
    }

    fun hide(view: View) {
        view.clearFocus()
        val imm = getInputMethodManager(view) ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideSoftInputOnFocus(editText: EditText) {
        val currentVersion = Build.VERSION.SDK_INT
        var methodName: String? = null
        if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            // 4.2
            methodName = "setShowSoftInputOnFocus"
        } else if (currentVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // 4.0
            methodName = "setSoftInputShownOnFocus"
        }

        if (methodName == null) {
            editText.inputType = InputType.TYPE_NULL
        } else {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus: Method
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, Boolean::class.javaPrimitiveType)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(editText, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
     */
    fun toggle(context: Context) {
        val imm = getInputMethodManager(context) ?: return

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 切换为英文输入模式
     */
    fun changeToEnglishInputType(editText: EditText) {
        editText.inputType = EditorInfo.TYPE_TEXT_VARIATION_URI
    }

    /**
     * 切换为中文输入模式
     */
    fun changeToChineseInputType(editText: EditText) {
        editText.inputType = EditorInfo.TYPE_CLASS_TEXT
    }

    /**
     * 监听输入法的回车按键
     */
    fun setEnterKeyListener(editText: EditText, listener: View.OnClickListener) {
        editText.setOnKeyListener { v, keyCode, event ->
            // 这两个条件必须同时成立，如果仅仅用了enter判断，就会执行两次
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                listener.onClick(v)
                true
            }else {
                false
            }
        }
    }

    private fun getInputMethodManager(context: Context): InputMethodManager? {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun getInputMethodManager(view: View): InputMethodManager? {
        val context = view.context ?: return null

        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
}
