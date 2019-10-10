package com.xiaoqianchang.bottomdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import static android.R.attr.id;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/12/29.
 *
 * @version 1.0
 */

public abstract class BaseBottomDialog extends DialogFragment {

    private static final String TAG = "base_bottom_dialog";

    private static final float DEFAULT_DIM = 0.2f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        View view = inflater.inflate(getLayoutRes(), container);
        bindView(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract int getLayoutRes();

    protected abstract void bindView(View view);

    private boolean getCancelOutside() {
        return false;
    }
}
