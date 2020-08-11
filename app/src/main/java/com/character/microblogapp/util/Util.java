package com.character.microblogapp.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.ui.page.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class Util {

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @SuppressLint("HardwareIds")
    public static String getPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "permission";
        }

        // 전화번호가 국가코드가 붙어 내려오는것과 관련하여 +82 을 없애주어야 한다.
        // 나이스 인증사에서는 국가코드가 없이 내려온다. 국가코드는 한국의 경우 앞에서 세자리이므로 3번째 index부터 얻으면 된다.

        try {
            return mTelephonyMgr != null ? mTelephonyMgr.getLine1Number().replace("+82", "0") : "";
        } catch (Exception e) {
            Toaster.showShort(context, "전화번호가 없는 기기에서는 이용하실수 없습니다.");
            return "";
        }
    }

    public static int px2dp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static String dateFormat(String date, String inputFormat, String outputFormat) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        if (inputFormat.isEmpty()) {
            inputFormat = "yyyy-MM-dd HH:mm:ss";
        }
        if (outputFormat.isEmpty()) {
            outputFormat = "yyyy.MM.dd";
        }

        SimpleDateFormat fmt = new SimpleDateFormat(inputFormat, Locale.getDefault());
        String outputDate = "";

        try {
            Date srcDate = fmt.parse(date);

            fmt = new SimpleDateFormat(outputFormat, Locale.getDefault());
            outputDate = fmt.format(srcDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    public static String getStringTimeFormat_00_00(int second) {
        int m = second / 60;
        int s = second % 60;

        return String.format(Locale.getDefault(), "%02d : %02d", m, s);
    }

    public static String getStringTimeFormat_00_00_00(int second) {
        int h = second / 3600;
        int m = (second % 3600) / 60;
        int s = second % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
    }

    public static boolean checkPermissions(Context context, String[] strArr) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String checkSelfPermission : strArr) {
                if (ContextCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean convertPermissionResult(int[] iArr) {
        for (int i : iArr) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static File createFile() {
        File folder = new File(getFolderPath());
        if (!folder.exists())
            folder.mkdirs();

        Long tsLong = System.currentTimeMillis() / 1000;
        String ext = ".png";
        String filename = tsLong.toString() + ext;
        return new File(folder.toString(), filename);
    }

    public static File createFile(String ext) {
        File folder = new File(getFolderPath());
        if (!folder.exists())
            folder.mkdirs();

        Long tsLong = System.currentTimeMillis() / 1000;
        String filename = tsLong.toString() + "." + ext;
        return new File(folder.toString(), filename);
    }

    private static String getFolderPath() {
        return Environment.getExternalStorageDirectory() + "/" + Constant.SDCARD_FOLDER;
    }

    public static void saveBitmapToFile(BaseActivity activity, Bitmap bitmap, String filepath) {
        if (bitmap == null || filepath.isEmpty()) {
            return;
        }

        if (activity.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            OutputStream fOut = null;
            File file = new File(filepath);
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            activity.requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x11);
        }
    }

    public static void saveBitmapToJPGFile(BaseActivity activity, Bitmap bitmap, String filepath) {
        if (bitmap == null || filepath.isEmpty()) {
            return;
        }

        if (activity.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            OutputStream fOut = null;
            File file = new File(filepath);
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            activity.requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x11);
        }
    }

    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] childFileList = dir.listFiles();

            if (childFileList == null) {
                return;
            }

            for (File childFile : childFileList) {
                if (childFile.isDirectory()) {
                    deleteDir(childFile.getAbsolutePath()); // 하위 디렉토리 루프
                } else {
                    childFile.delete(); // 하위 파일삭제
                }
            }
//            dir.delete(); // root 삭제
        }
    }

    public static void loadImage(Context context, ImageView imageView, String url) {
        if (context == null || imageView == null || url == null || url.isEmpty())
            return;

        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, File file) {
        if (context == null || imageView == null || file == null)
            return;

        Glide.with(context).load(file).into(imageView);
    }


    public static boolean isEmail(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public static boolean isNickname(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        Pattern p = Pattern.compile("^[가-힣]*+[가-힣]*+$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public static String getCountryCode(String res) {
        if (res.isEmpty()) {
            return "";
        }

        return res.replaceAll("[^-+\\d]", "").trim();
    }

    public static boolean isInstalledPackage(Context context, String pkgName) {
        if (pkgName != null) {
            try {
                PackageManager pm = context.getPackageManager();
                pm.getInstallerPackageName(pkgName);
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static String stripHtml(String html) {
//        return Html.fromHtml(html).toString();
        return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").replace("&nbsp;", "").replace("&#39;", "").replace("&quot;", "")
                .replace("&rsquo;", "").replace("&lsquo;", "").replace("&ldquo;", "").replace("&rdquo;", "").replace("&amp;", "")
                .replace("&lsquo;", "").replace("&rsquo;", "");
    }


    /**
     * Check Wifi enable
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * Wifi Enable/Disable
     *
     * @param context
     * @param enabled
     */
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    /**
     * Check 3G/4G enable
     *
     * @param context
     * @return
     */
    public static boolean isMobileDataEnabled(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * 3G/4G Enable / Disable
     *
     * @param context
     * @param enabled
     */
    public static boolean setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(
                    iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvaliable(final Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean wifiConnected = false;
        boolean mobileConnected = false;

        // Checks the user prefs and the network connection. Based on the
        // result, decides
        // whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a
        // Wi-Fi connection.
        if (networkInfo != null && networkInfo.isConnected()) {
            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            // Otherwise, the app can't download content--either because there
            // is no network
            // connection (mobile or Wi-Fi), or because the pref setting is
            // WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
            wifiConnected = false;
            mobileConnected = false;
        }

        return (mobileConnected || wifiConnected);
    }

    public static String[] getSplitCharArrayFromString(String source) {

        if (source == null || source.length() == 0)
            return null;

        String[] result = new String[source.length()];

        for (int i = 0; i < source.length() ; i++) {
            result[i] = source.substring(i, i + 1);
        }

        return result;
    }

    public static String numberFormatString(int price) {

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String formattedStringPrice = myFormatter.format(price);
        return formattedStringPrice;
    }

}
