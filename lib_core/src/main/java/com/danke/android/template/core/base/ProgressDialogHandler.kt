package com.danke.android.template.core.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import com.danke.android.template.core.R

/**
 * @author danke
 * @date 2018/6/13
 */
class ProgressDialogHandler(private val mContext: Context) : Handler() {

    companion object {
        private const val SHOW_PROGRESS_DIALOG = 1
        private const val DISMISS_PROGRESS_DIALOG = 2
    }

    private var mProgressDialog: ProgressDialog? = null

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> showInner()
            DISMISS_PROGRESS_DIALOG -> dismissInner()
        }
    }

    fun setCanceledOnTouchOutside(cancelable: Boolean) {
        if (mProgressDialog != null) {
            mProgressDialog!!.setCanceledOnTouchOutside(cancelable)
        }
    }

    fun show() {
        sendEmptyMessage(SHOW_PROGRESS_DIALOG)
    }

    fun dismiss() {
        sendEmptyMessage(DISMISS_PROGRESS_DIALOG)
    }

    private fun showInner() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(mContext, R.style.LoadingDialog)
            mProgressDialog!!.isIndeterminate = true
            mProgressDialog!!.setCanceledOnTouchOutside(false)
            mProgressDialog!!.setCancelable(true)
        }

        if (!mProgressDialog!!.isShowing) {
            try {
                mProgressDialog!!.show()
            } catch (e: Exception) {
                return
            }

            mProgressDialog!!.setContentView(R.layout.progressbar)
        }
    }

    private fun dismissInner() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
        mProgressDialog = null
    }

}
