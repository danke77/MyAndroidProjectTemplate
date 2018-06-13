package com.danke.android.template.core.base.arch

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context

/**
 * @author danke
 * @date 2018/6/23
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            modelClass.getDeclaredConstructor(Context::class.java)
                    .newInstance(context)
}