package com.character.microblogapp.ui.page.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.character.microblogapp.util.PrefMgr.PUSH_ACCEPTED;
import static com.character.microblogapp.util.PrefMgr.SHOW_HOME_BTN_START;

public class Intro2Activity extends BaseActivity {

    @BindView(R.id.iv_splash_gif)
    ImageView ivSplash;

    int app_exit = 0;

    boolean m_isEndAni = false;
    boolean m_isLoginEnd = false;
    boolean m_isLoginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_intro2);

        setFinishAppWhenPressedBackKey();

        SharedPreferences prefs = getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        PrefMgr prefMgr = new PrefMgr(prefs);

        int tab = getIntent().getIntExtra("tab", 0);
        if (tab > 0) {

            prefMgr.put(PUSH_ACCEPTED, tab);
        }

        prefMgr.put(SHOW_HOME_BTN_START, 0);
        nextStep();
        doAutoLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        app_exit = 1;
        loginHandler.sendEmptyMessage(0);
    }

    void nextStep() {
        Glide.with(this)
                .load(R.drawable.splash)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(ivSplash));

        loginHandler.sendEmptyMessageDelayed(0, 3500);
    }

    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            Drawable drawable = ivSplash.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).stop();
            }
            m_isEndAni = true;
            if (m_isLoginEnd) {
                if (app_exit != 1) {
                    if (m_isLoginSuccess) {
                        if (MyInfo.getInstance().character.isEmpty()) {
                            // 성격테스트를 진행하지 않은 회원이므로 성격테스트페이지로 이행
                            go2Character();
                        } else {
                            // 실행후 심사통과 화면 계속 노출되는 오류 수정 yj 2020-04-13
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences(PrefMgr.APP_PREFS,
                                    Context.MODE_PRIVATE);
                            PrefMgr prefMgr = new PrefMgr(prefs);
                            prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);

                            startActivity(Intro2Activity.this, MainActivity.class);
                            finish();
                        }
                    } else {
                        goNext();
                    }
                }
            }
//            doAutoLogin();
            return false;
        }
    });

    void goNext() {
        startActivity(Intro2Activity.this, IntroActivity.class);
        finish();
    }

    void go2Character() {
        Intent intent = new Intent(
                this, CharacterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void doAutoLogin() {
        String email = MyInfo.getInstance().email;
        String password = MyInfo.getInstance().pwd;
        int loginType = MyInfo.getInstance().login_type;

//        showProgress(this);
        Net.instance().api.login(email, password, loginType, MyInfo.getInstance().fcm_token)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
//                        hideProgress();
                        m_isLoginEnd = true;
                        m_isLoginSuccess = true;
                        if (app_exit == 1)
                            return;

                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().transformData(response);

                        if (m_isEndAni) {

                            if (response.info.character.isEmpty()) {
                                // 성격테스트를 진행하지 않은 회원이므로 성격테스트페이지로 이행
                                go2Character();
                            } else {
                                // 실행후 심사통과 화면 계속 노출되는 오류 수정 yj 2020-04-13
                                SharedPreferences prefs = getApplicationContext().getSharedPreferences(PrefMgr.APP_PREFS,
                                        Context.MODE_PRIVATE);
                                PrefMgr prefMgr = new PrefMgr(prefs);
                                prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);

                                startActivity(Intro2Activity.this, MainActivity.class);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
//                        hideProgress();
                        m_isLoginEnd = true;
                        m_isLoginSuccess = false;
                        if (app_exit == 1)
                            return;
                        if (m_isEndAni)
                            goNext();
                    }
                });
    }


}
