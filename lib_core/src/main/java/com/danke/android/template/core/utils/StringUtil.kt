package com.danke.android.template.core.utils

import android.content.Context

import java.util.regex.Pattern

/**
 * @author danke
 * @date 2018/6/13
 */
object StringUtil {

    private const val KB = (1 shl 10).toLong()
    private const val MB = (1 shl 20).toLong()
    private const val GB = (1 shl 30).toLong()

    private const val REGEX_NUMBER = "[-+]?\\d*\\.?\\d+"

    private const val REGEX_CHINESE = "[\u0391-\uFFE5]"

    private const val REGEX_MAILTO = "^mailto:.+"

    private const val REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"

    private const val REGEX_HTTP_URL = "^((http|ftp|https)://)?(" +
            "([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1," +
            "3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?"

    @JvmStatic
    fun isEmpty(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.isEmpty()
    }

    @JvmStatic
    fun isNotEmpty(content: String): Boolean {
        return !isEmpty(content)
    }

    @JvmStatic
    fun isNumeric(text: String): Boolean {
        return isNotEmpty(text) && check(text, REGEX_NUMBER)
    }

    @JvmStatic
    fun isChinese(text: String): Boolean {
        return isNotEmpty(text) && check(text, REGEX_CHINESE)
    }

    // 处理html里面的发邮件标记
    @JvmStatic
    fun isEmailTo(text: String): Boolean {
        return isNotEmpty(text) && check(text, REGEX_MAILTO)
    }

    @JvmStatic
    fun isEmail(text: String): Boolean {
        return isNotEmpty(text) && check(text, REGEX_EMAIL)
    }

    @JvmStatic
    fun isHttpUrl(text: String): Boolean {
        return isNotEmpty(text) && check(text, REGEX_HTTP_URL)
    }

    @JvmStatic
    fun check(str: String, regex: String): Boolean {
        var flag: Boolean
        flag = try {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            matcher.matches()
        } catch (e: Exception) {
            false
        }

        return flag
    }

    @JvmStatic
    fun toInt(s: String): Int {
        if (isEmpty(s)) {
            return 0
        }
        var x = 0
        try {
            x = Integer.valueOf(s)
        } catch (e: Exception) {
        }

        return x
    }

    @JvmStatic
    fun toLong(s: String): Long {
        if (isEmpty(s)) {
            return 0
        }
        var x: Long = 0
        try {
            x = java.lang.Long.valueOf(s)
        } catch (e: Exception) {
        }

        return x
    }

    @JvmStatic
    fun toFloat(s: String): Float {
        if (isEmpty(s)) {
            return 0F
        }
        var x = 0f
        try {
            x = java.lang.Float.valueOf(s)!!
        } catch (e: Exception) {
        }

        return x
    }

    @JvmStatic
    fun toDouble(s: String): Double {
        if (isEmpty(s)) {
            return 0.0
        }
        var x = 0.0
        try {
            x = java.lang.Double.valueOf(s)!!
        } catch (ignored: Exception) {
        }

        return x
    }

    /**
     * 友好的显示文件尺寸
     * 比如:0B, 120KB, 1022KB, 1.02MB, 1021MB, 1GB
     *
     * @param context
     * @param size
     * @return
     */
    @JvmStatic
    fun friendlyFileSize(context: Context, size: Long): String {
        val locale = DateUtil.getCurrentLocale(context)
        return when {
            size < 0 -> "0B"
            size < KB -> String.format(locale, "%dB", size)
            size < MB -> String.format(locale, "%.2fKB", size * 1.0 / KB)
            size < GB -> String.format(locale, "%.2fMB", size * 1.0 / MB)
            else -> String.format(locale, "%.2fGB", size * 1.0 / GB)
        }
    }

}
