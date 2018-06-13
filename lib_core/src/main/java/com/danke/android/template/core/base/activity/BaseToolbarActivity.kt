package com.danke.android.template.core.base.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.DrawableRes
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import com.danke.android.template.core.R
import kotlinx.android.synthetic.main.activity_toolbar.*

/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseToolbarActivity : BaseActivity() {

    protected val toolbar: Toolbar by lazy { toolBar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        init()
    }

    @CallSuper
    protected open fun init() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getNavigationIcon()
        toolbar.setNavigationOnClickListener { finish() }
    }

    protected open fun getNavigationIcon(): Drawable? =
            ContextCompat.getDrawable(this, R.drawable.ic_back_dark)

    protected open fun setFab(@DrawableRes resId: Int,
                              @Nullable onClickListener: View.OnClickListener) {
        fab.visibility = View.VISIBLE
        fab.setImageResource(resId)
        fab.setOnClickListener(onClickListener)
    }
}