package com.danke.android.template.core.base.fragment.share;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author danke
 * @date 2018/6/13
 */
public abstract class AbsSharedData implements ISharedData<Bundle> {

    @Override
    public void observeResult(@NonNull final LifecycleOwner lifecycleOwner,
                              @NonNull final Observer<Bundle> observer) {
        switch (getLifecycle().getCurrentState()) {
            case INITIALIZED:
                getLifecycle().addObserver(new LifecycleObserver() {

                    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
                    public void onChanged() {
                        removeObservers(lifecycleOwner);
                        getSharedData().observe(lifecycleOwner, observer);
                        getLifecycle().removeObserver(this);
                    }
                });
                break;
            case DESTROYED:
                // ignore
                // removeObservers(lifecycleOwner);
                break;
            default:
                removeObservers(lifecycleOwner);
                getSharedData().observe(lifecycleOwner, observer);
                break;
        }
    }

    /**
     * 共享数据源对于指定的 LifecycleOwner 只允许有一个 Observer
     *
     * @param lifecycleOwner
     */
    private void removeObservers(@NonNull LifecycleOwner lifecycleOwner) {
        getSharedData().removeObservers(lifecycleOwner);
    }
}
