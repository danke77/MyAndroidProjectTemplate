package com.danke.android.template.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.danke.android.template.core.base.app.BaseApp

/**
 * @author danke
 * @date 2018/6/13
 */
object Prefs {

    private val DEFAULT_PREF = PreferenceManager.getDefaultSharedPreferences(BaseApp.get())

    fun get(): SharedPreferences {
        return DEFAULT_PREF
    }

    operator fun get(name: String): SharedPreferences {
        return BaseApp.get().getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}
