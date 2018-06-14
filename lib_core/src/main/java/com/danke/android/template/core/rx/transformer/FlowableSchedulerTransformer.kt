package com.danke.android.template.core.rx.transformer

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

/**
 * @author danke
 * @date 2018/6/13
 */
class FlowableSchedulerTransformer<T> : FlowableTransformer<T, T> {

    companion object {
        @JvmStatic
        fun <T> create(): FlowableSchedulerTransformer<T> {
            return FlowableSchedulerTransformer()
        }
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> =
            upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}