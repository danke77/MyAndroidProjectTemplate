package com.danke.android.template.core.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * 带有菜单栏的 Fragment.
 * 注意：需要在该fragment的布局文件中定义 {@link Toolbar} 视图, 并通过 {@link #getToolbarLayoutId} 返回布局 ID.
 * 和在 {@link Activity} 中是一样正常使用，所以需要子类实现3个方法:
 * <pre>
 *
 *      public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
 *          super.onCreateOptionsMenu(menu, inflater);
 *          inflater.inflate(R.menu.menu_edit, menu);
 *      }
 *
 *      public boolean onOptionsItemSelected(MenuItem item) {
 *          switch (item.getItemId()) {
 *              case R.id.action:
 *                  //Do something...
 *                  return true;
 *              default:
 *                  return false;
 *          }
 *      }
 *
 *      protected int getToolbarLayoutId() {
 *          return R.id.toolbar;
 *      }
 * </pre>
 *
 * @author danke
 * @date 2018/6/13
 */
public abstract class BaseMenuFragment extends BaseFragment {

    private Toolbar mToolbar;
    private TextView mTitleTv;
    private Menu mMenu;
    private MenuInflater mMenuInflater;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable default menu setting
        super.setHasOptionsMenu(false);
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbarMenu(view);
    }

    private void setupToolbarMenu(@Nullable View view) {
        final int id = getToolbarLayoutId();
        if (id == View.NO_ID || view == null) {
            return;
        }

        View childView = view.findViewById(id);
        if (childView instanceof Toolbar) {
            mToolbar = (Toolbar) childView;
            mTitleTv = mToolbar.findViewById(getTitleLayoutId());
            mMenu = mToolbar.getMenu();
            mToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

            onCreateOptionsMenu(mToolbar.getMenu(), getMenuInflater(mToolbar));
            onPrepareOptionsMenu(mToolbar.getMenu());
        }
    }

    public void invalidateOptionsMenu() {
        if (getToolbar() != null) {
            onPrepareOptionsMenu(getToolbar().getMenu());
        } else if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    public void setTitle(@StringRes int title) {
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        } else if (mToolbar != null) {
            mToolbar.setTitle(title);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(title);
            }
        }
    }

    public void setTitle(String title) {
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        } else if (mToolbar != null) {
            mToolbar.setTitle(title);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(title);
            }
        }
    }

    /**
     * @return Null if the layout of fragment haven't {@link Toolbar}, please check it out
     */
    @Nullable
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * @return Null if the layout of fragment haven't {@link Toolbar}, please check it out
     */
    @Nullable
    public Menu getMenu() {
        return mMenu;
    }

    /**
     * Get Toolbar's MenuInflater by reflect
     */
    @Nullable
    private MenuInflater getMenuInflater(Toolbar obj) {
        if (mMenuInflater == null) {
            try {
                Method method = Toolbar.class.getDeclaredMethod("getMenuInflater");
                method.setAccessible(true);
                mMenuInflater = (MenuInflater) method.invoke(obj);
            } catch (Throwable ignore) {
            }
        }

        return mMenuInflater;
    }

    @IdRes
    protected abstract int getToolbarLayoutId();

    @IdRes
    protected abstract int getTitleLayoutId();

}
