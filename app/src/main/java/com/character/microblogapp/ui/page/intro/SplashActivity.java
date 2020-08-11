package com.character.microblogapp.ui.page.intro;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.character.microblogapp.R;
import com.character.microblogapp.databinding.ActivitySplashBinding;
import com.character.microblogapp.ui.page.BaseActivity;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        setFinishAppWhenPressedBackKey();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nextStep();
    }

    void nextStep() {
        loginHandler.sendEmptyMessageDelayed(0, 3000);
    }

    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            startActivity(SplashActivity.this, Intro2Activity.class);
            finish();
            return false;
        }
    });
}
