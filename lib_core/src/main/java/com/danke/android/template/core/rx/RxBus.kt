package com.danke.android.template.core.rx

import android.content.Intent
import com.danke.android.template.core.rx.transformer.ObservableSchedulerTransformer
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * @author danke
 * @date 2018/6/13
 */
enum class RxBus {

    INSTANCE;

    // toSerialized method made bus thread safe
    private val mBus: Relay<Any> = PublishRelay.create<Any>().toSerialized()

    init {
    }

    fun hasObservers(): Boolean = mBus.hasObservers()

    fun post(intent: Intent) {
        if (hasObservers()) {
            mBus.accept(intent)
        }
    }

    fun post(`object`: Any) {
        if (hasObservers()) {
            mBus.accept(`object`)
        }
    }

    fun <T> toObservable(tClass: Class<T>): Observable<T> = mBus.ofType(tClass)

    fun toObservable(): Observable<Any> = mBus

    fun broadcast(): Observable<Intent> =
            toObservable(Intent::class.java)
                    .compose(ObservableSchedulerTransformer())

    fun <T> broadcast(tClass: Class<T>): Observable<T> =
            toObservable(tClass)
                    .compose(ObservableSchedulerTransformer())

    fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }
}
