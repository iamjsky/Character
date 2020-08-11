package com.character.microblogapp.ui.page;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.ButterKnife;

public class BaseDialog extends Dialog implements DialogInterface.OnShowListener {

    private Rect boundRect = new Rect();

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    protected BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        if (getWindow() != null) {
            getWindow().getDecorView().getWindowVisibleDisplayFrame(this.boundRect);
        }
        setOnShowListener(this);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (getWindow() != null) {
            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getWindow().getDecorView().getHeight() /*boundRect.height()*/);
        }
    }
}
