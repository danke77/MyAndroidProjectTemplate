package com.danke.android.template.core.base.app

import android.app.Application

/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseApp : Application() {

    companion object {

        protected lateinit var sInstance: BaseApp

        @JvmStatic
        fun get(): BaseApp {
            return sInstance
        }
    }

    init {
        sInstance = this@BaseApp
    }

    abstract val appName: String

}
