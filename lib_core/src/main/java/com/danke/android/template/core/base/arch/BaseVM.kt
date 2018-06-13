package com.danke.android.template.core.base.arch

import android.arch.lifecycle.ViewModel

import io.reactivex.disposables.CompositeDisposable

/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseVM : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
