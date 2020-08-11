package com.character.microblogapp.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

public class BaseEditText extends AppCompatEditText {

    private Handler handler = null;

    private boolean isHiddenKeyboard = true;

    public BaseEditText(Context context) {
        super(context);
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (handler != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    handler.sendEmptyMessage(InputMethodManager.RESULT_HIDDEN);
                }
            }

            if (isHiddenKeyboard == false) {
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    public void setHiddenKeyboardOnBackPressed(boolean isHiddenKeyboard) {
        this.isHiddenKeyboard = isHiddenKeyboard;
    }

    public void setOnBackPressedHandler(Handler handler) {
        this.handler = handler;
    }
}
