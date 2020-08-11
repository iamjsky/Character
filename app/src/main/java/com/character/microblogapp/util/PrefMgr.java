package com.character.microblogapp.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class PrefMgr {

    public static final String APP_PREFS = "character_prefs";

    public static final String USER_LOGIN_TYPE = "login_type";
    public static final String USER_UID = "uid";
    public static final String USER_EMAIL = "email";
    public static final String USER_PWD = "pwd";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String WATCHED_PROFILE_ACCEPTED = "watched_profile_accepted";
    public static final String PUSH_ACCEPTED = "push_accepted";
    public static final String SHOW_HOME_BTN_START = "show_home_btn_start";

    private SharedPreferences prefs;

    public PrefMgr(SharedPreferences prefs) {
        super();
        this.prefs = prefs;
    }

    public void put(String key, Object value) {
        Editor editor = prefs.edit();
        if (value.getClass().equals(String.class)) {
            editor.putString(key, (String) value);
        } else if (value.getClass().equals(Boolean.class)) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value.getClass().equals(Integer.class)) {
            editor.putInt(key, (Integer) value);
        } else if (value.getClass().equals(Float.class) || value.getClass().equals(Double.class)) {
            editor.putFloat(key, (Float) value);
        }

        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
}
