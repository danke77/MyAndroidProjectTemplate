package com.danke.android.template.core.base.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.DrawableRes
import android.support.annotation.Nullable
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import com.danke.android.template.core.R
import kotlinx.android.synthetic.main.activity_collapsing_toolbar.*

/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseCollapsingToolbarActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar)
        init()
    }

    @CallSuper
    protected open fun init() {
        setSupportActionBar(toolBar)
        toolBar.navigationIcon = getNavigationIcon()
        toolBar.setNavigationOnClickListener { finish() }

        collapsingToolbar.expandedTitleGravity = Gravity.START
        setToolbarGravity(Gravity.START)
    }

    protected open fun getNavigationIcon(): Drawable? =
            ContextCompat.getDrawable(this, R.drawable.ic_back_dark)

    protected open fun setToolbarGravity(gravity: Int) {
        collapsingToolbar.collapsedTitleGravity = gravity
    }

    override fun setTitle(@StringRes title: Int) {
        collapsingToolbar.title = getString(title)
    }

    override fun setTitle(title: CharSequence?) {
        collapsingToolbar.title = title
    }

    protected open fun setFab(@DrawableRes resId: Int,
                              @Nullable onClickListener: View.OnClickListener) {
        fab.visibility = View.VISIBLE
        fab.setImageResource(resId)
        fab.setOnClickListener(onClickListener)
    }

}