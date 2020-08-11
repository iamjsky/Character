package com.character.microblogapp.util;

import android.content.Context;
import android.widget.Toast;

import com.character.microblogapp.R;

public class Toaster {

    public static void showShort(Context context, String text) {
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShort(Context context, int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }

    }

    public static void showLong(Context context, String text) {
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLong(Context context, int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        }
    }

    public static void showNIToast(Context context) {
        showShort(context, R.string.service_not_ready);
    }

}
