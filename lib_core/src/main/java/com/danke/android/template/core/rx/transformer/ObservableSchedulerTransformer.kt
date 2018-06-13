package com.danke.android.template.core.rx.transformer

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author danke
 * @date 2018/6/13
 */
class ObservableSchedulerTransformer<T> : ObservableTransformer<T, T> {

    companion object {
        fun <T> create(): ObservableSchedulerTransformer<T> {
            return ObservableSchedulerTransformer()
        }
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
            upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}