package com.character.microblogapp.ui.page.intro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Api;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.setting.NoticeActivity;
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

public class SignupReadyActivity extends BaseActivity {

    private static OAuthLogin mOAuthLoginModule; // 네이버
    private OAuthLoginHandler mOAuthLoginHandler; // 카카오
    private CallbackManager fbCallbackManager; // 페북

    @BindView(R.id.com_kakao_login)
    LoginButton btnKakaoLogin;

    private SessionCallback kakaoCallback;
	
	String sns_email = "sns@gmail.com";

	int nType = LOGIN_EMAIL;
	boolean isCertSuccess = false;

	String str_cert_birth = "";
    String str_cert_birthday = "";
    String str_cert_phone = "";
    String str_cert_gender = "";
    String str_cert_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_ready);

        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GO_ACT_CERT_PHONE && resultCode == RESULT_OK) {
            str_cert_birth = data.getStringExtra("birth");
            str_cert_birthday = data.getStringExtra("birthday");
            str_cert_phone = data.getStringExtra("phone");
            str_cert_gender = data.getStringExtra("gender");
            str_cert_name = data.getStringExtra("name");

            showProgress(this);
            Net.instance().api.check_phone_dup(str_cert_phone)
                    .enqueue(new Net.ResponseCallBack<MBase>() {
                        @Override
                        public void onSuccess(MBase response) {
                            super.onSuccess(response);
                            hideProgress();
                            isCertSuccess = true;
                            if (nType == LOGIN_EMAIL) {
                                Intent intent1 = new Intent(SignupReadyActivity.this, SignupActivity.class);
                                intent1.putExtra("login_type", LOGIN_EMAIL);
                                intent1.putExtra("birth", str_cert_birth);
                                intent1.putExtra("birthday", str_cert_birthday);
                                intent1.putExtra("gender", str_cert_gender);
                                intent1.putExtra("phone", str_cert_phone);
                                intent1.putExtra("name", str_cert_name);
                                MyInfo.getInstance().signUp_type = 0;
                                startActivity(intent1);
                            } else if (nType == LOGIN_FACEBOOK) {
                                MyInfo.getInstance().signUp_type = 1;
                                onClickFacebook();
                            } else if (nType == LOGIN_NAVER) {
                                MyInfo.getInstance().signUp_type = 2;
                                mOAuthLoginModule.startOauthLoginActivity(SignupReadyActivity.this, mOAuthLoginHandler);
                            } else if (nType == LOGIN_KAKAO) {
//                                btnKakaoLogin.performClick();
                                MyInfo.getInstance().signUp_type = 3;
                                Session.getCurrentSession().open(AuthType.KAKAO_TALK, SignupReadyActivity.this);
                            }
                        }

                        @Override
                        public void onFailure(MError response) {
                            super.onFailure(response);
                            isCertSuccess = false;
                            if (response.resultcode == 500) {
                                hideProgress();
                                networkErrorOccupied(response);
                            } else {
                                hideProgress();
                                new AlertDialog.Builder(SignupReadyActivity.this).setTitle(R.string.phone_duplicate).setMessage(R.string.phone_duplicate_message).setPositiveButton(R.string.confirm, null).show();
                            }
                        }
                    });
            return;
        }

        if (nType == LOGIN_KAKAO) {
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

        if (nType == LOGIN_FACEBOOK) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void initUI() {
        super.initUI();

        initFacebook();
        initKakao();
        initNaver();
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
//        new KakaoLoginHelper().kakaoData(SignupReadyActivity.this);
        kakaoCallback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(kakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initFacebook() {
        fbCallbackManager = CallbackManager.Factory.create();
    }

    @OnClick({R.id.btnEmail
            , R.id.btnFacebook
            , R.id.btnNaver
            , R.id.btnKakao
            , R.id.rlt_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEmail:
                nType = LOGIN_EMAIL;
                if (CERT_TEST) {
                    Intent intent1 = new Intent(SignupReadyActivity.this, SignupActivity.class);
                    intent1.putExtra("login_type", LOGIN_EMAIL);
                    startActivity(intent1);

                } else {
                    Intent intent1 = new Intent(SignupReadyActivity.this, CertActivity.class);
                    startActivityForResult(intent1, GO_ACT_CERT_PHONE);
                }

                break;
            case R.id.btnFacebook:
                nType = LOGIN_FACEBOOK;
//                onClickFacebook();
                Intent intent1 = new Intent(SignupReadyActivity.this, CertActivity.class);
                startActivityForResult(intent1, GO_ACT_CERT_PHONE);
                break;
            case R.id.btnNaver:
                nType = LOGIN_NAVER;
//                mOAuthLoginModule.startOauthLoginActivity(SignupReadyActivity.this, mOAuthLoginHandler);
                Intent intent2 = new Intent(SignupReadyActivity.this, CertActivity.class);
                startActivityForResult(intent2, GO_ACT_CERT_PHONE);
                break;
            case R.id.btnKakao:
                nType = LOGIN_KAKAO;
//                btnKakaoLogin.performClick();
                Intent intent3 = new Intent(SignupReadyActivity.this, CertActivity.class);
                startActivityForResult(intent3, GO_ACT_CERT_PHONE);
                break;
            case R.id.rlt_back:
                finish();
                break;
            default:
                Toaster.showNIToast(this);
        }
    }

    @OnClick(R.id.txv_useterm)
    void OnClickUseTerm() {
        Intent intent = new Intent(SignupReadyActivity.this, NoticeActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.txv_privacypolicy)
    void OnClickPrivacy() {
        Intent intent = new Intent(SignupReadyActivity.this, NoticeActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @OnClick(R.id.txv_gpsterm)
    void OnClickGpsTerm() {
        Intent intent = new Intent(SignupReadyActivity.this, NoticeActivity.class);
        intent.putExtra("type", 3);
        startActivity(intent);
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

                                    Log.e("Facebook", "id: " + id);
                                    Log.e("Facebook", "name: " + name);
                                    Log.e("Facebook", "email: " + email);
                                    Log.e("Facebook", "gender: " + gender);

                                    //Facebook Login
                                    goToLoginWithSns(LOGIN_FACEBOOK, id, name, email, gender);

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

    private void goToLoginWithSns(final int sns_type, final String sns_id, final  String sns_nick, final String sns_email, String gender) {
        // 이미 가입된 회원인지 체크

        showProgress(this);
        Net.instance().api.login(sns_id, "111111", sns_type, MyInfo.getInstance().fcm_token)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().email = sns_id;
                        MyInfo.getInstance().pwd = "111111";
                        MyInfo.getInstance().login_type = sns_type;
                        MyInfo.getInstance().transformData(response);
                        MyInfo.getInstance().save(SignupReadyActivity.this);

                        startActivity(SignupReadyActivity.this, MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        Intent intent = new Intent(SignupReadyActivity.this, SignupActivity.class);
                        intent.putExtra("login_type", sns_type);
                        intent.putExtra("sns_id", sns_id);
                        intent.putExtra("sns_nick", sns_nick);
                        intent.putExtra("email", sns_email);
                        intent.putExtra("gender", str_cert_gender);
                        intent.putExtra("birth", str_cert_birth);
                        intent.putExtra("birthday", str_cert_birthday);
                        intent.putExtra("phone", str_cert_phone);
                        intent.putExtra("name", str_cert_name);
                        startActivity(intent);
                    }
                });

    }

    /**
     * 네이버 로그인 핸들러
     */
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
                            String nickname = joData.getString("nickname");
                            String email = joData.getString("email");
                            String gender = ""; //joData.getString("gender");
                            goToLoginWithSns(LOGIN_NAVER, id, nickname, email, gender);
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
//                    String name = response.getNickname();
                    String nickname = response.getNickname();
                    String email = response.getKakaoAccount().getEmail() == null ? "" : response.getKakaoAccount().getEmail();
                    String gender = response.getKakaoAccount().getGender() == null ? "" : response.getKakaoAccount().getGender().getValue();

                    UserManagement.getInstance().requestLogout(null);

                    if (nType == LOGIN_KAKAO) {
//                        snsLogin(Constant.LOGIN_KAKAO, id, name, nickname, email);
                        goToLoginWithSns(LOGIN_KAKAO, id, nickname, email, gender);
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

    /**
     * 카카오로그인 핸들러
     */
//    public class KakaoLoginHelper {
//
//
//        public String TAG = "test";
//
//        public KakaoLoginHelper.SessionCallback callback;
//
//
//        public void kakaoData(Activity activity) {
//
//            callback = new KakaoLoginHelper.SessionCallback();
//            Session.getCurrentSession().addCallback(callback);
//            Session.getCurrentSession().checkAndImplicitOpen();
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
//            keys.add("kakao_account.birthday");
//            keys.add("kakao_account.age_range");
//            keys.add("kakao_account.gender");
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
//                    if (nType == LOGIN_KAKAO && isCertSuccess) {
//                        goToLoginWithSns(LOGIN_KAKAO, String.valueOf(result.getId()), result.getNickname(), result.getKakaoAccount().getEmail(), result.getKakaoAccount().getGender().getValue());
//                        nType = LOGIN_EMAIL;
//                    }
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
