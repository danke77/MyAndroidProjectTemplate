package com.danke.android.template.core.utils

import android.content.Context
import android.os.Build
import android.support.annotation.StringDef
import com.danke.android.template.core.base.app.BaseApp

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * @author danke
 * @date 2018/6/13
 */
object DateUtil {

    private const val SECOND = 1000
    private const val MIN = SECOND * 60
    private const val HOUR = MIN * 60
    private const val DAY = HOUR * 24

    const val DATE_FORMAT = "yyyy/MM/dd HH:mm:ss"

    @StringDef(DATE_FORMAT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DateFormat

    /**
     * 当前时区
     *
     * @return
     */
    fun getCurrentLocale(): Locale {
        val context = BaseApp.get().applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }

    /**
     * 当前时区
     *
     * @param context
     * @return
     */
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }

    /**
     * 当前日期时间
     *
     * @param context
     * @return
     */
    fun getCurrentDateTime(context: Context): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, getCurrentLocale(context))
        return dateFormat.format(Date(System.currentTimeMillis()))
    }

    /**
     * 当前日期
     *
     * @param context
     * @return
     */
    fun getCurrentDate(context: Context): Date {
        val cal = Calendar.getInstance(getCurrentLocale(context))
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    /**
     * 指定日期时间
     *
     * @param locale
     * @param milliseconds
     * @param dataFormat
     * @return
     */
    fun getDateTime(milliseconds: Long,
                    @DateFormat dataFormat: String = DATE_FORMAT,
                    locale: Locale = getCurrentLocale()): String {
        val dateFormat = SimpleDateFormat(dataFormat, locale)
        return dateFormat.format(Date(milliseconds))
    }

    /**
     * 格式转换
     *
     * @param dateString
     * @param fromFormat
     * @param toFormat
     * @param locale
     * @return
     */
    fun formatTransform(dateString: String,
                        @DateFormat fromFormat: String,
                        @DateFormat toFormat: String,
                        locale: Locale = getCurrentLocale()): String {
        val fromSdf = SimpleDateFormat(fromFormat, locale)
        val toSdf = SimpleDateFormat(toFormat, locale)

        val date: Date
        try {
            date = fromSdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return dateString
        }

        return toSdf.format(date)
    }

    /**
     * 毫秒数
     *
     * @param dateString
     * @param dateFormat
     * @param locale
     * @return
     */
    fun getTimeMilliseconds(dateString: String,
                            @DateFormat dateFormat: String = DATE_FORMAT,
                            locale: Locale = getCurrentLocale()): Long {
        val fromSdf = SimpleDateFormat(dateFormat, locale)
        val date: Date
        try {
            date = fromSdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0
        }

        return date.time
    }

    /**
     * 指定 Date
     *
     * @param dateString
     * @param dateFormat
     * @param locale
     * @return
     */
    fun getDate(dateString: String,
                @DateFormat dateFormat: String = DATE_FORMAT,
                locale: Locale = getCurrentLocale()): Date? {
        val sdfFrom = SimpleDateFormat(dateFormat, locale)
        var date: Date? = null
        try {
            date = sdfFrom.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    /**
     * 字符串日期与今天的相隔天数
     *
     * @param date
     * @param dataFormat
     * @param locale
     * @return
     */
    fun getDaysToNow(date: String,
                     @DateFormat dataFormat: String = DATE_FORMAT,
                     locale: Locale = getCurrentLocale()): Int {
        val now = Date()
        val to: Date
        try {
            to = SimpleDateFormat(dataFormat, locale).parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

        return getDaysBetweenTwoDates(to, now, locale)
    }

    /**
     * 两个日期之间的相隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    fun getDaysBetweenTwoDates(date1: Date,
                               date2: Date,
                               locale: Locale = getCurrentLocale()): Int {
        val calendar = Calendar.getInstance(locale)
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.time = date2
        val day2 = calendar.get(Calendar.DAY_OF_YEAR)

        return Math.abs(day2 - day1)
    }

}