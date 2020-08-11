package com.character.microblogapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

import butterknife.ButterKnife;

public class BaseDialog extends Dialog {
    public Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setContentView(layoutResID);
        ButterKnife.bind(this);

    }
}
