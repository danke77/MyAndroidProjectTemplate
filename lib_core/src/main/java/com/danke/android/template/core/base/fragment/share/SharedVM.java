package com.danke.android.template.core.base.fragment.share;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

/**
 * @author danke
 * @date 2018/6/13
 */
public class SharedVM extends ViewModel {

    private MutableLiveData<Bundle> sharedData = new MutableLiveData<>();

    public MutableLiveData<Bundle> getSharedData() {
        return sharedData;
    }

}