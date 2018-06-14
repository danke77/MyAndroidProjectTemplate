package com.danke.android.template.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.text.TextUtils
import android.widget.Toast

import me.drakeet.support.toast.ToastCompat

/**
 * @author danke
 * @date 2018/6/13
 */
@MainThread
object ToastUtil {

    private var mToast: ToastCompat? = null

    @JvmStatic
    fun show(context: Context?, @StringRes message: Int) {
        if (context == null) {
            return
        }

        show(context, context.getString(message))
    }

    @SuppressLint("ShowToast")
    @JvmStatic
    fun show(context: Context?, message: String) {
        if (context == null || TextUtils.isEmpty(message)) {
            return
        }

        val duration = Toast.LENGTH_SHORT
        if (mToast == null) {
            mToast = ToastCompat.makeText(context.applicationContext, message, duration)
        } else {
            mToast!!.setText(message)
            mToast!!.duration = duration
        }
        mToast!!.show()
    }
}
