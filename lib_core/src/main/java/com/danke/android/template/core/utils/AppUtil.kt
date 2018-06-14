package com.danke.android.template.core.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri

import com.danke.android.template.core.base.app.BaseApp

import java.io.File

/**
 * @author danke
 * @date 2018/6/13
 */
object AppUtil {

    private var mPackageInfo: PackageInfo? = null

    val packageInfo: PackageInfo?
        @JvmStatic
        get() {
            try {
                val packageManager = BaseApp.get().packageManager
                return packageManager.getPackageInfo(BaseApp.get().packageName, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

    val versionName: String
        @JvmStatic
        get() {
            if (mPackageInfo == null) {
                mPackageInfo = packageInfo
            }

            return if (mPackageInfo != null && mPackageInfo!!.versionName != null) {
                mPackageInfo!!.versionName
            } else ""

        }

    val versionCode: Int
        @JvmStatic
        get() {
            if (mPackageInfo == null) {
                mPackageInfo = packageInfo
            }

            return if (mPackageInfo != null) {
                mPackageInfo!!.versionCode
            } else 0

        }

    /**
     * 检查应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    @JvmStatic
    fun apkIsInstalled(context: Context, packageName: String): Boolean {
        var hasInstalled = false
        val pm = context.packageManager
        val list = pm.getInstalledPackages(0)
        for (p in list) {
            if (packageName == p.packageName) {
                hasInstalled = true
                break
            }
        }

        return hasInstalled
    }


    /**
     * 安装 App 的 Intent
     *
     * @param file
     * @return
     */
    @JvmStatic
    fun getInstallApkIntent(file: File): Intent {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")

        return intent
    }

    /**
     * 安装应用
     */
    @JvmStatic
    fun installApk(context: Context, file: File) {
        if (!file.exists()) {
            return
        }

        context.startActivity(getInstallApkIntent(file))
    }

    /**
     * Service 是否运行
     *
     * @param context
     * @param serviceClass
     * @return
     */
    @JvmStatic
    fun checkServiceIsRunning(context: Context,
                              serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }

        return false
    }

    /**
     * 启动 App
     *
     * @param context
     */
    @JvmStatic
    fun launchApp(context: Context) {
        val intent = context.packageManager
                .getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }
}
