package com.danke.android.template.core.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @author danke
 * @date 2018/6/13
 */
object FileUtil {

    @Throws(IOException::class)
    fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int = `in`.read(buffer)
        while (read != -1) {
            out.write(buffer, 0, read)
            read = `in`.read(buffer)
        }
    }
}