package com.character.microblogapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

public class KeyboardUtil implements OnGlobalLayoutListener {

    private View currentView;
    private float density = 1;

    private static HashMap<KeyboardToggleListener, KeyboardUtil> listenerMap = new HashMap();
    private KeyboardToggleListener toggleListener;

    private KeyboardUtil(Activity activity, KeyboardToggleListener KeyboardToggleListener) {
        this.toggleListener = KeyboardToggleListener;
        this.currentView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        this.currentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.density = activity.getResources().getDisplayMetrics().density;
    }

    public static void showKeyboard(View view) {
        try {
            ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getApplicationWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View view) {
        try {
            ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onGlobalLayout() {
        Rect rect = new Rect();
        this.currentView.getWindowVisibleDisplayFrame(rect);
        float height = ((float) (this.currentView.getRootView().getHeight() - (rect.bottom - rect.top))) / this.density;
        if (this.toggleListener != null) {
            this.toggleListener.onToggleSoftKeyboard(height > 200.0f, rect);
        }
    }

    public static void addKeyboardToggleListener(Activity activity, KeyboardToggleListener KeyboardToggleListener) {
        removeKeyboardToggleListener(KeyboardToggleListener);
        listenerMap.put(KeyboardToggleListener, new KeyboardUtil(activity, KeyboardToggleListener));
    }

    private static void removeKeyboardToggleListener(KeyboardToggleListener KeyboardToggleListener) {
        if (listenerMap.containsKey(KeyboardToggleListener)) {
            ((KeyboardUtil) listenerMap.get(KeyboardToggleListener)).m8241a();
            listenerMap.remove(KeyboardToggleListener);
        }
    }

    public static void removeAllKeyboardToggleListeners() {
        for (KeyboardToggleListener KeyboardToggleListener : listenerMap.keySet()) {
            ((KeyboardUtil) listenerMap.get(KeyboardToggleListener)).m8241a();
        }
        listenerMap.clear();
    }

    private void m8241a() {
        this.toggleListener = null;
        this.currentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public interface KeyboardToggleListener {
        void onToggleSoftKeyboard(boolean z, Rect rect);
    }

}
