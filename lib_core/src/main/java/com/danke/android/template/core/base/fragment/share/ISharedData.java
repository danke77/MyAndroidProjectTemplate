package com.danke.android.template.core.base.fragment.share;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

/**
 * @author danke
 * @date 2018/6/13
 */
public interface ISharedData<T> extends LifecycleOwner {

    /**
     * 发送数据
     *
     * @param t
     */
    void postData(T t);

    void setData(T t);

    /**
     * 获取共享数据源
     *
     * @return
     */
    MutableLiveData<T> getSharedData();

    /**
     * 异步注册数据源变化（用于数据源还没有初始化完成场景）
     *
     * @param lifecycleOwner
     * @param observer
     */
    void observeResult(@NonNull final LifecycleOwner lifecycleOwner,
                       @NonNull final Observer<T> observer);
}
