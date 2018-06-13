package com.danke.android.template.core.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils

import java.io.FileInputStream
import java.io.IOException

/**
 * @author danke
 * @date 2018/6/13
 */
object ProcessUtil {

    /**
     * 当前进程是否是主进程
     *
     * @param context
     * @return
     */
    fun isMainProc(context: Context): Boolean {
        val myPid = Process.myPid()
        var procName = readProcName(context, myPid)
        if (TextUtils.isEmpty(procName)) {
            procName = readProcName(myPid)
        }

        return context.packageName == procName
    }


    /**
     * 从 android runtime 中读出进程名称
     *
     * @param context
     * @param myPid
     * @return
     */
    fun readProcName(context: Context, myPid: Int): String? {
        var myProcess: ActivityManager.RunningAppProcessInfo? = null
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val list = activityManager.runningAppProcesses
        if (list != null) {
            try {
                for (info in list) {
                    if (info.pid == myPid) {
                        myProcess = info
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (myProcess != null) {
                return myProcess.processName
            }
        }

        return null
    }

    /**
     * 从内核中读取进程名称
     *
     * @param myPid
     * @return
     */
    fun readProcName(myPid: Int): String? {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream("/proc/$myPid/cmdline")
            val buffer = ByteArray(128)
            val len = fileInputStream.read(buffer)
            if (len <= 0) {
                return null
            }
            var index = 0
            while (index < buffer.size) {
                if (buffer[index] > 128 || buffer[index] <= 0) {
                    break
                }
                index++
            }

            return String(buffer, 0, index)
        } catch (ignore: Exception) {
            ignore.printStackTrace()
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close()
                }
            } catch (ignore: IOException) {
            }

        }

        return null
    }
}
