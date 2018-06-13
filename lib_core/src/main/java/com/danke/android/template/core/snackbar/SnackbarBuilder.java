package com.danke.android.template.core.snackbar;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @author danke
 * @date 2018/6/13
 */
public class SnackbarBuilder {

    private static class SnackbarParams {

        final View view;

        CharSequence message;

        @BaseTransientBottomBar.Duration
        int duration;

        SnackbarParams(@NonNull View view) {
            this.view = view;
            message = "";
            duration = Snackbar.LENGTH_LONG;
        }
    }

    private final SnackbarParams P;

    public SnackbarBuilder(@NonNull Activity activity) {
        P = new SnackbarParams(activity.findViewById(android.R.id.content));
    }

    public SnackbarBuilder(@NonNull View view) {
        P = new SnackbarParams(view);
    }

    public SnackbarBuilder setMessage(@NonNull CharSequence text) {
        P.message = text;
        return this;
    }

    public SnackbarBuilder setMessage(@StringRes int resId) {
        P.message = P.view.getResources().getText(resId);
        return this;
    }

    public SnackbarBuilder setDuration(@BaseTransientBottomBar.Duration int duration) {
        P.duration = duration;
        return this;
    }

    public Snackbar build() {
        return Snackbar.make(P.view, P.message, P.duration);
    }

    public Snackbar show() {
        Snackbar snackbar = build();
        snackbar.show();
        return snackbar;
    }
}
