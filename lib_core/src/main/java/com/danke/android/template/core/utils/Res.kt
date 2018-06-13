package com.danke.android.template.core.utils

import android.content.Context

/**
 * @author danke
 * @date 2018/6/13
 */
object Res {

    fun getResIdByName(context: Context, resName: String, resType: String): Int {
        return context.resources.getIdentifier(resName, resType, context.packageName)
    }
}
