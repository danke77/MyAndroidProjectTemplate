package com.danke.android.template.core.base.activity

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup

import com.danke.android.template.core.base.ProgressDialogHandler
import com.danke.android.template.core.base.SmartFragmentManager

/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseActivity : AppCompatActivity() {

    private val progressHandler: ProgressDialogHandler by lazy { ProgressDialogHandler(this) }

    private var mSmartFragmentManager: SmartFragmentManager? = null

    val smartFragmentManager: SmartFragmentManager
        get() {
            if (mSmartFragmentManager == null) {
                mSmartFragmentManager = SmartFragmentManager(supportFragmentManager)
            }
            return mSmartFragmentManager!!
        }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onContentViewSet()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        onContentViewSet()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        onContentViewSet()
    }

    override fun onDestroy() {
        super.onDestroy()

        progressHandler.dismiss()
        progressHandler.removeCallbacksAndMessages(null)
    }

    @CallSuper
    protected fun onContentViewSet() {
    }

    protected fun showProgress() {
        progressHandler.show()
    }

    protected fun dismissProgress() {
        progressHandler.dismiss()
    }
}
