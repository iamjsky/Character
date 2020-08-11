package com.character.microblogapp.ui.page.intro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.databinding.ActivityLoginBinding;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Api;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import org.cybergarage.util.StringUtil;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private static OAuthLogin mOAuthLoginModule;
    private OAuthLoginHandler mOAuthLoginHandler;

    // 카카오 로그인
    @BindView(R.id.com_kakao_login)
    LoginButton btnKakaoLogin;

    private SessionCallback kakaoCallback;

    private CallbackManager fbCallbackManager;

    int nType = LOGIN_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,  R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GO_ACT_CERT_PHONE && resultCode == RESULT_OK && nType == LOGIN_EMAIL) {
            String birthday = data.getStringExtra("birthday");

            Intent intent1 = new Intent(LoginActivity.this, FindPwdActivity.class);
            intent1.putExtra("birthday", birthday);
            startActivity(intent1);
        }

        if (nType == LOGIN_FACEBOOK) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

//        if (nType == LOGIN_KAKAO) {
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return;
            }
//        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        initNaver();
        initKakao();
        initFacebook();
    }

    private void initNaver() {
        mOAuthLoginHandler = new NaverLoginHandler(this);
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                this,
                "YQ9Z4tlK24FUj3wISAzX",
                "ZlvzPcREoo",
                "성격지상주의"
        );
    }

    private void initKakao() {
//        new KakaoLoginHelper().kakaoData(LoginActivity.this);
        kakaoCallback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(kakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initFacebook() {
        // facebook
//        FacebookSdk.sdkInitialize(getApplicationContext());
        fbCallbackManager = CallbackManager.Factory.create();
    }

    @OnClick({R.id.btnEmail
            , R.id.btnFacebook
            , R.id.btnNaver
            , R.id.btnKakao
            , R.id.btnSignup
            , R.id.btnIdFind
            , R.id.btnPasswordFind})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                startActivity(LoginActivity.this, SignupReadyActivity.class);
                break;
            case R.id.btnEmail:
                nType = LOGIN_EMAIL;
                onLogin();
                break;
            case R.id.btnIdFind:
                startActivity(this, FindIdActivity.class);
                break;
            case R.id.btnPasswordFind:
                if (CERT_TEST) {
                    startActivity(this, FindPwdActivity.class);
                } else {
                    Intent intent1 = new Intent(LoginActivity.this, CertActivity.class);
                    startActivityForResult(intent1, GO_ACT_CERT_PHONE);
                }
                break;

            case R.id.btnNaver:
                nType = LOGIN_NAVER;
                onClickNaver();
                break;

            case R.id.btnKakao:
                nType = LOGIN_KAKAO;
//                btnKakaoLogin.performClick();
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, LoginActivity.this);
                break;

            case R.id.btnFacebook:
                nType = LOGIN_FACEBOOK;
                onClickFacebook();
                break;
            default:
                Toaster.showNIToast(this);
        }
    }

    void goMain() {

        Intent intent = new Intent(
                this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void go2Character() {
        Intent intent = new Intent(
                this, CharacterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void onLogin() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

//        if (!StringUtil.hasData(email)) {
        if (email.isEmpty()) {
            Toaster.showShort(this, "아이디로 설정한 이메일을 입력해주십시오.");
            return;
        }

//        if (!StringUtil.hasData(password)) {
        if (password.isEmpty()) {
            Toaster.showShort(this, R.string.input_password);
            return;
        }

        showProgress(this);
        Net.instance().api.login(email, password, LOGIN_EMAIL, MyInfo.getInstance().fcm_token)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().email = email;
                        MyInfo.getInstance().pwd = password;
                        MyInfo.getInstance().login_type = LOGIN_EMAIL;
                        MyInfo.getInstance().transformData(response);
                        MyInfo.getInstance().save(LoginActivity.this);

                        if (response.info.character.isEmpty()) {
                            // 성격테스트를 진행하지 않은 회원이므로 성격테스트페이지로 이행
                            go2Character();
                        } else {
                            // 실행후 심사통과 화면 계속 노출되는 오류 수정 yj 2020-04-13
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences(PrefMgr.APP_PREFS,
                                    Context.MODE_PRIVATE);
                            PrefMgr prefMgr = new PrefMgr(prefs);
                            prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);

                            goMain();
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            if (response.resultcode == 15) {
                                new AlertDialog.Builder(LoginActivity.this).setTitle(R.string.login_failed).setMessage("탈퇴한 회원입니다.").setPositiveButton(R.string.confirm, null).show();
                            } else {
                                new AlertDialog.Builder(LoginActivity.this).setTitle(R.string.login_failed).setMessage(R.string.login_failed_message).setPositiveButton(R.string.confirm, null).show();
                            }
                        }
                    }
                });
    }

    public void snsOnLogin(final String snsId, final String snsEmaill, final int snsType) {
        showProgress(this);
        Net.instance().api.login(snsId, "111111", snsType, MyInfo.getInstance().fcm_token)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().email = snsId;
                        MyInfo.getInstance().pwd = "111111";
                        MyInfo.getInstance().login_type = snsType;
                        MyInfo.getInstance().transformData(response);
                        MyInfo.getInstance().save(LoginActivity.this);

                        if (response.info.character.isEmpty()) {
                            // 성격테스트를 진행하지 않은 회원이므로 성격테스트페이지로 이행
                            go2Character();
                        } else {
                            // 실행후 심사통과 화면 계속 노출되는 오류 수정 yj 2020-04-13
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences(PrefMgr.APP_PREFS,
                                    Context.MODE_PRIVATE);
                            PrefMgr prefMgr = new PrefMgr(prefs);
                            prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);
                            startActivity(LoginActivity.this, MainActivity.class);
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 15) {
                            new AlertDialog.Builder(LoginActivity.this).setTitle(R.string.login_failed).setMessage("다수의 신고로 활동이 정지된 회원입니다.").setPositiveButton(R.string.confirm, null).show();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this).setTitle(R.string.login_failed).setMessage(R.string.login_failed_message).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    public void snsLogin(final int type, final String id, String mb_name, final String mb_nick, final String mb_email) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mb_name", mb_nick);
                    obj.put("identifier", id);
                    obj.put("mb_email", mb_email);
                    obj.put("mb_nick", mb_nick);

                    snsOnLogin(id, mb_email, type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void onClickNaver() {
        mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
    }

    void onClickFacebook() {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(fbCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("Facebook", "onSuccess");
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("Facebook", "result: " + object.toString());
                                String id = "", name = "";
                                String email = "", gender = "";
                                try {
                                    if (response.getJSONObject().isNull("id"))
                                        id = "";
                                    else
                                        id = (String) response.getJSONObject().get("id");

                                    if (response.getJSONObject().isNull("name"))
                                        name = "";
                                    else
                                        name = (String) response.getJSONObject().get("name");

                                    if (response.getJSONObject().isNull("email"))
                                        email = "";
                                    else
                                        email = (String) response.getJSONObject().get("email");

                                    if (response.getJSONObject().isNull("gender"))
                                        gender = "";
                                    else
                                        gender = (String) response.getJSONObject().get("gender");

                                    final String strEmail = email;

                                    Log.e("Facebook", "id: " + id);
                                    Log.e("Facebook", "name: " + name);
                                    Log.e("Facebook", "email: " + email);
                                    Log.e("Facebook", "gender: " + gender);

                                    //Facebook Login
                                    snsLogin(Constant.LOGIN_FACEBOOK, id, name, name, email);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
//                              parameters.putString("fields", "id, name, email, gender, birthday");
                        parameters.putString("fields", "id,name,email,gender");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.e("Facebook", "onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook", "onError: " + exception.getLocalizedMessage());
                        LoginManager.getInstance().logOut();
                    }

                });
    }


    public class NaverLoginHandler extends OAuthLoginHandler {
        public final WeakReference<Activity> mActivity;

        public NaverLoginHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }


        @Override
        public void run(boolean success) {
            final Activity activity = mActivity.get();

            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(activity);
                String tokenType = mOAuthLoginModule.getTokenType(activity);

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://openapi.naver.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();
                Api api = retrofit.create(Api.class);

                Call<ResponseBody> result = api.getNaverProfile(tokenType + " " + accessToken);
                result.enqueue(new retrofit2.Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String strResult = response.body().string();
                            Log.e("dd", strResult);
                            JSONObject jsonObject = new JSONObject(strResult);
                            JSONObject joData = jsonObject.getJSONObject("response");
                            String id = joData.getString("id");
//                            String name = joData.getString("name");
                            String nickname = joData.getString("nickname");
                            String email = joData.getString("email");
                            snsLogin(Constant.LOGIN_NAVER, id, nickname, nickname, email);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(activity).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(activity);
                Toast.makeText(activity, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    // 카톡로그인 콜백
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onSuccess(MeV2Response response) {
                    Logger.d("user id : " + response.getId());

                    String id = response.getId() + "";
                    String name = response.getNickname();
                    String nickname = response.getNickname();
                    String email = response.getKakaoAccount().getEmail() == null ? "" : response.getKakaoAccount().getEmail();

                    UserManagement.getInstance().requestLogout(null);

                    if (nType == LOGIN_KAKAO) {
                        snsLogin(Constant.LOGIN_KAKAO, id, name, nickname, email);
                        nType = LOGIN_EMAIL;
                    }

                }

            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
//            Toast.makeText(LoginActivity.this, "SessionOpenFailed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakaoCallback);
    }

//    public class KakaoLoginHelper {
//
//
//        public String TAG = "test";
//
//        public SessionCallback callback;
//
//
//        public void kakaoData(Activity activity) {
//
//            Log.e("dfgkfdgkfdj", "토큰큰 : ");
//
//            callback = new SessionCallback();
//            Session.getCurrentSession().addCallback(callback);
//            Session.getCurrentSession().checkAndImplicitOpen();
//
//            Log.e(TAG, "토큰큰 : " + Session.getCurrentSession().getTokenInfo().getAccessToken());
//            Log.e(TAG, "토큰큰 리프레쉬토큰 : " + Session.getCurrentSession().getTokenInfo().getRefreshToken());
//            Log.e(TAG, "토큰큰 파이어데이트 : " + Session.getCurrentSession().getTokenInfo().getRemainingExpireTime());
//
//
//        }
//
//
//        public class SessionCallback implements ISessionCallback {
//
//            @Override
//            public void onSessionOpened() {
//                requestMe();
//            }
//
//            @Override
//            public void onSessionOpenFailed(KakaoException exception) {
//                if (exception != null) {
//                    Log.e(TAG, "exception : " + exception);
//                }
//            }
//        }
//
//        /**
//         * 사용자에 대한 정보를 가져온다
//         **/
//        public void requestMe() {
//
//            List<String> keys = new ArrayList<>();
//            keys.add("properties.nickname");
//            keys.add("properties.profile_image");
//            keys.add("kakao_account.email");
//
//            UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
//                @Override
//                public void onFailure(ErrorResult errorResult) {
//                    super.onFailure(errorResult);
//                    Log.e(TAG, "requestMe onFailure message : " + errorResult.getErrorMessage());
//                }
//
//                @Override
//                public void onFailureForUiThread(ErrorResult errorResult) {
//                    super.onFailureForUiThread(errorResult);
//                    Log.e(TAG, "requestMe onFailureForUiThread message : " + errorResult.getErrorMessage());
//                }
//
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) {
//                    Log.e(TAG, "requestMe onSessionClosed message : " + errorResult.getErrorMessage());
//                }
//
//                @Override
//                public void onSuccess(MeV2Response result) {
//                    Log.e(TAG, "requestMe onSuccess message : " + result.getKakaoAccount().getEmail() + " " + result.getId() + " " + result.getNickname());
//
//                    if (nType == LOGIN_KAKAO)
//                        snsLogin(Constant.LOGIN_KAKAO, String.valueOf(result.getId()), result.getNickname(), result.getNickname(), result.getKakaoAccount().getEmail());
//
////                    onClickLogout();
//                }
//
//            });
//        }
//
//        /**
//         * 로그아웃시
//         **/
//        public void onClickLogout() {
//
//            UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) {
//                }
//
//                @Override
//                public void onNotSignedUp() {
//                }
//
//                @Override
//                public void onSuccess(Long result) {
//                }
//            });
//        }
//    }
}
