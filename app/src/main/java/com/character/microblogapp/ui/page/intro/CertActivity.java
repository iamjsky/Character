package com.character.microblogapp.ui.page.intro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;

public class CertActivity extends BaseActivity {

    @BindView(R.id.wv_cert)
    WebView wv_cert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_cert);
    }

    @Override
    protected void initUI() {
        super.initUI();

        wv_cert.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_cert.getSettings().setJavaScriptEnabled(true);

        wv_cert.setWebViewClient(new WebViewClient());

        wv_cert.getSettings().setSavePassword(false);
        wv_cert.getSettings().setAppCacheEnabled(true);

        wv_cert.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_cert.getSettings().setSupportMultipleWindows(true);
        wv_cert.getSettings().setDomStorageEnabled(true);

        wv_cert.getSettings().setLoadWithOverviewMode(true);
        wv_cert.getSettings().setUseWideViewPort(true);
        wv_cert.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wv_cert.setScrollbarFadingEnabled(true);

        wv_cert.setFocusable(true);
        wv_cert.setFocusableInTouchMode(true);
        wv_cert.getSettings().setBuiltInZoomControls(true);
        wv_cert.getSettings().setSupportZoom(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebSettings webViewSettings = wv_cert.getSettings();
            webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(wv_cert, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        wv_cert.setWebChromeClient(new WebChromeClient() {

            // javaScript Alert 출력
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (message == null || message.trim().length() == 0) {

                } else {
                    new AlertDialog.Builder(CertActivity.this)
                            .setMessage(message)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
                return true;
            }

            // javaScript Confirm 출력
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final android.webkit.JsResult result) {
                if (message == null || message.trim().length() == 0) {

                } else {
                    new AlertDialog.Builder(CertActivity.this)
                            .setMessage(message)
                            .setPositiveButton("확인",
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            result.confirm();

                                        }
                                    })
                            .setNegativeButton("취소",
                                    new AlertDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.cancel();
                                        }
                                    }).create().show();
                    //                           .setCancelable(false).create().show();
                }
                return true;
            }

            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
                return false;
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

            }
        });

        wv_cert.addJavascriptInterface(new ContentManager(getApplicationContext()), "webInterface");
        wv_cert.loadUrl(SERVER_CERT_URL);
    }


    private String getAge(String birthday) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formats;
        formats = new SimpleDateFormat("yyyy");
        int time2 = Integer.parseInt(formats.format(cal.getTime()));
        int ageSum = Integer.parseInt(birthday.substring(0, 4));

        return Integer.toString(time2 - ageSum + 1);
    }

    class ContentManager {
        private Context context = null;

        public ContentManager(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void bridge_verify_result(String status, String birthday, String gender, String phone, String name) {
            if (status.equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("birth", getAge(birthday));
                intent.putExtra("birthday", birthday);
                intent.putExtra("gender", gender);
                intent.putExtra("phone", phone);
//                intent.putExtra("phone", "01048760982");
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(CertActivity.this, "본인인증이 실패되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
