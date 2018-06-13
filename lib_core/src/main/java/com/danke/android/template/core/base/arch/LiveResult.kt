package com.danke.android.template.core.base.arch

/**
 * @author danke
 * @date 2018/6/13
 */
class LiveResult<out T>(val data: T?, val error: Throwable?) {

    companion object {

        fun <T> success(data: T): LiveResult<T> {
            return LiveResult(data, null)
        }

        fun <T> error(error: Throwable): LiveResult<T> {
            return LiveResult<T>(null, error)
        }
    }
}