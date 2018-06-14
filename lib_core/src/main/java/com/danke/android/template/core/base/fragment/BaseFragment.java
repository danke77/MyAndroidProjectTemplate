package com.danke.android.template.core.base.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danke.android.template.core.BuildConfig;
import com.danke.android.template.core.base.ProgressDialogHandler;
import com.danke.android.template.core.base.SmartFragmentManager;
import com.danke.android.template.core.base.fragment.share.AbsSharedData;
import com.danke.android.template.core.base.fragment.share.ISharedData;
import com.danke.android.template.core.base.fragment.share.SharedVM;
import com.danke.android.template.core.utils.KeyboardUtil;
import com.danke.android.template.core.base.activity.BaseActivity;


/**
 * @author danke
 * @date 2018/6/13
 */
public abstract class BaseFragment extends Fragment implements ISharedData<Bundle> {

    protected final String TAG = getClass().getSimpleName();

    private SmartFragmentManager mSmartFragmentManager;
    private ProgressDialogHandler mProgressHandler;
    private ISharedData<Bundle> mSharedData = new AbsSharedData() {

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return BaseFragment.this.getLifecycle();
        }

        @Override
        public MutableLiveData<Bundle> getSharedData() {
            return ViewModelProviders.of(BaseFragment.this).get(SharedVM.class).getSharedData();
        }

        @Override
        public void postData(Bundle bundle) {
            getSharedData().postValue(bundle);
        }

        @Override
        public void setData(Bundle bundle) {
            getSharedData().setValue(bundle);
        }
    };

    @Override
    public MutableLiveData<Bundle> getSharedData() {
        return mSharedData.getSharedData();
    }

    @Override
    public void observeResult(@NonNull LifecycleOwner lifecycleOwner,
                              @NonNull Observer<Bundle> observer) {
        mSharedData.observeResult(lifecycleOwner, observer);
    }

    @Override
    public void postData(Bundle bundle) {
        if (getActivity() != null) {
            mSharedData.postData(bundle);
        }
    }

    @Override
    public void setData(Bundle bundle) {
        if (getActivity() != null) {
            mSharedData.setData(bundle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressHandler = new ProgressDialogHandler(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mProgressHandler.removeCallbacksAndMessages(null);
        mProgressHandler.dismiss();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (BuildConfig.DEBUG) {
            View view = getView();
            if (view == null) {
                return;
            }
            view.setVisibility(hidden ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public void onNewInstance(Bundle bundle) {

    }

    public void onPostBundle(@NonNull Bundle bundle) {

    }

    /**
     * handler onBackPressed
     * <p>
     * only work between onStart and onStop
     *
     * @return true consume back press action, false not consume.
     */
    public boolean onBackPressed() {
        return false;
    }

    protected SmartFragmentManager getSmartFragmentManager() {
        if (mSmartFragmentManager == null) {
            mSmartFragmentManager = new SmartFragmentManager(getChildFragmentManager());
        }
        return mSmartFragmentManager;
    }

    protected void showProgress() {
        mProgressHandler.show();
    }

    protected void showProgress(boolean cancelable) {
        mProgressHandler.setCanceledOnTouchOutside(cancelable);
        mProgressHandler.show();
    }


    protected void dismissProgress() {
        mProgressHandler.dismiss();
    }


    @SuppressLint("ObsoleteSdkInt")
    @CallSuper
    public void finish() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.isRemoving()) {
            return;
        }

        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && activity.isDestroyed()) {
            return;
        }

        if (KeyboardUtil.isActive(activity)) {
            KeyboardUtil.hide(activity.getWindow());
        }

        if (parentFragment == null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        } else {
            parentFragment.getChildFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }

        if (activity instanceof BaseActivity
                && ((BaseActivity) activity).getSmartFragmentManager().isEmpty()) {
            activity.finish();
        }
    }
}
