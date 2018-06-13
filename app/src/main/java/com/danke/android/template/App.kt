package com.danke.android.template

import com.alibaba.android.arouter.launcher.ARouter
import com.danke.android.template.core.base.app.BaseApp

/**
 * @author danke
 * @date 2018/6/13
 */
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initRouter()
    }

    private fun initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }

        ARouter.init(this)
    }

    override val appName: String
        get() = getString(R.string.app_name)

}
