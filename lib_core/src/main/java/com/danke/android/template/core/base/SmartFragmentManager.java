package com.danke.android.template.core.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


import com.danke.android.template.core.base.fragment.BaseFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danke
 * @date 2018/6/13
 */
public class SmartFragmentManager {

    @IntDef({LAUNCH_STANDARD, LAUNCH_POP, LAUNCH_PULL, LAUNCH_REPLACE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentLaunchMode {
    }

    /**
     * 标准模式，每次重新创建 Fragment
     */
    public static final int LAUNCH_STANDARD = 0;

    /**
     * 单例模式，如果已经显示过会销毁上面所有 Fragment 并调用 {@link BaseFragment#onNewInstance(Bundle)}
     */
    public static final int LAUNCH_POP = 1;

    /**
     * 单例模式，如果已经显示过会显示到最上层并调用 {@link BaseFragment#onNewInstance(Bundle)}
     */
    public static final int LAUNCH_PULL = 2;

    /**
     * 替换模式，删除所有 Fragment，重新创建 Fragment
     */
    public static final int LAUNCH_REPLACE = 3;

    private FragmentManager mFragmentManager;
    private SparseArray<List<SoftReference<BaseFragment>>> mFragmentHeap = new SparseArray<>();

    class FragmentLifeObserver implements LifecycleObserver {

        private SoftReference<BaseFragment> mFragmentRef;

        private FragmentLifeObserver(BaseFragment fragment) {
            mFragmentRef = new SoftReference<>(fragment);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void onCreate() {
            onFragmentCreate(mFragmentRef.get());
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onResume() {
            // onFragmentVisible(mFragmentRef.get());
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {
            onFragmentDestroy(mFragmentRef.get());
            BaseFragment fragment = mFragmentRef.get();
            if (fragment != null) {
                fragment.getLifecycle().removeObserver(this);
            }
        }
    }

    public SmartFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    /**
     * 入栈
     *
     * @param fragment
     */
    private void onFragmentCreate(@Nullable BaseFragment fragment) {
        if (fragment == null) {
            return;
        }

        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(fragment.getId());
        if (list == null) {
            list = new ArrayList<>();
            mFragmentHeap.put(fragment.getId(), list);
        }
        list.add(new SoftReference<>(fragment));
    }

    /**
     * 修改栈中顺序
     *
     * @param fragment 显示的 Fragment
     */
    private void onFragmentVisible(@Nullable BaseFragment fragment) {
        if (fragment == null) {
            return;
        }

        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(fragment.getId());
        if (list == null) {
            return;
        }

        for (SoftReference<BaseFragment> reference : list) {
            if (reference == null) {
                continue;
            }

            if (reference.get() == fragment) {
                list.remove(reference);
                list.add(reference);
                return;
            }
        }
    }

    /**
     * 出栈，并显示栈顶 Fragment
     *
     * @param fragment
     */
    @SuppressLint("ObsoleteSdkInt")
    private void onFragmentDestroy(@Nullable BaseFragment fragment) {
        if (fragment == null) {
            return;
        }

        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(fragment.getId());
        if (list == null) {
            return;
        }

        for (SoftReference<BaseFragment> reference : list) {
            if (reference == null) {
                continue;
            }

            if (reference.get() == fragment) {
                list.remove(reference);
                break;
            }
        }

        // 显示栈顶 Fragment
        BaseFragment topFragment = top(fragment.getId());
        if (topFragment != null) {
            Fragment parentFragment = topFragment.getParentFragment();
            if (parentFragment != null && parentFragment.isRemoving()) {
                return;
            }

            Activity activity = topFragment.getActivity();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && activity.isDestroyed()) {
                return;
            }

            mFragmentManager.beginTransaction()
                    .show(topFragment)
                    .commitAllowingStateLoss();
        }
    }

    public boolean has(@IdRes int containerViewId, @NonNull BaseFragment fragment) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return false;
        }

        for (SoftReference<BaseFragment> reference : list) {
            if (reference != null
                    && reference.get() != null
                    && reference.get() == fragment) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取指定类型的 Fragment
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T find(@IdRes int containerViewId, @NonNull Class<T> clazz) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }

        for (SoftReference<BaseFragment> reference : list) {
            BaseFragment fragment;
            if (reference == null
                    || (fragment = reference.get()) == null
                    || !clazz.isInstance(fragment)) {
                continue;
            }

            return (T) fragment;
        }

        return null;
    }

    /**
     * 获取指定类型的最上层的 Fragment
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T top(@IdRes int containerViewId, @NonNull Class<T> clazz) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }

        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment fragment;
            if (reference == null
                    || (fragment = reference.get()) == null
                    || !clazz.isInstance(fragment)) {
                continue;
            }

            return (T) fragment;
        }

        return null;
    }

    /**
     * 获取最上层的 Fragment
     *
     * @param containerViewId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T top(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }

        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment fragment;
            if (reference == null
                    || (fragment = reference.get()) == null) {
                continue;
            }

            return (T) fragment;
        }

        return null;
    }

    /**
     * finish containerViewId 上面所有的 Fragment
     *
     * @param containerViewId
     */
    public void finish(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list != null) {
            for (SoftReference<BaseFragment> reference : list) {
                BaseFragment fragment;
                if (reference != null
                        && (fragment = reference.get()) != null) {
                    mFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }
        }

        mFragmentHeap.remove(containerViewId);
    }

    /**
     * 获取 Fragment 的 Index
     *
     * @param containerViewId
     * @param t
     * @param <T>
     * @return
     */
    private <T extends BaseFragment> int indexOf(@IdRes int containerViewId, @NonNull T t) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return -1;
        }

        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment fragment;
            if (reference == null
                    || (fragment = reference.get()) == null) {
                continue;
            }

            if (fragment == t) {
                return index;
            }
        }

