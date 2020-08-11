package com.character.microblogapp.ui.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.ui.page.setting.SettingActivity;
import com.character.microblogapp.ui.widget.ProgressWheel;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.BuildConfig;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements Constant {

    /*  +---------------------------------------------------------+
    | UI controls & Data members
    +---------------------------------------------------------+   */

    private boolean mFinishAppWhenPressedBackKey = false;
    private boolean mFinish = false;

    ProgressDialog mDlgProgress = null;

    @Nullable
    @BindView(R.id.containerView)
    ViewGroup containerView;

    /*  +---------------------------------------------------------+
    | Overrides
    +---------------------------------------------------------+   */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BuildConfig.DEBUG) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        initUI();

        if (containerView != null) {
            setupUI(containerView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        overridePendingTransition(0, 0);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (isGrantedResult(grantResults))
            onPermissionGranted(requestCode);
        else
            onPermissionDenied(requestCode);
    }

    @Override
    public void onBackPressed() {
        if (mFinishAppWhenPressedBackKey) {
            if (!mFinish) {
                mFinish = true;
                Toaster.showShort(this, R.string.app_finish_message);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFinish = false;
                    }
                }, 2000);
            } else {
                ActivityCompat.finishAffinity(this);
                System.runFinalizersOnExit(true);
                System.exit(0);

            }

            return;
        }

        super.onBackPressed();
    }

    /*  +---------------------------------------------------------+
    | Event handlers
    +---------------------------------------------------------+   */


    /*  +---------------------------------------------------------+
    | Helpers
    +---------------------------------------------------------+   */

    @Override
    public void startActivity(Intent paramIntent) {
        super.startActivity(paramIntent);
        transitionIn();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        transitionIn();
    }

    public void setFinishAppWhenPressedBackKey() {
        mFinishAppWhenPressedBackKey = true;
    }

    private void transitionIn() {
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    private void transitionOut() {
        overridePendingTransition(R.anim.hold, R.anim.fadeout);
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String[] permissions, int code) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> deniedPermissions = getDeniedPermissions(this, permissions);
            if (deniedPermissions.isEmpty()) {
                onPermissionGranted(code);
            } else {
                permissions = new String[deniedPermissions.size()];
                deniedPermissions.toArray(permissions);
                ActivityCompat.requestPermissions(this, permissions, code);
            }
        } else {
            onPermissionGranted(code);
        }
    }

    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedList = new ArrayList<>(2);
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }

    private static boolean isGrantedResult(int... grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) return false;
        }
        return true;
    }

    protected void onPermissionGranted(int code) {
    }

    protected void onPermissionDenied(int code) {
        if (code == PERMISSIONS_MEDIA_REQUEST) {
            // 권한허용을 하지 않았으므로 알림팝업을 띄우고 앱을 종료한다.
            new AlertDialog.Builder(this)
                    .setMessage(R.string.permission_msg)
                    .setPositiveButton(R.string.permission_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 앱세팅페이지로 이행
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    protected void initUI() {
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(BaseActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            try {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }
        }
    }

    public void showProgress(Context ctx) {
        if (mDlgProgress != null && mDlgProgress.isShowing())
            return;

        mDlgProgress = new ProgressDialog(ctx);

        mDlgProgress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDlgProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDlgProgress.getWindow().setDimAmount(0);

        mDlgProgress.setCancelable(false);
        mDlgProgress.setCanceledOnTouchOutside(false);
        mDlgProgress.setIndeterminate(false);

        mDlgProgress.show();

        mDlgProgress.setContentView(R.layout.popup_progress);
        ProgressWheel progressWheel = mDlgProgress.findViewById(R.id.progressWheel);
        progressWheel.spin();
    }

    public void hideProgress() {
        if (mDlgProgress != null && mDlgProgress.isShowing()) {
            mDlgProgress.dismiss();
            mDlgProgress = null;
        }
    }

    public void networkErrorOccupied(MError error) {
        hideProgress();

        if (error.resultcode == 500) {
            if (Util.isNetworkAvaliable(this)) {
                Toaster.showShort(this, R.string.error_unknown);
            } else {
                Toaster.showShort(this, R.string.error_unknown_token);
            }
        } else {
            Toaster.showShort(this, R.string.data_parse_error);
        }
    }

    public void startActivity(Context from, Class to) {
        Intent intent = new Intent(from, to);
        startActivity(intent);
    }

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    @BindingAdapter("app:fontname")
    public static void setFont(TextView textView, String fontName) {
        Typeface face = getFont(textView.getContext(), fontName);
        if (face != null)
            textView.setTypeface(face);
    }

    @BindingAdapter({"app:fontname"})
    public static void setFont(Button btn, String fontName) {
        Typeface face = getFont(btn.getContext(), fontName);
        if (face != null)
            btn.setTypeface(face);
    }

    @BindingAdapter({"app:fontname"})
    public static void setFont(EditText edt, String fontName) {
        Typeface face = getFont(edt.getContext(), fontName);
        if (face != null)
            edt.setTypeface(face);
    }

    @BindingAdapter({"app:fontname"})
    public static void setFont(RadioButton rb, String fontName) {
        Typeface face = getFont(rb.getContext(), fontName);
        if (face != null)
            rb.setTypeface(face);
    }

    public static Typeface getFont(Context context, String name) {
        Typeface typeface = fontCache.get(name);
        if(typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            fontCache.put(name, typeface);
        }

        return typeface;
    }
}
