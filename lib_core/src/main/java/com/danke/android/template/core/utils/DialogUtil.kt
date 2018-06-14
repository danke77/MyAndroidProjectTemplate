package com.danke.android.template.core.utils

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.danke.android.template.core.R

/**
 * @author danke
 * @date 2018/6/13
 */
object DialogUtil {

    const val DIALOG_BUTTON_SHOW_NUMBER_ONE = 1
    const val DIALOG_BUTTON_SHOW_NUMBER_TWO = 2
    const val DIALOG_BUTTON_SHOW_NUMBER_THREE = 3

    @IntDef(DIALOG_BUTTON_SHOW_NUMBER_ONE, DIALOG_BUTTON_SHOW_NUMBER_TWO, DIALOG_BUTTON_SHOW_NUMBER_THREE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DialogButtonShowNumber

    /**
     * @param context
     * @param icon                  default 0
     * @param title
     * @param message
     * @param positiveText
     * @param negativeText          default ""
     * @param positiveClickListener
     * @param negativeClickListener
     * @param cancelable            default true
     */
    @JvmStatic
    @JvmOverloads
    fun showDialog(context: Context,
                   @DrawableRes icon: Int = 0,
                   title: CharSequence,
                   message: CharSequence,
                   positiveText: CharSequence,
                   negativeText: CharSequence = "",
                   positiveClickListener: DialogInterface.OnClickListener,
                   negativeClickListener: DialogInterface.OnClickListener? = null,
                   cancelable: Boolean = true) {

        val builder = AlertDialog.Builder(context, 0)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveClickListener)
                .setCancelable(cancelable)

        if (!TextUtils.isEmpty(negativeText)
                && negativeClickListener != null) {
            builder.setNegativeButton(negativeText, negativeClickListener)
        }

        val dialog = builder.create()

        // 都不为空，“确定”选项，改变颜色，以便突出
        if (!TextUtils.isEmpty(positiveText) && !TextUtils.isEmpty(negativeText)) {
            setDefaultDialogStyle(context, dialog, DIALOG_BUTTON_SHOW_NUMBER_TWO)
        } else if (!TextUtils.isEmpty(positiveText)) {
            setDefaultDialogStyle(context, dialog, DIALOG_BUTTON_SHOW_NUMBER_ONE)
        }

        dialog.show()
    }

    /**
     * @param context
     * @param icon                  default 0
     * @param title
     * @param view
     * @param positiveText
     * @param negativeText          default ""
     * @param positiveClickListener
     * @param negativeClickListener
     * @param cancelable            default true
     */
    @JvmStatic
    @JvmOverloads
    fun showDialog(context: Context,
                   @DrawableRes icon: Int = 0,
                   title: CharSequence,
                   view: View,
                   positiveText: CharSequence,
                   negativeText: CharSequence = "",
                   positiveClickListener: DialogInterface.OnClickListener,
                   negativeClickListener: DialogInterface.OnClickListener? = null,
                   cancelable: Boolean = true) {

        val builder = AlertDialog.Builder(context, 0)
                .setIcon(icon)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveText, positiveClickListener)

        if (!TextUtils.isEmpty(negativeText)
                && negativeClickListener != null) {
            builder.setNegativeButton(negativeText, negativeClickListener)
        }

        val dialog = builder.create()

        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)

        // 都不为空，“确定”选项，改变颜色，以便突出
        if (!TextUtils.isEmpty(positiveText) && !TextUtils.isEmpty(negativeText)) {
            setDefaultDialogStyle(context, dialog, DIALOG_BUTTON_SHOW_NUMBER_TWO)
        } else if (!TextUtils.isEmpty(positiveText)) {
            setDefaultDialogStyle(context, dialog, DIALOG_BUTTON_SHOW_NUMBER_ONE)
        }

        dialog.show()
    }

    /**
     * 修改按钮颜色
     */
    private fun setDefaultDialogStyle(context: Context,
                                      alertDialog: AlertDialog,
                                      @DialogButtonShowNumber number: Int) {

        when (number) {
            DIALOG_BUTTON_SHOW_NUMBER_TWO -> {
                setHighLightPositiveStyle(context, alertDialog)
            }

            DIALOG_BUTTON_SHOW_NUMBER_THREE -> {
                setHighLightPositiveStyle(context, alertDialog)
            }

            DIALOG_BUTTON_SHOW_NUMBER_ONE -> {
                setHighLightPositiveStyle(context, alertDialog)
            }

            else -> {
            }
        }
    }

    /**
     * 只设置positive文字颜色，其他默认使用系统
     *
     * @param context
     * @param alertDialog
     */
    private fun setHighLightPositiveStyle(context: Context,
                                          alertDialog: AlertDialog) {
        setDialogStyle(context, alertDialog,
                R.color.dialog_title,
                R.color.dialog_content,
                R.color.dialog_highlight_positive,
                R.color.dialog_neutral,
                R.color.dialog_negative)
    }

    private fun setDialogStyle(context: Context,
                               alertDialog: AlertDialog,
                               @ColorRes titleTextColor: Int,
                               @ColorRes contentTextColor: Int,
                               @ColorRes positiveTextColor: Int,
                               @ColorRes neutralTextColor: Int,
                               @ColorRes negativeTextColor: Int) {

        val titleId = alertDialog.context.resources
                .getIdentifier("android:id/alertTitle", null, null)
        if (titleId != 0) {
            val title = alertDialog.findViewById<TextView>(titleId)
            if (title != null) {
                title.setTextColor(ContextCompat.getColor(context, titleTextColor))
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimensionPixelSize(R.dimen.dialogTitle).toFloat())
            }
        }

        val messageId = alertDialog.context.resources
                .getIdentifier("android:id/message", null, null)
        if (messageId != 0) {
            val message = alertDialog.findViewById<TextView>(messageId)
            if (message != null) {
                message.setTextColor(ContextCompat.getColor(context, contentTextColor))
                message.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimensionPixelSize(R.dimen.dialogMessage).toFloat())
            }
        }


        alertDialog.setOnShowListener {
            val positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            if (positiveBtn != null && positiveTextColor != 0) {
                positiveBtn.setTextColor(ContextCompat.getColor(context, positiveTextColor))
                positiveBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimensionPixelSize(R.dimen.dialogButton).toFloat())
            }

            val neutralBtn = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
            if (neutralBtn != null && neutralTextColor != 0) {
                neutralBtn.setTextColor(ContextCompat.getColor(context, neutralTextColor))
                neutralBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimensionPixelSize(R.dimen.dialogButton).toFloat())
            }

            val negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            if (negativeBtn != null && negativeTextColor != 0) {
                negativeBtn.setTextColor(ContextCompat.getColor(context, negativeTextColor))
                negativeBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimensionPixelSize(R.dimen.dialogButton).toFloat())
            }
        }
    }

}
