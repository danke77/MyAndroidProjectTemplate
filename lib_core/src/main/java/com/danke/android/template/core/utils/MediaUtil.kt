package com.danke.android.template.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

import java.io.FileNotFoundException

/**
 * @author danke
 * @date 2018/6/13
 */
object MediaUtil {

    @JvmStatic
    fun insertImageToGallery(context: Context, path: String, fileName: String) {
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver, path, fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // 通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$path")))
    }
}