        return -1;
    }

    /**
     * 当前 containerViewId 下 Fragment 的数量
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    public <T extends BaseFragment> int size(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        return list == null ? 0 : list.size();
    }

    /**
     * 所有 Fragment 的数量
     *
     * @param <T>
     * @return
     */
    public <T extends BaseFragment> int size() {
        int heapSize = mFragmentHeap.size();
        int size = 0;

        for (int i = 0; i < heapSize; i++) {
            List<SoftReference<BaseFragment>> list = mFragmentHeap.get(i);
            if (list == null) {
                continue;
            }

            size += list.size();
        }

        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public <T extends BaseFragment> T showWithAnimation(@NonNull Context context,
                                                        @IdRes int containerViewId,
                                                        @NonNull Class<T> clazz,
                                                        Bundle bundle,
                                                        @FragmentLaunchMode int launchMode,
                                                        @AnimatorRes @AnimRes int enter,
                                                        @AnimatorRes @AnimRes int exit,
                                                        @AnimatorRes @AnimRes int popEnter,
                                                        @AnimatorRes @AnimRes int popExit) {
        return showAction(context, containerViewId, clazz, bundle, launchMode, enter, exit, popEnter, popExit);
    }

    public void showWithAnimation(@NonNull Context context,
                                  @IdRes int containerViewId,
                                  @NonNull BaseFragment fragment,
                                  Bundle bundle,
                                  @FragmentLaunchMode int launchMode,
                                  @AnimatorRes @AnimRes int enter,
                                  @AnimatorRes @AnimRes int exit,
                                  @AnimatorRes @AnimRes int popEnter,
                                  @AnimatorRes @AnimRes int popExit) {
        showAction(context, containerViewId, fragment, bundle, launchMode, enter, exit, popEnter, popExit);
    }

    public <T extends BaseFragment> T show(@NonNull Context context,
                                           @IdRes int containerViewId,
                                           @NonNull Class<T> clazz,
                                           Bundle bundle,
                                           @FragmentLaunchMode int launchMode) {
        return showAction(context, containerViewId, clazz, bundle, launchMode, 0, 0, 0, 0);
    }

    public void show(@NonNull Context context,
                     @IdRes int containerViewId,
                     @NonNull BaseFragment fragment,
                     Bundle bundle,
                     @FragmentLaunchMode int launchMode) {
        showAction(context, containerViewId, fragment, bundle, launchMode, 0, 0, 0, 0);
    }

    /**
     * @param context
     * @param containerViewId
     * @param clazz
     * @param bundle
     * @param launchMode      {@link #LAUNCH_STANDARD}
     *                        {@link #LAUNCH_POP}
     *                        {@link #LAUNCH_PULL}
     *                        {@link #LAUNCH_REPLACE}
     * @param enter
     * @param exit
     * @param popEnter
     * @param popExit
     * @param <T>
     * @return
     */
    @SuppressLint("ResourceType")
    private <T extends BaseFragment> T showAction(@NonNull Context context,
                                                  @IdRes int containerViewId,
                                                  @NonNull Class<T> clazz,
                                                  Bundle bundle,
                                                  @FragmentLaunchMode int launchMode,
                                                  @AnimatorRes @AnimRes int enter,
                                                  @AnimatorRes @AnimRes int exit,
                                                  @AnimatorRes @AnimRes int popEnter,
                                                  @AnimatorRes @AnimRes int popExit) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (enter > 0 || exit > 0 || popEnter > 0 || popExit > 0) {
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
        }

        BaseFragment prev = top(containerViewId);
        T t;
        switch (launchMode) {
            case LAUNCH_REPLACE:
                t = (T) Fragment.instantiate(context, clazz.getName(), bundle);
                t.getLifecycle().addObserver(new FragmentLifeObserver(t));
                ft.replace(containerViewId, t);
                break;
            case LAUNCH_PULL:
            case LAUNCH_POP:
                t = find(containerViewId, clazz);
                if (t != null) {
                    int index;
                    if (launchMode == LAUNCH_POP && (index = indexOf(containerViewId, t)) != -1) {
                        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
                        for (index++; index < list.size(); index++) {
                            SoftReference<BaseFragment> reference = list.get(index);
                            BaseFragment fragment;
                            if (reference == null || (fragment = reference.get()) == null) {
                                continue;
                            }
                            ft.remove(fragment);
                        }
                    }
                    t.onNewInstance(bundle);
                    onFragmentVisible(t);
                    break;
                }
            case LAUNCH_STANDARD:
            default:
                t = (T) Fragment.instantiate(context, clazz.getName(), bundle);
                t.getLifecycle().addObserver(new FragmentLifeObserver(t));
                ft.add(containerViewId, t);
        }

        if (prev != null && prev.isAdded() && prev != t) {
            ft.hide(prev);
        }
        ft.show(t);
        ft.commitAllowingStateLoss();

        return t;
    }

    /**
     * @param context
     * @param containerViewId
     * @param fragment
     * @param bundle
     * @param launchMode      {@link #LAUNCH_STANDARD}
     *                        {@link #LAUNCH_POP}
     *                        {@link #LAUNCH_PULL}
     *                        {@link #LAUNCH_REPLACE}
     * @param enter
     * @param exit
     * @param popEnter
     * @param popExit
     */
    @SuppressLint("ResourceType")
    private void showAction(@NonNull Context context,
                            @IdRes int containerViewId,
                            @NonNull BaseFragment fragment,
                            Bundle bundle,
                            @FragmentLaunchMode int launchMode,
                            @AnimatorRes @AnimRes int enter,
                            @AnimatorRes @AnimRes int exit,
                            @AnimatorRes @AnimRes int popEnter,
                            @AnimatorRes @AnimRes int popExit) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (enter > 0 || exit > 0 || popEnter > 0 || popExit > 0) {
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
        }

        BaseFragment prev = top(containerViewId);
        switch (launchMode) {
            case LAUNCH_REPLACE:
                fragment.getLifecycle().addObserver(new FragmentLifeObserver(fragment));
                ft.replace(containerViewId, fragment);
                break;
            case LAUNCH_PULL:
            case LAUNCH_POP:
                if (has(containerViewId, fragment)) {
                    int index;
                    if (launchMode == LAUNCH_POP && (index = indexOf(containerViewId, fragment)) != -1) {
                        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
                        for (index++; index < list.size(); index++) {
                            SoftReference<BaseFragment> reference = list.get(index);
                            BaseFragment aboveFragment;
                            if (reference == null || (aboveFragment = reference.get()) == null) {
                                continue;
                            }
                            ft.remove(aboveFragment);
                        }
                    }
                    fragment.onNewInstance(bundle);
                    onFragmentVisible(fragment);
                    break;
                }
            case LAUNCH_STANDARD:
            default:
                fragment.getLifecycle().addObserver(new FragmentLifeObserver(fragment));
                ft.add(containerViewId, fragment);
        }

        if (prev != null && prev.isAdded() && prev != fragment) {
            ft.hide(prev);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }
}
